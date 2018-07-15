package net.starlegacy.spacecandy;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class ParticleLaser extends Particle {
    public ParticleLaser(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double red, double green, double blue) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, red, green, blue);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.particleRed = (float) red;
        this.particleGreen = (float) green;
        this.particleBlue = (float) blue;
        this.particleScale = 1;
        this.particleMaxAge = 1;
        this.setParticleTextureIndex(7);
    }

    @Override
    public int getBrightnessForRender(float something) {
        return 0xF000F0;
    }

    @Override
    public void onUpdate() {
        if (particleAge++ >= particleMaxAge) setExpired();
        particleScale = 0.5f;
    }
}
