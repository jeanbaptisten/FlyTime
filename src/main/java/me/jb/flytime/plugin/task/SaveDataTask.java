package me.jb.flytime.plugin.task;

import me.jb.flytime.api.playerdata.PlayerDataService;
import org.jetbrains.annotations.NotNull;

public class SaveDataTask implements Runnable {

  private final PlayerDataService playerDataService;

  public SaveDataTask(@NotNull PlayerDataService playerDataService) {
    this.playerDataService = playerDataService;
  }

  @Override
  public void run() {
    this.playerDataService.saveData();
  }
}
