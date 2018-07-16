package net.starlegacy.spacecandy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import java.util.Random;

import static com.google.common.primitives.Ints.min;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class SpaceRenderHandler extends IRenderHandler {
    private Star[] stars = new Star[8192];
    private int displayList = -1;

    SpaceRenderHandler() {
        generateStars();
    }

    private void generateStars() {
        final Random random = new Random(0);
        final int radius = 100;
        final double threshold = 0.15;

        for (int i = 0; i < stars.length; i++) {
            Star star = new Star();
            double u = 2 * Math.PI * random.nextDouble();
            double v = Math.acos(1 - 2 * random.nextDouble()) - Math.PI * 0.5;
            double size = (1 + 2 * random.nextDouble()) * .032;

            // prevents aliasing artifacts, making small stars fainter but not smaller
            star.alpha = (int) (255 * (size > threshold ? 1 : (float) Math.min(size * (1 / threshold), 1)));
            star.red = (int) (255 * (0.7f + random.nextFloat() * 0.3f));
            star.green = (int) (255 * (0.7f + random.nextFloat() * 0.3f));
            star.blue = (int) (255 * (0.7f + random.nextFloat() * 0.3f));
            int lowestDifference = min(255 - star.red, 255 - star.green, 255 - star.blue);
            star.red += lowestDifference;
            star.green += lowestDifference;
            star.blue += lowestDifference;
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

        buildStars();
    }

    private void buildStars() {
        GlStateManager.glNewList(displayList = GLAllocation.generateDisplayLists(1), GL11.GL_COMPILE);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        renderStars(buffer);

        tess.draw();
        GlStateManager.glEndList();
    }

    private void renderStars(BufferBuilder buffer) {
        buffer.begin(GL_QUADS, POSITION_COLOR);
        double k = 150;

        for (int i = 0; i < 6; i++) {
            GlStateManager.pushMatrix();

            switch (i) {
                case 1:
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case 2:
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case 3:
                    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case 4:
                    GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                    break;
                case 5:
                    GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
                    break;
            }

            blackVertex(buffer, -k, -k, -k);
            blackVertex(buffer, -k, -k, k);
            blackVertex(buffer, k, -k, k);
            blackVertex(buffer, k, -k, -k);

            GlStateManager.popMatrix();
        }

        for (Star star : stars)
            for (Vector3d point : star.points)
                buffer.pos(point.x, point.y, point.z).color(star.red, star.green, star.blue, star.alpha).endVertex();
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        renderSkySpace();
    }

    private void renderSkySpace() {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);

        GlStateManager.callList(displayList);

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    private void blackVertex(BufferBuilder buffer, double x, double y, double z) {
        buffer.pos(x, y, z).color(0, 0, 0, 255).endVertex();
    }

    private static class Star {
        Vector3d[] points;
        int alpha;
        int red;
        int green;
        int blue;
    }
}
