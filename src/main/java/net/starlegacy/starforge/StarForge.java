package net.starlegacy.starforge;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import javax.annotation.Nullable;

import static net.minecraft.client.Minecraft.getMinecraft;

@Mod(
        modid = "starforge",
        name = "StarForge",
        version = "1.0.0"
)
public class StarForge {

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        getMinecraft().effectRenderer.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new IParticleFactory() {
            @Nullable
            @Override
            public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
                return new ParticleLaser(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            }
        });
    }
}
