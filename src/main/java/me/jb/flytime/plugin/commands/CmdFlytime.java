package me.jb.flytime.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.PluginManager;
import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.api.title.MessageTitleModel;
import me.jb.flytime.api.utils.IntegerUtils;
import me.jb.flytime.api.utils.MessageUtils;
import me.jb.flytime.plugin.enums.Message;
import me.jb.flytime.plugin.playerdata.DefaultPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
@CommandAlias("flytime|ft")
@CommandPermission("flytime.admin")
public class CmdFlytime extends BaseCommand {

  private final FileHandler fileHandler;
  private final MessageTitleModel messageTitleModel;
  private final PluginManager pluginManager;
  private final PlayerDataModel playerDataModel;

  @Inject
  public CmdFlytime(
      @NotNull FileHandler fileHandler,
      @NotNull PlayerDataModel playerDataModel,
      @NotNull MessageTitleModel messageTitleModel,
      @NotNull PluginManager pluginManager) {
    this.fileHandler = fileHandler;
    this.playerDataModel = playerDataModel;
    this.messageTitleModel = messageTitleModel;
    this.pluginManager = pluginManager;
  }

  @Default
  @Subcommand("help")
  @Description("Mauvaise commande.")
  public void onHelp(@NotNull CommandSender commandSender) {
    FileConfiguration messageConfig = this.fileHandler.getMainConfig().getConfig();
    String helpMess = messageConfig.getString(Message.HELP.getKey());
    commandSender.sendMessage(MessageUtils.setColorsMessage(helpMess));
  }

  @Subcommand("add")
  @Syntax("<playerName> <flyTime>")
  @CommandCompletion("@players @range:100")
  @Description("Ajoute du temps de fly à un joueur.")
  public void onFlyAdd(
      @NotNull CommandSender commandSender,
      @NotNull OfflinePlayer offlinePlayer,
      Integer flyTimee,
      String[] args) {

    // Check length
    FileConfiguration messageConfig = this.fileHandler.getMainConfig().getConfig();

    if (args.length > 3) {
      String helpMess = messageConfig.getString(Message.HELP.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(helpMess));
      return;
    }

    // Check if player is cached
    Optional<PlayerData> playerDataOptional =
        this.playerDataModel.getPlayerData(offlinePlayer.getName());

    int flyTime = flyTimee * 60;

    if (!playerDataOptional.isPresent()) {
      // If not, cache him with fly time
      PlayerData newPlayerData = new DefaultPlayerData(offlinePlayer, flyTime);
      this.playerDataModel.addPlayerData(newPlayerData);
      newPlayerData.activateFly(messageConfig, true);

      // else give it fly time
    } else {
      PlayerData playerData = playerDataOptional.get();
      // If he has unlimited fly, don't give to him.
      if (playerData.hasUnlimitedFly() || playerData.hasBypass()) {
        String gotFlyTimeMess =
            messageConfig
                .getString(Message.CANNOT_ADD_FLY_TIME.getKey())
                .replace("%player_name%", offlinePlayer.getName());
        commandSender.sendMessage(MessageUtils.setColorsMessage(gotFlyTimeMess));
        return;
      }

      playerData.addFlyTime(flyTime);
      playerData.activateFly(messageConfig, true);
    }

    String gaveFlyTimeMess =
        messageConfig
            .getString(Message.FLY_GIVEN.getKey())
            .replace("%fly_time_add%", String.valueOf(flyTime))
            .replace("%player_name%", offlinePlayer.getName());
    commandSender.sendMessage(MessageUtils.setColorsMessage(gaveFlyTimeMess));

    // Check if player is connected, then sendAdd messages.
    Player selectedPlayer = Bukkit.getPlayerExact(offlinePlayer.getName());
    if (selectedPlayer != null) {
      this.messageTitleModel.getMessageTitle("flyObtained").sendAdd(selectedPlayer, flyTime);
      String gotFlyTimeMess =
          messageConfig
              .getString(Message.FLY_OBTAINED.getKey())
              .replace("%fly_time_add%", IntegerUtils.parseTime(flyTime));
      selectedPlayer.sendMessage(MessageUtils.setColorsMessage(gotFlyTimeMess));
    }
  }

