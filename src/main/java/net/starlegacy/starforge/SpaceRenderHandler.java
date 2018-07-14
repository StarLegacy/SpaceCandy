package net.starlegacy.starforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;

import static net.minecraft.client.Minecraft.*;

public class SpaceRenderHandler extends IRenderHandler {
    private World world;
    private Minecraft mc;
    private static final ResourceLocation SPACE_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");

    public SpaceRenderHandler(World world) {
        this.world = world;
        this.mc = getMinecraft();
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        mc.renderEngine.bindTexture(SPACE_TEXTURES);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int k1 = 0; k1 < 6; ++k1)
        {
            GlStateManager.pushMatrix();
//
            if (k1 == 1)
            {
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 2)
            {
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 3)
            {
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 4)
            {
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if (k1 == 5)
            {
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 100.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(100.0D, 100.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(100.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableAlpha();
    }
}
