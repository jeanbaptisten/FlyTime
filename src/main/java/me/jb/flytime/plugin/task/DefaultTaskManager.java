package me.jb.flytime.plugin.task;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.api.playerdata.PlayerDataService;
import me.jb.flytime.api.task.TaskManager;
import me.jb.flytime.api.title.MessageTitleModel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefaultTaskManager implements TaskManager {

  private final JavaPlugin javaPlugin;
  private final FileHandler fileHandler;
  private final PlayerDataModel playerDataModel;
  private final PlayerDataService playerDataService;
  private final MessageTitleModel messageTitleModel;
  private BukkitTask saveDataTask, flyTimeTask;

  @Inject
  public DefaultTaskManager(
      @NotNull JavaPlugin javaPlugin,
      @NotNull FileHandler fileHandler,
      @NotNull PlayerDataModel playerDataModel,
      @NotNull PlayerDataService playerDataService,
      @NotNull MessageTitleModel messageTitleModel) {
    this.javaPlugin = javaPlugin;
    this.fileHandler = fileHandler;
    this.playerDataModel = playerDataModel;
    this.playerDataService = playerDataService;
    this.messageTitleModel = messageTitleModel;
  }

  @Override
  public void loadTasks() {
    this.flyTimeTask =
        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(
                this.javaPlugin,
                new FlyTimeTask(javaPlugin, fileHandler, this.playerDataModel, messageTitleModel),
                1,
                20L);

    FileConfiguration fileConfiguration = this.fileHandler.getMainConfig().getConfig();
    int period = fileConfiguration.getInt("saveDelay");
    this.saveDataTask =
        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(
                this.javaPlugin, new SaveDataTask(this.playerDataService), 1, period * 20L);
  }

  @Override
  public void reloadTasks() {
    // TODO : Reload les tasks
  }
}
