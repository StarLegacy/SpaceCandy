package net.starlegacy.starforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL42;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import java.util.Random;

import static net.minecraft.client.Minecraft.*;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class SpaceRenderHandler extends IRenderHandler {
    private World world;
    private Minecraft mc;

    private static final int radius = 100;
    private Star[] stars = new Star[8000];

    public SpaceRenderHandler(World world) {
        this.world = world;
        this.mc = getMinecraft();
        generateStars();
    }

    private void generateStars() {
        final Random random = new Random(0);
        final int radius = 100;
        final double threshold = 0.15;

        for (int i = 0; i < stars.length; i++) {
            Star star = new Star();
            double u = 2 * Math.PI * random.nextFloat();
            double v = Math.acos(1 - 2 * random.nextFloat()) - Math.PI * 0.5;
            double size = (1 + 2 * random.nextFloat()) * .032f;

            // prevents aliasing artifacts, making small stars fainter but not smaller
            star.alpha = size > threshold ? 1 : Math.min(size * (1 / threshold), 1);
            if (size < threshold) size = threshold;

            star.points = new Vector3d[]{
                    new Vector3d(-size, -size, radius),
                    new Vector3d(-size, size, radius),
                    new Vector3d(size, size, radius),
                    new Vector3d(size, -size, radius),
            };

            Matrix3d matrix1 = new Matrix3d();
            matrix1.rotY(u);
            Matrix3d matrix2 = new Matrix3d();
            matrix2.rotX(v);
            matrix1.mul(matrix2);

            matrix1.transform(star.points[0]);
            matrix1.transform(star.points[1]);
            matrix1.transform(star.points[2]);
            matrix1.transform(star.points[3]);

            stars[i] = star;
        }
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for (Star star : stars)
        {
            for (Vector3d point : star.points)
            {
                bufferbuilder.pos(point.x, point.y, point.z).color(210, 210, 255, (int)(255 * star.alpha)).endVertex();
            }
        }
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    private static class Star
    {
        Vector3d[] points;
        double alpha;
    }
}
