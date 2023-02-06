package me.jb.flytime.plugin.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.plugin.worldguard.event.RegionsEnteredEvent;
import me.jb.flytime.plugin.playerdata.DefaultPlayerData;
import me.jb.flytime.plugin.worldguard.FlagManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

@Singleton
public class WGListener implements Listener {

  private final FlagManager flagManager;
  private final FileHandler fileHandler;
  private final PlayerDataModel playerDataModel;

  @Inject
  public WGListener(
      @NotNull FlagManager flagManager,
      @NotNull FileHandler fileHandler,
      @NotNull PlayerDataModel playerDataModel) {
    this.flagManager = flagManager;
    this.fileHandler = fileHandler;
    this.playerDataModel = playerDataModel;
  }

  @EventHandler
  public void onRegionEnter(RegionsEnteredEvent event) {

    Player player = event.getPlayer();

    if (event.getPlayer() == null) return;

    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

    if (player.hasPermission("flytime.claimbypass")) return;

    this.checkWGFlag(event, player, localPlayer);

    this.checkSSPerm(event, player, localPlayer);
  }

  private void checkWGFlag(RegionsEnteredEvent event, Player player, LocalPlayer localPlayer) {
    Location loc = localPlayer.getBlockLocation();
    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    RegionQuery query = container.createQuery();

    if (!query.testState(loc, localPlayer, this.flagManager.getFlyFlag())) {
      disableFly(event, player, localPlayer);
    }
  }

  private void checkSSPerm(RegionsEnteredEvent event, Player player, LocalPlayer localPlayer) {
    Island island = SuperiorSkyblockAPI.getGrid().getIslandAt(player.getLocation());

    if (island == null) return;

    if (!island.hasPermission(player, IslandPrivilege.getByName("Fly"))) {
      disableFly(event, player, localPlayer);
    }
  }

  private void disableFly(RegionsEnteredEvent event, Player player, LocalPlayer localPlayer) {
    Optional<PlayerData> playerDataOptional =
        this.playerDataModel.getPlayerData(localPlayer.getName());

    FileConfiguration mainConfig = this.fileHandler.getMainConfig().getConfig();

    if (!playerDataOptional.isPresent() || !playerDataOptional.get().canFly()) return;

    PlayerData playerData = playerDataOptional.orElseGet(() -> new DefaultPlayerData(player));

    if (event.getPlayer().getAllowFlight()) playerData.disableFly(mainConfig, false);
  }
}
