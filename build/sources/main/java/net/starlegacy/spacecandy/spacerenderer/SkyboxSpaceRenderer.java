package net.starlegacy.spacecandy.spacerenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.client.Minecraft.getMinecraft;

public class SkyboxSpaceRenderer implements ISpaceRenderer {
    private Minecraft mc;
    private ResourceLocation[] SPACE_TEXTURES = new ResourceLocation[6];

    public SkyboxSpaceRenderer() {
        this.mc = getMinecraft();
        SPACE_TEXTURES[0] = new ResourceLocation("spacecandy", "textures/environment/space_down.png");
        SPACE_TEXTURES[1] = new ResourceLocation("spacecandy", "textures/environment/space_front.png");
        SPACE_TEXTURES[2] = new ResourceLocation("spacecandy", "textures/environment/space_back.png");
        SPACE_TEXTURES[3] = new ResourceLocation("spacecandy", "textures/environment/space_up.png");
        SPACE_TEXTURES[4] = new ResourceLocation("spacecandy", "textures/environment/space_left.png");
        SPACE_TEXTURES[5] = new ResourceLocation("spacecandy", "textures/environment/space_right.png");
    }

    @Override
    public void render() {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int i = 0; i < 6; ++i) {
            mc.getTextureManager().bindTexture(SPACE_TEXTURES[i]);
            GlStateManager.pushMatrix();

            if (i == 1) {
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (i == 2) {
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(180.0f, 0.0F, 1.0F, 0.0F);
            }

            if (i == 3) {
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (i == 4) {
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-90.0f, 0.0F, 1.0F, 0.0F);
            }

            if (i == 5) {
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(90.0f, 0.0F, 1.0F, 0.0F);
            }

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            double size = 100.0D;
            bufferbuilder.pos(-size, -size, -size).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(-size, -size, size).tex(0.0D, 1).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(size, -size, size).tex(1, 1).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(size, -size, -size).tex(1, 0.0D).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableAlpha();
    }
}