package me.jb.flytime.plugin.listeners;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.api.playerdata.PlayerDataService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerListener implements Listener {

  private final PlayerDataModel playerDataModel;
  private final PlayerDataService playerDataService;

  @Inject
  public PlayerListener(
      @NotNull PlayerDataService playerDataService, @NotNull PlayerDataModel playerDataModel) {
    this.playerDataService = playerDataService;
    this.playerDataModel = playerDataModel;
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    Optional<PlayerData> playerDataOptional = this.playerDataModel.getPlayerData(player.getName());
    if (playerDataOptional.isPresent()) {
      PlayerData playerData = playerDataOptional.get();
      if (!player.getName().equals(playerData.getPlayerName()))
        this.playerDataService.updateNickname(player);
    }
  }
}
