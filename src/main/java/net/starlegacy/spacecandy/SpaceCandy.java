package net.starlegacy.spacecandy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.starlegacy.spacecandy.spacerenderer.ISpaceRenderer;
import net.starlegacy.spacecandy.spacerenderer.SkyboxSpaceRenderer;

import static net.minecraft.client.Minecraft.getMinecraft;

@Mod(modid = "spacecandy", name = "SpaceCandy", version = "3.0.0")
@Mod.EventBusSubscriber
public class SpaceCandy {
    private static ISpaceRenderer spaceRenderer;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        DimensionType.THE_END.clazz = CandyWorldProvider.class;
        spaceRenderer = new SkyboxSpaceRenderer();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        getMinecraft().effectRenderer.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(),
                (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                        new ParticleLaser(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn));
    }

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        World world = event.getWorld();

        if (world.isRemote && world.provider.getDimensionType() == DimensionType.THE_END) {
            world.provider.setSkyRenderer(new IRenderHandler() {
                @Override
                public void render(float partialTicks, WorldClient world, Minecraft mc) {
                    spaceRenderer.render();
                }
            });
        }
    }
}
