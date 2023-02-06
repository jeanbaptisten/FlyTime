package me.jb.flytime.plugin.nofall;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.customevent.FlyDisabledEvent;
import me.jb.flytime.api.nofall.NoFallManager;
import me.jb.flytime.plugin.task.NoFallTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefaultNoFallManager implements NoFallManager {

  private final Map<UUID, Player> playerNoFallMap = new HashMap<>();

  private final JavaPlugin javaPlugin;
  private final FileHandler fileHandler;
  private int delay;

  @Inject
  public DefaultNoFallManager(@NotNull JavaPlugin javaPlugin, @NotNull FileHandler fileHandler) {
    this.fileHandler = fileHandler;
    this.javaPlugin = javaPlugin;
    this.load();
  }

  @Override
  public void load() {
    this.delay = fileHandler.getMainConfig().getConfig().getInt("noFallTime");
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFlyDisabled(FlyDisabledEvent event) {
    if (event.isCancelled()) return;

    Player player = event.getPlayer();
    this.playerNoFallMap.put(player.getUniqueId(), player);
    Bukkit.getScheduler()
        .runTaskLater(this.javaPlugin, new NoFallTask(this, player.getUniqueId()), delay * 20L);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onFallDamage(EntityDamageEvent event) {

    if (!(event.getEntity() instanceof Player)) return;

    if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

    UUID playerUUID = event.getEntity().getUniqueId();

    if (!this.playerNoFallMap.containsKey(playerUUID)) return;

    this.playerNoFallMap.remove(playerUUID);
    event.setCancelled(true);
  }

  @Override
  public void removePlayer(UUID uuid) {
    this.playerNoFallMap.remove(uuid);
  }
}
