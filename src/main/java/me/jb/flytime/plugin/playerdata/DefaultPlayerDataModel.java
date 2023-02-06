package me.jb.flytime.plugin.playerdata;

import com.google.common.base.Preconditions;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefaultPlayerDataModel implements PlayerDataModel {

  private final Map<String, PlayerData> playerDataMap = new HashMap<>();

  @Inject
  public DefaultPlayerDataModel() {}

  @Override
  public void addPlayerData(@NotNull PlayerData playerData) {
    Preconditions.checkArgument(
        !this.playerDataMap.containsValue(playerData),
        "PlayerData map already contain data for player " + playerData.getPlayerUUID());
    this.playerDataMap.put(playerData.getPlayerName(), playerData);
  }

  @Override
  public void removePlayerData(@NotNull String playerName) {
    Preconditions.checkArgument(
        this.playerDataMap.containsKey(playerName),
        "PlayerData map doesn't contain data for player " + playerName);
    this.playerDataMap.remove(playerName);
  }

  @Override
  public boolean containsPlayerData(@NotNull String playerName) {
    return this.playerDataMap.containsKey(playerName);
  }

  @Override
  public boolean containsPlayerData(@NotNull PlayerData playerData) {
    return this.playerDataMap.containsValue(playerData);
  }

  @Override
  public void replacePlayerData(PlayerData playerData) {
    this.playerDataMap.put(playerData.getPlayerName(), playerData);
  }

  @Override
  public Optional<PlayerData> getPlayerData(@NotNull String playerName) {
    return Optional.ofNullable(this.playerDataMap.get(playerName));
  }

  @Override
  public Optional<PlayerData> getPlayerData(@NotNull UUID playerUUID) {
    return this.playerDataMap.values().stream()
        .filter(playerData -> playerData.getPlayerUUID().equals(playerUUID))
        .findAny();
  }

  @Override
  public List<PlayerData> getAllData() {
    return playerDataMap.values().stream().map(DefaultPlayerData::new).collect(Collectors.toList());
  }

  @Override
  public void clearCache() {
    this.playerDataMap.clear();
  }

  @Override
  public String toString() {
    return "DefaultPlayerDataModel{" + "playerDataMap=" + playerDataMap + '}';
  }
}
