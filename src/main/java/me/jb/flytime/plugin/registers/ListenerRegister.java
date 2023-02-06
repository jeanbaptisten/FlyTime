package me.jb.flytime.plugin.registers;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.nofall.NoFallManager;
import me.jb.flytime.plugin.listeners.PlayerListener;
import me.jb.flytime.plugin.listeners.WGListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ListenerRegister {

  private final JavaPlugin plugin;
  private final PluginManager pluginManager;
  private final NoFallManager noFallManager;
  private final PlayerListener playerListener;
  private final WGListener wgListener;

  @Inject
  public ListenerRegister(
      @NotNull JavaPlugin plugin,
      @NotNull PluginManager pluginManager,
      @NotNull NoFallManager noFallManager,
      @NotNull PlayerListener playerListener,
      @NotNull WGListener wgListener) {
    this.plugin = plugin;
    this.pluginManager = pluginManager;
    this.noFallManager = noFallManager;
    this.playerListener = playerListener;
    this.wgListener = wgListener;
  }

  public void registerListeners() {
    this.pluginManager.registerEvents(this.playerListener, this.plugin);
    this.pluginManager.registerEvents(this.wgListener, this.plugin);
    this.pluginManager.registerEvents(this.noFallManager, this.plugin);
  }
}
