package me.jb.flytime.api.title;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MessageTitle {
    String getId();

    void sendAdd(@NotNull Player player, Integer flyTime);

    void sendCountdown(@NotNull Player player);
}
