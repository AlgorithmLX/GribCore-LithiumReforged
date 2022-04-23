package gribland.gribcore.mixin.lithium.world.chunk_ticking;

import gribland.gribcore.lithium.common.world.PlayerChunkWatchingManagerIterable;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.server.level.PlayerMap;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerMap.class)
public class PlayerChunkWatchingManagerMixin implements PlayerChunkWatchingManagerIterable {
    @Shadow
    @Final
    private Object2BooleanMap<ServerPlayer> players;

    @Override
    public Iterable<ServerPlayer> getPlayers() {
        return this.players.keySet();
    }
}
