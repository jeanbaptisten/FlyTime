package me.jb.flytime.api.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

  private final File configFile;
  private final FileConfiguration configuration;

  public CustomConfig(@NotNull File configFile) {
    this.configFile = configFile;
    this.configuration = FileEngine.fileConfigurationLoader(this.configFile);
  }

  @NotNull
  public FileConfiguration getConfig() {
    return this.configuration;
  }

  public void save() {
    try {
      this.configuration.save(this.configFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return "CustomConfig{" + "configFile=" + configFile + ", configuration=" + configuration + '}';
  }
}
