package gribland.gribcore;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceConfig extends SimpleTexture {
    public ResourceConfig(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    protected TextureImage getTextureImage(ResourceManager resourceManager) {
        try {
            InputStream is = new FileInputStream(new File(Minecraft.getInstance().gameDirectory + "/" + Constantum.ModId) + "/" +this.location.toString().replace(Constantum.ModId + ":", ""));
            TextureImage image;

            try {
                image = new TextureImage(new TextureMetadataSection(false,true), NativeImage.read(is));
            } finally {
                is.close();
            }
            return image;
        } catch (IOException e) {
            return new TextureImage(e);
        }
    }
}
