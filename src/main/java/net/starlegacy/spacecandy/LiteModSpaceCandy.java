package net.starlegacy.spacecandy;

import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.starlegacy.spacecandy.particle.ParticleLaser;
import net.starlegacy.spacecandy.spacerenderer.ISpaceRenderer;
import net.starlegacy.spacecandy.spacerenderer.SkyboxSpaceRenderer;

import java.io.File;

@SuppressWarnings("unused")
public class LiteModSpaceCandy implements LiteMod, InitCompleteListener {
    public static ISpaceRenderer spaceRenderer;

    public LiteModSpaceCandy() {
    }

    @Override
    public String getName() {
        return "SpaceCandy";
    }

    @Override
    public String getVersion() {
        return "2.0-SNAPSHOT";
    }

    @Override
    public void init(File configPath) {
        spaceRenderer = new SkyboxSpaceRenderer();
    }

    @Override
    public void onInitCompleted(Minecraft minecraft, LiteLoader loader) {
        minecraft.effectRenderer.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(),
                (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                        new ParticleLaser(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn)
        );
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {
    }
}