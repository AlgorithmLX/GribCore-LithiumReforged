package gribland.gribcore.lithium.common.world;

import net.minecraft.server.level.ServerPlayer;

public interface PlayerChunkWatchingManagerIterable {
    Iterable<ServerPlayer> getPlayers();
}
