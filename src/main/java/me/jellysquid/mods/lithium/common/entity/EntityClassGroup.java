package me.jellysquid.mods.lithium.common.entity;

import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class EntityClassGroup {
    public static final EntityClassGroup BOAT_SHULKER_LIKE_COLLISION;
    public static final EntityClassGroup MINECART_BOAT_LIKE_COLLISION;

    static {
        String remapped_method_30948 = ObfuscationReflectionHelper.findMethod(Entity.class, "method_30948").getName();
        BOAT_SHULKER_LIKE_COLLISION = new EntityClassGroup(
                (Class<?> entityClass) -> isMethodFromSuperclassOverwritten(entityClass, Entity.class, remapped_method_30948));

        String remapped_method_30949 = ObfuscationReflectionHelper.findMethod(Entity.class,"canCollideWith").getName();
        MINECART_BOAT_LIKE_COLLISION = new EntityClassGroup(
                (Class<?> entityClass) -> isMethodFromSuperclassOverwritten(entityClass, Entity.class, remapped_method_30949, Entity.class));

        if ((!MINECART_BOAT_LIKE_COLLISION.contains(MinecartEntity.class))) {
            throw new AssertionError();
        }
        if ((!BOAT_SHULKER_LIKE_COLLISION.contains(ShulkerEntity.class))) {
            throw new AssertionError();
        }
        if ((MINECART_BOAT_LIKE_COLLISION.contains(ShulkerEntity.class))) {
            Logger.getLogger("Lithium EntityClassGroup").warning("Either chunk.entity_class_groups is broken or something else gave Shulkers the minecart-like collision behavior.");
        }
        BOAT_SHULKER_LIKE_COLLISION.clear();
        MINECART_BOAT_LIKE_COLLISION.clear();
    }

    private final Predicate<Class<?>> classFitEvaluator;
    private volatile Reference2ByteOpenHashMap<Class<?>> class2GroupContains;

    public EntityClassGroup(Predicate<Class<?>> classFitEvaluator) {
        this.class2GroupContains = new Reference2ByteOpenHashMap<>();
        Objects.requireNonNull(classFitEvaluator);
        this.classFitEvaluator = classFitEvaluator;
    }

    public void clear() {
        this.class2GroupContains = new Reference2ByteOpenHashMap<>();
    }

    public boolean contains(Class<?> entityClass) {
        byte contains = this.class2GroupContains.getOrDefault(entityClass, (byte) 2);
        if (contains != 2) {
            return contains == 1;
        } else {
            return this.testAndAddClass(entityClass);
        }
    }

    private boolean testAndAddClass(Class<?> entityClass) {
        byte contains;
        synchronized (this) {
            contains = this.class2GroupContains.getOrDefault(entityClass, (byte) 2);
            if (contains != 2) {
                return contains == 1;
            }
            Reference2ByteOpenHashMap<Class<?>> newMap = this.class2GroupContains.clone();
            contains = this.classFitEvaluator.test(entityClass) ? (byte) 1 : (byte) 0;
            newMap.put(entityClass, contains);
            this.class2GroupContains = newMap;
        }
        return contains == 1;
    }

    public static boolean isMethodFromSuperclassOverwritten(Class<?> clazz, Class<?> superclass, String methodName, Class<?>... methodArgs) {
        while (clazz != null && clazz != superclass && superclass.isAssignableFrom(clazz)) {
            try {
                clazz.getDeclaredMethod(methodName, methodArgs);
                return true;
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            } catch (Throwable e) {
                final String crashedClass = clazz.getName();
                CrashReport crashReport = CrashReport.forThrowable(e, "GribCore EntityClassGroup analysis");
                CrashReportCategory crashReportSection = crashReport.addCategory(e.getClass().toString() + " when getting declared methods.");
                crashReportSection.setDetail("Analyzed class", crashedClass);
                crashReportSection.setDetail("Analyzed method name", methodName);
                crashReportSection.setDetail("Analyzed method args", methodArgs);

                throw new ReportedException(crashReport);
            }
        }
        return false;
    }
}