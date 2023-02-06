package me.jb.flytime.plugin.playerdata;

import java.util.UUID;

import me.jb.flytime.api.customevent.FlyDisabledEvent;
import me.jb.flytime.api.customevent.FlyEnabledEvent;
import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.utils.MessageUtils;
import me.jb.flytime.plugin.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class DefaultPlayerData implements PlayerData {

  private final UUID playerUUID;
  private String playerName;
  private boolean unlimitedFlyState;
  private int remainingFlyTime; // seconds

  public DefaultPlayerData(
      @NotNull UUID playerUUID,
      @NotNull String playerName,
      boolean unlimitedFlyState,
      @Range(from = 0, to = Integer.MAX_VALUE) int remainingFlyTime) {
    this.playerUUID = playerUUID;
    this.playerName = playerName;
    this.unlimitedFlyState = unlimitedFlyState;
    this.remainingFlyTime = remainingFlyTime;
  }

  public DefaultPlayerData(PlayerData playerData) {
    this(
        playerData.getPlayerUUID(),
        playerData.getPlayerName(),
        playerData.hasUnlimitedFly(),
        playerData.getRemainingFlyTime());
  }

  public DefaultPlayerData(
      OfflinePlayer player,
      boolean unlimitedFlyState,
      @Range(from = 0, to = Integer.MAX_VALUE) int remainingFlyTime) {
    this(player.getUniqueId(), player.getName(), unlimitedFlyState, remainingFlyTime);
  }

  public DefaultPlayerData(
      OfflinePlayer player, @Range(from = 0, to = Integer.MAX_VALUE) int remainingFlyTime) {
    this(player.getUniqueId(), player.getName(), false, remainingFlyTime);
  }

  public DefaultPlayerData(@NotNull OfflinePlayer player) {
    this(player, false, 0);
  }

  @Override
  @NotNull
  public UUID getPlayerUUID() {
    return this.playerUUID;
  }

  @Override
  @NotNull
  public String getPlayerName() {
    return this.playerName;
  }

  @Override
  public boolean hasUnlimitedFly() {
    return this.unlimitedFlyState;
  }

  @Override
  public boolean canFly() {
    return this.unlimitedFlyState || this.hasBypass() || (this.remainingFlyTime != 0);
  }

  @Override
  public void activateFly(FileConfiguration fileConfiguration, boolean forced) {

    Player player = this.getPlayer();
    if (player == null) return;
    if (player.getAllowFlight()) return;

    FlyEnabledEvent flyDisabledEvent = new FlyEnabledEvent(player, this.remainingFlyTime, forced);
    Bukkit.getPluginManager().callEvent(flyDisabledEvent);

    if (flyDisabledEvent.isCancelled()) return;

    player.setAllowFlight(true);

    String flyEnabled;
    if (forced) flyEnabled = fileConfiguration.getString(Message.FLY_ENABLED.getKey());
    else flyEnabled = fileConfiguration.getString(Message.FLY_ON.getKey());
    player.sendMessage(MessageUtils.setColorsMessage(flyEnabled));
  }

  @Override
  public void disableFly(FileConfiguration fileConfiguration, boolean forced) {
    Player player = this.getPlayer();

    if (player == null) return;
    if (!player.getAllowFlight()) return;

    FlyDisabledEvent flyDisabledEvent = new FlyDisabledEvent(player, this.remainingFlyTime, forced);
    Bukkit.getPluginManager().callEvent(flyDisabledEvent);

    if (flyDisabledEvent.isCancelled()) return;

    player.setAllowFlight(false);

    String flyDisabled;
    if (forced) flyDisabled = fileConfiguration.getString(Message.FLY_DISABLED.getKey());
    else flyDisabled = fileConfiguration.getString(Message.FLY_OFF.getKey());
    player.sendMessage(MessageUtils.setColorsMessage(flyDisabled));
  }

  @Override
  public void changePlayerName(@NotNull String playerName) {
    this.playerName = playerName;
  }

  @Override
  public void setUnlimitedFlyState(boolean unlimitedFlyState) {
    this.unlimitedFlyState = unlimitedFlyState;
  }

  @Override
  @Range(from = 0, to = Integer.MAX_VALUE)
  public synchronized int getRemainingFlyTime() {
    return this.remainingFlyTime;
  }

  @Override
  public synchronized void addFlyTime(@Range(from = 0, to = Integer.MAX_VALUE) int flyTime) {
    this.remainingFlyTime += flyTime;
  }

  @Override
  public void removeFlyTime(@Range(from = 0, to = Integer.MAX_VALUE) int flyTime) {
    if (flyTime >= this.remainingFlyTime) this.remainingFlyTime = 0;
    else this.remainingFlyTime -= flyTime;
  }

  @Override
  public boolean hasBypass() {
    Player player = this.getPlayer();
    if (player == null) return false;
    return player.hasPermission("flytime.bypass");
  }

  @Override
  public Player getPlayer() {
    return Bukkit.getPlayer(this.playerUUID);
  }

  @Override
  public String toString() {
    return "DefaultPlayerData{"
        + "playerUUID="
        + playerUUID
        + ", playerName='"
        + playerName
        + '\''
        + ", unlimitedFlyState="
        + unlimitedFlyState
        + ", remainingFlyTime="
        + remainingFlyTime
        + '}';
  }
}