  @Subcommand("give")
  @Syntax("<playerName>")
  @CommandCompletion("@players")
  @Description("Donne le fly infini à un joueur.")
  public void onFlyGive(
      @NotNull CommandSender commandSender, @NotNull OfflinePlayer offlinePlayer, String[] args) {
    // Check length
    FileConfiguration messageConfig = this.fileHandler.getMainConfig().getConfig();

    if (args.length > 2) {
      String helpMess = messageConfig.getString(Message.HELP.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(helpMess));
      return;
    }

    // Check if player is cached
    Optional<PlayerData> playerDataOptional =
        this.playerDataModel.getPlayerData(offlinePlayer.getName());

    if (!playerDataOptional.isPresent()) {
      // If not, cache him with fly time
      PlayerData playerData = new DefaultPlayerData(offlinePlayer);
      playerData.setUnlimitedFlyState(true);
      playerData.activateFly(messageConfig, true);

      this.playerDataModel.addPlayerData(playerData);
      // else give it fly
    } else {
      PlayerData playerData = playerDataOptional.get();

      // If he has unlimited fly, return
      if (playerData.hasUnlimitedFly() || playerData.hasBypass()) {
        String gotFlyTimeMess =
            messageConfig
                .getString(Message.ALREADY_UNLIMITED_FLY_TIME.getKey())
                .replace("%player_name%", offlinePlayer.getName());
        commandSender.sendMessage(MessageUtils.setColorsMessage(gotFlyTimeMess));
        return;
      }
      // Set unlimited fly then sendAdd messages.
      playerData.setUnlimitedFlyState(true);
      playerData.activateFly(messageConfig, true);
    }

    String giveFlyTimeMess =
        messageConfig
            .getString(Message.UNLIMITED_FLY_TIME_GIVE.getKey())
            .replace("%player_name%", offlinePlayer.getName());
    commandSender.sendMessage(MessageUtils.setColorsMessage(giveFlyTimeMess));

    Player selectedPlayer = Bukkit.getPlayerExact(offlinePlayer.getName());
    if (selectedPlayer != null) {
      String gotFlyTimeMess = messageConfig.getString(Message.UNLIMITED_FLY_TIME_GOT.getKey());
      selectedPlayer.sendMessage(MessageUtils.setColorsMessage(gotFlyTimeMess));
    }
  }

  @Subcommand("remove")
  @Syntax("<playerName> <flyTime>")
  @CommandCompletion("@players @range:100")
  @Description("Enlève du temps de fly à un joueur.")
  public void onFlyRemove(
      @NotNull CommandSender commandSender,
      @NotNull OfflinePlayer offlinePlayer,
      Integer flyTimee,
      String[] args) {

    // Check length
    FileConfiguration messageConfig = this.fileHandler.getMainConfig().getConfig();

    if (args.length > 3) {
      String helpMess = messageConfig.getString(Message.HELP.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(helpMess));
      return;
    }

    // Check if player is cached
    Optional<PlayerData> playerDataOptional =
        this.playerDataModel.getPlayerData(offlinePlayer.getName());

    int flyTime = flyTimee * 60;
    // If not return
    if (!playerDataOptional.isPresent() || playerDataOptional.get().getRemainingFlyTime() == 0) {
      String cannotRemoveFlyTimeMess =
          messageConfig
              .getString(Message.CANNOT_REMOVE_FLY_TIME.getKey())
              .replace("%player_name%", offlinePlayer.getName());
      commandSender.sendMessage(MessageUtils.setColorsMessage(cannotRemoveFlyTimeMess));
      return;
      // else give it fly time
    }

    PlayerData playerData = playerDataOptional.get();
    // If he has unlimited fly, don't give to him.
    if (playerData.hasUnlimitedFly() || playerData.hasBypass()) {
      String gotFlyTimeMess =
          messageConfig
              .getString(Message.CANNOT_REMOVE_FLY_TIME.getKey())
              .replace("%player_name%", offlinePlayer.getName());
      commandSender.sendMessage(MessageUtils.setColorsMessage(gotFlyTimeMess));
    }

    if (playerData.getRemainingFlyTime() > flyTime)
      this.removeFlyTime(commandSender, offlinePlayer, messageConfig, playerData, flyTime);
    else {
      this.removeFlyTime(
          commandSender,
          offlinePlayer,
          messageConfig,
          playerData,
          playerData.getRemainingFlyTime());
      playerData.disableFly(messageConfig, true);
    }
  }

