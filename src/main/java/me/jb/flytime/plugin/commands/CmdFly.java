package me.jb.flytime.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.api.utils.MessageUtils;
import me.jb.flytime.plugin.enums.Message;
import me.jb.flytime.plugin.playerdata.DefaultPlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
@CommandAlias("fly")
public class CmdFly extends BaseCommand {

  private final FileHandler fileHandler;
  private final PlayerDataModel playerDataModel;

  @Inject
  public CmdFly(FileHandler fileHandler, @NotNull PlayerDataModel playerDataModel) {
    this.fileHandler = fileHandler;
    this.playerDataModel = playerDataModel;
  }

  @Default
  @Description("Activation/Désactivation du fly.")
  public void onFly(@NotNull Player player) {

    FileConfiguration mainConfig = this.fileHandler.getMainConfig().getConfig();

    Optional<PlayerData> playerDataOptional = this.playerDataModel.getPlayerData(player.getName());

    if ((!playerDataOptional.isPresent() || !playerDataOptional.get().canFly())
        && !player.hasPermission("flytime.bypass")) {

      String noFlyTime = mainConfig.getString(Message.NOT_ENOUGH_FLY_TIME.getKey());
      player.sendMessage(MessageUtils.setColorsMessage(noFlyTime));
      return;
    }

    PlayerData playerData = playerDataOptional.orElseGet(() -> new DefaultPlayerData(player));

    if (player.getAllowFlight()) playerData.disableFly(mainConfig, false);
    else playerData.activateFly(mainConfig, false);
  }
}
