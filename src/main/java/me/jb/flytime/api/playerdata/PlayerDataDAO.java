package me.jb.flytime.api.playerdata;

import me.jb.flytime.api.exception.PlayerDataException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PlayerDataDAO {
    List<PlayerData> getPersistantData() throws PlayerDataException;

    void createPlayerData(PlayerData playerData) throws PlayerDataException;

    void removePlayerData(@NotNull PlayerData playerData) throws PlayerDataException;

    void updatePlayerData(@NotNull PlayerData playerData) throws PlayerDataException;

    void updateNickname(Player player) throws PlayerDataException;

    boolean containsPlayer(String playerName) throws PlayerDataException;
}
