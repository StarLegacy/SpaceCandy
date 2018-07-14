package net.starlegacy.starforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static net.minecraft.client.Minecraft.*;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class SpaceRenderHandler extends IRenderHandler {
    private static Star[] stars = new Star[4096];
    private static Random random = new Random(0);
    private static final int radius = 100;

    static {
        for (int i = 0; i < stars.length; i++) {
            Star star = new Star();
            Vector3d point = randomSpherePoint(100);
            star.x = point.x;
            star.y = point.y;
            star.z = point.z;
            star.size = random.nextDouble();
            stars[i] = star;
        }
    }

    private static Vector3d randomSpherePoint(int radius) {
        double u = random.nextFloat();
        double v = random.nextFloat();
        double theta = 2 * Math.PI * u;
        double phi = Math.acos(2 * v - 1);
        Vector3d vector3d = new Vector3d();
        vector3d.x = (radius * Math.sin(phi) * Math.cos(theta));
        vector3d.y = (radius * Math.sin(phi) * Math.sin(theta));
        vector3d.z = (radius * Math.cos(phi));
        return vector3d;
    }

    private World world;
    private Minecraft mc;

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
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        for (Star star : stars) {
            bufferbuilder.pos(star.x, star.y, star.z).color(255, 255, 255, 255).endVertex();
        }
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    private static class Star {
        public double x;
        public double y;
        public double z;
        public double size;
    }
}
