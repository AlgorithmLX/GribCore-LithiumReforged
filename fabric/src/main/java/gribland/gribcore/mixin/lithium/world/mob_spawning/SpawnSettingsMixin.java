package gribland.gribcore.mixin.lithium.world.mob_spawning;

import com.google.common.collect.Maps;
import gribland.gribcore.lithium.common.util.collections.HashedReferenceList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@SuppressWarnings("AmbiguousMixinReference")
@Mixin(MobSpawnSettings.class)
public class SpawnSettingsMixin {
    @Mutable
    @Shadow
    @Final
    private Map<MobCategory, List<MobSpawnSettings.SpawnerData>> spawners;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void reinit(float creatureSpawnProbability, Map<MobCategory, List<MobSpawnSettings.SpawnerData>> spawners, Map<EntityType<?>, MobSpawnSettings.SpawnerData> spawnCosts, boolean playerSpawnFriendly, CallbackInfo ci) {
        Map<MobCategory, List<MobSpawnSettings.SpawnerData>> spawns = Maps.newEnumMap(MobCategory.class);

        for (Map.Entry<MobCategory, List<MobSpawnSettings.SpawnerData>> entry : this.spawners.entrySet()) {
            spawns.put(entry.getKey(), new HashedReferenceList<>(entry.getValue()));
        }

        this.spawners = spawns;
    }
}
