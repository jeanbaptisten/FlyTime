package me.jb.flytime.plugin;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageKeys;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.PluginManager;
import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.task.TaskManager;
import me.jb.flytime.api.title.MessageTitleManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefaultPluginManager implements PluginManager {

  private final FileHandler fileHandler;
  private final BukkitCommandManager commandManager;
  private final MessageTitleManager messageTitleManager;
  private final TaskManager taskManager;

  @Inject
  public DefaultPluginManager(
      @NotNull FileHandler fileHandler,
      @NotNull BukkitCommandManager commandManager,
      @NotNull MessageTitleManager messageTitleManager,
      @NotNull TaskManager taskManager) {
    this.fileHandler = fileHandler;
    this.commandManager = commandManager;
    this.messageTitleManager = messageTitleManager;
    this.taskManager = taskManager;
  }

  @Override
  public void reload() {
    this.fileHandler.loadFiles();
    this.messageTitleManager.reload();
    this.taskManager.reloadTasks();
    this.loadCommandMessages(this.fileHandler.getMainConfig().getConfig());
  }

  private void loadCommandMessages(FileConfiguration messageConfig) {
    ConfigurationSection configurationSection =
        messageConfig.getConfigurationSection("globalCommands");
    configurationSection
        .getKeys(false)
        .forEach(
            messageKey -> {
              this.commandManager
                  .getLocales()
                  .addMessage(
                      Locale.ENGLISH,
                      MessageKeys.valueOf(messageKey),
                      configurationSection.getString(messageKey));
            });
  }
}
