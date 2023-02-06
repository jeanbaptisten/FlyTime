package me.jb.flytime.plugin.title;

import javax.inject.Inject;

import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.title.MessageTitle;
import me.jb.flytime.api.title.MessageTitleDAO;
import me.jb.flytime.api.utils.MessageUtils;
import org.bukkit.configuration.ConfigurationSection;

public class YamlMessageTitleDAO implements MessageTitleDAO {

  private final FileHandler fileHandler;

  @Inject
  public YamlMessageTitleDAO(FileHandler fileHandler) {
    this.fileHandler = fileHandler;
  }

  @Override
  public MessageTitle getTitle(String id) {
    ConfigurationSection configurationSection =
        this.fileHandler.getMainConfig().getConfig().getConfigurationSection("title." + id);

    String title = MessageUtils.setColorsMessage(configurationSection.getString("title"));
    String subtitle = MessageUtils.setColorsMessage(configurationSection.getString("subtitle"));
    int fadeIn = configurationSection.getInt("fadeIn");
    int stay = configurationSection.getInt("stay");
    int fadeOut = configurationSection.getInt("fadeOut");

    return new DefaultMessageTitle(id, null, title, subtitle, fadeIn, stay, fadeOut, true, false);
  }

  @Override
  public MessageTitle getCountdown(String id) {
    ConfigurationSection configurationSection =
        this.fileHandler.getMainConfig().getConfig().getConfigurationSection("countdown." + id);

    String title = MessageUtils.setColorsMessage(configurationSection.getString("title.title"));
    String subtitle =
        MessageUtils.setColorsMessage(configurationSection.getString("title.subtitle"));
    int fadeIn = configurationSection.getInt("title.fadeIn");
    int stay = configurationSection.getInt("title.stay");
    int fadeOut = configurationSection.getInt("title.fadeOut");
    boolean activeTitle = configurationSection.getBoolean("title.active");

    String message = MessageUtils.setColorsMessage(configurationSection.getString("message.text"));
    boolean activeMessage = configurationSection.getBoolean("message.active");

    return new DefaultMessageTitle(
        id, message, title, subtitle, fadeIn, stay, fadeOut, activeTitle, activeMessage);
  }
}
