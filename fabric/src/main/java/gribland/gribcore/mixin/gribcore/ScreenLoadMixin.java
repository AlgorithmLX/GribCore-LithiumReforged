package gribland.gribcore.mixin.gribcore;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gribland.gribcore.Constantum;
import gribland.gribcore.ResourceConfig;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

import static net.minecraft.client.gui.GuiComponent.blit;
import static net.minecraft.client.gui.GuiComponent.fill;

@Mixin(value = LoadingOverlay.class, remap = false)
public class ScreenLoadMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow private long fadeOutStart;
    @Shadow private long fadeInStart;
    @Shadow private float currentProgress;
    @Shadow @Final private boolean fadeIn;
    @Shadow @Final private ReloadInstance reload;

    @Shadow @Final private Consumer<Optional<Throwable>> onFinish;
    private static final ResourceLocation LOGO_LOCATION = new ResourceLocation(Constantum.ModId, "textures/title/mojangstudios.png");

    private static final int BRAND_BACKGROUND = FastColor.ARGB32.color(255, 78, 83, 82);
    private static final int BRAND_BACKGROUND_NO_ALPHA;

    @Inject(method = "registerTextures", at = @At("HEAD"), cancellable = true)
    private static void renderTexturesInjecting(Minecraft minecraft, CallbackInfo ci) {
        minecraft.getTextureManager().register(LOGO_LOCATION, new ResourceConfig(LOGO_LOCATION));
        ci.cancel();
    }
    @SuppressWarnings("deprecation")
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderInjecting(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        int k = this.minecraft.getWindow().getGuiScaledWidth();
        int l = this.minecraft.getWindow().getGuiScaledHeight();
        long m = Util.getMillis();
        if (this.fadeIn && (reload.isApplying() || this.minecraft.screen != null) && this.fadeInStart == -1L) {
            this.fadeInStart = m;
        }

        float g = this.fadeOutStart > -1L ? (float)(m - this.fadeOutStart) / 1000.0F : -1.0F;
        float h = this.fadeInStart > -1L ? (float)(m - this.fadeInStart) / 500.0F : -1.0F;
        float o;
        int n;
        if (g >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(poseStack, 0, 0, f);
            }

            n = Mth.ceil((1.0F - Mth.clamp(g - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(poseStack, 0, 0, k, l, BRAND_BACKGROUND_NO_ALPHA | n << 24);
            o = 1.0F - Mth.clamp(g - 1.0F, 0.0F, 1.0F);
        } else if (fadeIn) {
            if (this.minecraft.screen != null && h < 1.0F) {
                this.minecraft.screen.render(poseStack, i, j, f);
            }

            n = Mth.ceil(Mth.clamp(h, 0.15D, 1.0D) * 255.0D);
            fill(poseStack, 0, 0, k, l, BRAND_BACKGROUND_NO_ALPHA | n << 24);
            o = Mth.clamp(h, 0.0F, 1.0F);
        } else {
            fill(poseStack, 0, 0, k, l, BRAND_BACKGROUND);
            o = 1.0F;
        }

        n = (int)((double)this.minecraft.getWindow().getGuiScaledWidth() * 0.5D);
        int p = (int)((double)this.minecraft.getWindow().getGuiScaledHeight() * 0.5D);
        double d = Math.min((double)this.minecraft.getWindow().getGuiScaledWidth() * 0.75D, this.minecraft.getWindow().getGuiScaledHeight()) * 0.25D;
        int q = (int)(d * 0.5D);
        double e = d * 4.0D;
        int r = (int)(e * 0.5D);
        this.minecraft.getTextureManager().bind(LOGO_LOCATION);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.alphaFunc(516, 0.0F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, o);
        blit(poseStack, n - r, p - q, r, (int)d, -0.0625F, 0.0F, 120, 60, 120, 120);
        blit(poseStack, n, p - q, r, (int)d, 0.0625F, 60.0F, 120, 60, 120, 120);
        RenderSystem.defaultBlendFunc();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.disableBlend();
        int s = (int)((double)this.minecraft.getWindow().getGuiScaledHeight() * 0.8325D);
        float t = this.reload.getActualProgress();
        currentProgress= Mth.clamp(this.currentProgress * 0.95F + t * 0.050000012F, 0.0F, 1.0F);
        if (g < 1.0F) {
            drawProgressBar(poseStack, k / 2 - r, s - 5, k / 2 + r, s + 5, 1.0F - Mth.clamp(g, 0.0F, 1.0F));
        }

        if (g >= 2.0F) {
            this.minecraft.setOverlay(null);
        }

        if (this.fadeOutStart == -1L && this.reload.isDone() && (!this.fadeIn || h >= 2.0F)) {
            try {
                this.reload.checkExceptions();
                onFinish.accept(Optional.empty());
            } catch (Throwable var23) {
                onFinish.accept(Optional.of(var23));
            }

            this.fadeOutStart = Util.getMillis();
            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight());
            }
        }
        ci.cancel();
    }

    static {
        BRAND_BACKGROUND_NO_ALPHA = BRAND_BACKGROUND & 808080;
    }

    @Shadow
    private void drawProgressBar(PoseStack poseStack, int i, int j, int k, int l, float f) {
        int m = Mth.ceil((float)(k - i - 2) * this.currentProgress);
        int n = Math.round(f * 255.0F);
        int o = FastColor.ARGB32.color(n, 255, 255, 255);
        fill(poseStack, i + 1, j, k - 1, j + 1, o);
        fill(poseStack, i + 1, l, k - 1, l - 1, o);
        fill(poseStack, i, j, i + 1, l, o);
        fill(poseStack, k, j, k - 1, l, o);
        fill(poseStack, i + 2, j + 2, i + m, l - 2, o);
    }
}
