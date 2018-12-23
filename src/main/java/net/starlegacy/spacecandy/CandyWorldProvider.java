package net.starlegacy.spacecandy;

import net.minecraft.world.WorldProviderEnd;

public class CandyWorldProvider extends WorldProviderEnd {
    @Override
    public boolean shouldClientCheckLighting() {
        return false;
    }
}
