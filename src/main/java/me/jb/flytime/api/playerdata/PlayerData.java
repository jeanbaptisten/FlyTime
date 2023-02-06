package me.jb.flytime.api.playerdata;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.UUID;

public interface PlayerData {
    @NotNull UUID getPlayerUUID();

    @NotNull String getPlayerName();

    boolean hasUnlimitedFly();

    boolean canFly();

    void activateFly(FileConfiguration fileConfiguration, boolean forced);

    void disableFly(FileConfiguration fileConfiguration, boolean forced);

    void changePlayerName(@NotNull String playerName);

    void setUnlimitedFlyState(boolean unlimitedFlyState);

    @Range(from = 0, to = Integer.MAX_VALUE) int getRemainingFlyTime();

    void addFlyTime(@Range(from = 0, to = Integer.MAX_VALUE) int flyTime);

    void removeFlyTime(@Range(from = 0, to = Integer.MAX_VALUE) int flyTime);

    boolean hasBypass();

    Player getPlayer();

}
