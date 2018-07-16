package net.starlegacy.spacecandy;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandDebug;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.starlegacy.spacecandy.command.CommandReloadStars;

import javax.annotation.Nullable;

import static net.minecraft.client.Minecraft.getMinecraft;

@Mod(
        modid = "spacecandy",
        name = "SpaceCandy",
        version = "1.2.0"
)
@Mod.EventBusSubscriber
public class SpaceCandy {

    private static SpaceRenderHandler skyRenderer;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        skyRenderer = new SpaceRenderHandler();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        getMinecraft().effectRenderer.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new IParticleFactory() {
            @Nullable
            @Override
            public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
                return new ParticleLaser(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            }
        });

        ClientCommandHandler.instance.registerCommand(new CommandReloadStars());
    }

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (world.isRemote && world.provider.getDimensionType() == DimensionType.THE_END) {
            world.provider.setSkyRenderer(skyRenderer);
        }
    }
}
