package me.jb.flytime.api.nofall;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

public interface NoFallManager extends Listener {
    void load();

    void removePlayer(UUID uuid);
}
