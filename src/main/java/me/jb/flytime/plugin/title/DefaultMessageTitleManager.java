package me.jb.flytime.plugin.title;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.title.MessageTitleDAO;
import me.jb.flytime.api.title.MessageTitleManager;
import me.jb.flytime.api.title.MessageTitleModel;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefaultMessageTitleManager implements MessageTitleManager {

  private final MessageTitleModel messageTitleModel;
  private final MessageTitleDAO messageTitleDAO;
  private final FileHandler fileHandler;

  @Inject
  public DefaultMessageTitleManager(
      @NotNull MessageTitleModel messageTitleModel,
      @NotNull MessageTitleDAO messageTitleDAO,
      @NotNull FileHandler fileHandler) {
    this.messageTitleModel = messageTitleModel;
    this.messageTitleDAO = messageTitleDAO;
    this.fileHandler = fileHandler;
  }

  @Override
  public void loadTitles() {
    this.loadCountdowns();
    this.messageTitleModel.addMessageTitle(this.messageTitleDAO.getTitle("flyObtained"));
    this.messageTitleModel.addMessageTitle(this.messageTitleDAO.getTitle("reminderFly"));
  }

  @Override
  public void reload() {
    this.messageTitleModel.clearCache();
    this.loadTitles();
  }

  private void loadCountdowns() {
    FileConfiguration mainConfig = this.fileHandler.getMainConfig().getConfig();

    mainConfig
        .getConfigurationSection("countdown")
        .getKeys(false)
        .forEach(
            keyCountdown -> {
              this.messageTitleModel.addMessageTitle(
                  this.messageTitleDAO.getCountdown(keyCountdown));
            });
  }
}
