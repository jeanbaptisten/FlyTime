package me.jb.flytime.api.playerdata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface PlayerDataModel {

  void addPlayerData(@NotNull PlayerData playerData);

  void removePlayerData(@NotNull String playerName);

  boolean containsPlayerData(@NotNull String playerName);

  boolean containsPlayerData(@NotNull PlayerData playerData);

  void replacePlayerData(PlayerData playerData);

  Optional<PlayerData> getPlayerData(@NotNull String playerName);

  Optional<PlayerData> getPlayerData(@NotNull UUID playerUUID);

  List<PlayerData> getAllData();

  void clearCache();
}
