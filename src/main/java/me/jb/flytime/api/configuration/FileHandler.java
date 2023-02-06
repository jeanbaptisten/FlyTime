package me.jb.flytime.api.configuration;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;
import java.io.File;

@Singleton
public class FileHandler {

  private CustomConfig mainConfig;

  private final Plugin plugin;

  public FileHandler(@NotNull Plugin plugin) {
    this.plugin = plugin;

    this.loadFiles();
  }

  @NotNull
  public CustomConfig getMainConfig() {
    return this.mainConfig;
  }


  public void loadFiles() {
    this.setupMainConfigFile();
  }

  private void setupMainConfigFile() {
    File messageConfigFile = FileEngine.fileCreator(this.plugin, "config.yml");
    this.mainConfig = new CustomConfig(messageConfigFile);
  }
}
