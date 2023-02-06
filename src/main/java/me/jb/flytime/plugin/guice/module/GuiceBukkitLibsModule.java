package me.jb.flytime.plugin.guice.module;

import co.aikar.commands.BukkitCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import me.clip.placeholderapi.libs.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

public class GuiceBukkitLibsModule extends AbstractModule {

  private final Plugin plugin;

  public GuiceBukkitLibsModule(Plugin plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  public BukkitCommandManager provideAcfCommandManager() {
    return new BukkitCommandManager(this.plugin);
  }

  @Provides
  @Singleton
  public BukkitAudiences provideBukkitAudiences() {
    return BukkitAudiences.create(this.plugin);
  }
}
