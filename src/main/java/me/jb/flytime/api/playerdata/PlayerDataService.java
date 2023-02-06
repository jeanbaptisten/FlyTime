package me.jb.flytime.api.playerdata;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerDataService {

    void loadData();

    void saveData();

    void updateNickname(@NotNull Player player);

    void reload();
}
