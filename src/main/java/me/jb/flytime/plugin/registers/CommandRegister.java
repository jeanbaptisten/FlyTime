package me.jb.flytime.plugin.registers;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageKeys;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.plugin.commands.CmdFly;
import me.jb.flytime.plugin.commands.CmdFlytime;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CommandRegister {

  private final BukkitCommandManager commandManager;
  private final CmdFlytime cmdFlytime;
  private final CmdFly cmdFly;

  @Inject
  public CommandRegister(
      @NotNull BukkitCommandManager commandManager,
      @NotNull CmdFlytime cmdFlytime,
      @NotNull CmdFly cmdFly) {
    this.commandManager = commandManager;
    this.cmdFlytime = cmdFlytime;
    this.cmdFly = cmdFly;
  }

  public void registerCommands() {
    this.commandManager.registerCommand(this.cmdFlytime);
    this.commandManager.registerCommand(this.cmdFly);
  }

  public void loadCommandMessages(FileConfiguration messageConfig) {
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
