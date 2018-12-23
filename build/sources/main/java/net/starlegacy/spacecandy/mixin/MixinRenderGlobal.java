package net.starlegacy.spacecandy.mixin;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.world.IWorldEventListener;
import net.starlegacy.spacecandy.LiteModSpaceCandy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal implements IWorldEventListener, IResourceManagerReloadListener
{
    /**
     * @author MicleBrick
     */
    @Overwrite
    public void renderSkyEnd() {
        LiteModSpaceCandy.spaceRenderer.render();
    }
}
