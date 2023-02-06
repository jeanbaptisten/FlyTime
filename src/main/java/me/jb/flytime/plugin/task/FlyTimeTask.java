package me.jb.flytime.plugin.task;

import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.api.title.MessageTitleModel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FlyTimeTask implements Runnable {

  private final JavaPlugin javaPlugin;
  private final FileHandler fileHandler;
  private final PlayerDataModel playerDataModel;
  private final MessageTitleModel messageTitleModel;

  public FlyTimeTask(
      @NotNull JavaPlugin javaPlugin,
      @NotNull FileHandler fileHandler,
      @NotNull PlayerDataModel playerDataModel,
      @NotNull MessageTitleModel messageTitleModel) {
    this.javaPlugin = javaPlugin;
    this.fileHandler = fileHandler;
    this.playerDataModel = playerDataModel;
    this.messageTitleModel = messageTitleModel;
  }

  @Override
  public void run() {
    FileConfiguration mainConfig = this.fileHandler.getMainConfig().getConfig();

    this.playerDataModel
        .getAllData()
        .forEach(
            playerData -> {
              if (playerData.getRemainingFlyTime() != 0) {
                PlayerData playerDataInModel =
                    this.playerDataModel.getPlayerData(playerData.getPlayerUUID()).get();
                playerDataInModel.removeFlyTime(1);

                if (playerData.getPlayer() == null) return;

                int remainingFlyTime = playerData.getRemainingFlyTime() - 1;

                if (this.messageTitleModel.containsId(String.valueOf(remainingFlyTime)))
                  this.messageTitleModel
                      .getMessageTitle(String.valueOf(remainingFlyTime))
                      .sendCountdown(playerData.getPlayer());

                if (remainingFlyTime == 0) {
                  if (playerData.hasUnlimitedFly() || playerData.hasBypass()) return;
                  this.runSync(() -> playerData.disableFly(mainConfig, true));
                }
              }
            });
  }

  private void runSync(@NotNull Runnable runnable) {
    Bukkit.getScheduler().runTask(this.javaPlugin, runnable);
  }
}