  @Subcommand("take")
  @Syntax("<playerName>")
  @CommandCompletion("@players")
  @Description("Retire le fly infini à un joueur.")
  public void onFlyTake(
      @NotNull CommandSender commandSender, @NotNull OfflinePlayer offlinePlayer, String[] args) {
    // Check length
    FileConfiguration messageConfig = this.fileHandler.getMainConfig().getConfig();

    if (args.length > 2) {
      String helpMess = messageConfig.getString(Message.HELP.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(helpMess));
      return;
    }

    // Check if player is cached
    Optional<PlayerData> playerDataOptional =
        this.playerDataModel.getPlayerData(offlinePlayer.getName());

    if (!playerDataOptional.isPresent() || !playerDataOptional.get().hasUnlimitedFly()) {
      // If not, cache him with fly time
      String gotFlyTimeMess =
          messageConfig
              .getString(Message.DOESNT_UNLIMITED_FLY_TIME.getKey())
              .replace("%player_name%", offlinePlayer.getName());
      commandSender.sendMessage(MessageUtils.setColorsMessage(gotFlyTimeMess));
      return;
      // else give it fly
    } else {
      PlayerData playerData = playerDataOptional.get();

      if (playerData.hasBypass()) {
        String gotFlyTimeMess =
            messageConfig
                .getString(Message.PLAYER_HAVE_BYPASS.getKey())
                .replace("%player_name%", offlinePlayer.getName());
        commandSender.sendMessage(MessageUtils.setColorsMessage(gotFlyTimeMess));
        return;
      }

      // Remove unlimited fly then sendAdd messages.
      playerData.setUnlimitedFlyState(false);

      if (playerData.getRemainingFlyTime() == 0) playerData.disableFly(messageConfig, true);
    }

    String giveFlyTimeMess =
        messageConfig
            .getString(Message.UNLIMITED_FLY_TIME_TAKEN.getKey())
            .replace("%player_name%", offlinePlayer.getName());
    commandSender.sendMessage(MessageUtils.setColorsMessage(giveFlyTimeMess));

    Player selectedPlayer = Bukkit.getPlayerExact(offlinePlayer.getName());
    if (selectedPlayer != null) {
      String gotFlyTimeMess = messageConfig.getString(Message.UNLIMITED_FLY_TIME_LOST.getKey());
      selectedPlayer.sendMessage(MessageUtils.setColorsMessage(gotFlyTimeMess));
    }
  }

  @Subcommand("reload")
  @Description("Reload le plugin.")
  public void onReload(@NotNull CommandSender commandSender, String[] args) {
    FileConfiguration messageConfig = this.fileHandler.getMainConfig().getConfig();

    if (args.length > 1) {
      String helpMess = messageConfig.getString(Message.HELP.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(helpMess));
      return;
    }

    this.pluginManager.reload();

    String reloadMess = messageConfig.getString(Message.RELOAD.getKey());
    commandSender.sendMessage(MessageUtils.setColorsMessage(reloadMess));
  }

  private void removeFlyTime(
      CommandSender commandSender,
      OfflinePlayer offlinePlayer,
      FileConfiguration messageConfig,
      PlayerData playerData,
      Integer flyTime) {
    playerData.removeFlyTime(flyTime);

    String removeFlyTimeSenderMess =
        messageConfig
            .getString(Message.FLY_REMOVED_SENDER.getKey())
            .replace("%player_name%", offlinePlayer.getName())
            .replace("%fly_time_remove%", IntegerUtils.parseTime(flyTime));
    commandSender.sendMessage(MessageUtils.setColorsMessage(removeFlyTimeSenderMess));

    Player selectedPlayer = Bukkit.getPlayerExact(offlinePlayer.getName());
    if (selectedPlayer != null) {
      String removedFlyTimeMess =
          messageConfig
              .getString(Message.FLY_REMOVED.getKey())
              .replace("%fly_time_remove%", String.valueOf(flyTime));
      selectedPlayer.sendMessage(MessageUtils.setColorsMessage(removedFlyTimeMess));
    }
  }
}
