package net.starlegacy.spacecandy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import java.util.Random;

import static com.google.common.primitives.Ints.min;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class SpaceRenderHandler extends IRenderHandler {
    private static class Star {
        Vector3d[] points;
        int alpha;
        int red;
        int green;
        int blue;
    }

    private static int displayList = -1;

    public static void generateStars() {
        final Random random = new Random(0);
        final int radius = 100;
        final double threshold = 0.15;
        Star[] stars = new Star[8192];
        for (int i = 0; i < stars.length; i++) {
            Star star = new Star();
            double u = 2 * Math.PI * random.nextDouble();
            double v = Math.acos(1 - 2 * random.nextDouble()) - Math.PI * 0.5;
            double size = (1 + 2 * random.nextDouble()) * .055;

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

        if (displayList > -1) GlStateManager.glDeleteLists(displayList, 1);

        GlStateManager.glNewList(displayList = GLAllocation.generateDisplayLists(1), GL11.GL_COMPILE_AND_EXECUTE);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        renderStars(tess, buffer, stars);

        GlStateManager.glEndList();
    }

    private static void renderStars(Tessellator tess, BufferBuilder buffer, Star[] stars) {
        for (int i = 0; i < 6; i++) {
            GlStateManager.pushMatrix();
            if (i == 1) GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            else if (i == 2) GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            else if (i == 3) GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            else if (i == 4) GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            else if (i == 5) GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            final double k = 110;
            buffer.begin(GL_QUADS, POSITION_COLOR);
            blackVertex(buffer, -k, -k, k);
            blackVertex(buffer, -k, k, k);
            blackVertex(buffer, k, k, k);
            blackVertex(buffer, k, -k, k);
            tess.draw();

            GlStateManager.popMatrix();
        }

        buffer.begin(GL_QUADS, POSITION_COLOR);

        for (Star star : stars)
            for (Vector3d point : star.points)
                buffer.pos(point.x, point.y, point.z).color(star.red, star.green, star.blue, star.alpha).endVertex();
        tess.draw();
    }

    private static void blackVertex(BufferBuilder buffer, double x, double y, double z) {
        buffer.pos(x, y, z).color(0, 0, 0, 255).endVertex();
    }

    public SpaceRenderHandler() {
        generateStars();
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

        renderSkySpace();

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    private void renderSkySpace() {
        GlStateManager.callList(displayList);
    }
}
