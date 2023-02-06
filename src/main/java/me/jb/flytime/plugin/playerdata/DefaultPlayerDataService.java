package me.jb.flytime.plugin.playerdata;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.exception.PlayerDataException;
import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataDAO;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.api.playerdata.PlayerDataService;
import me.jb.flytime.api.utils.async.AsyncRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefaultPlayerDataService implements PlayerDataService {

  private final JavaPlugin javaPlugin;
  private final PlayerDataDAO playerDataDAO;
  private final PlayerDataModel playerDataModel;

  @Inject
  public DefaultPlayerDataService(
      @NotNull JavaPlugin javaPlugin,
      @NotNull PlayerDataDAO playerDataDAO,
      @NotNull PlayerDataModel playerDataModel) {
    this.javaPlugin = javaPlugin;
    this.playerDataDAO = playerDataDAO;
    this.playerDataModel = playerDataModel;
  }

  @Override
  public void loadData() {
    new AsyncRequest(this.javaPlugin)
        .executeTask(
            () -> {
              try {
                this.playerDataDAO.getPersistantData().forEach(this.playerDataModel::addPlayerData);
              } catch (PlayerDataException e) {
                e.printStackTrace();
              }
            });
  }

  @Override
  public void saveData() {
    new AsyncRequest(this.javaPlugin)
        .executeTask(
            () ->
                this.playerDataModel
                    .getAllData()
                    .forEach(
                        playerData -> {
                          try {
                            if (playerData.getRemainingFlyTime() == 0
                                && !(playerData.hasUnlimitedFly() || playerData.hasBypass())) {
                              this.playerDataDAO.removePlayerData(playerData);
                              this.playerDataModel.removePlayerData(playerData.getPlayerName());
                            } else {
                              if (this.playerDataDAO.containsPlayer(playerData.getPlayerName()))
                                this.playerDataDAO.updatePlayerData(playerData);
                              else this.playerDataDAO.createPlayerData(playerData);
                            }
                          } catch (PlayerDataException e) {
                            e.printStackTrace();
                          }
                        }));
  }

  @Override
  public void updateNickname(@NotNull Player player) {
    new AsyncRequest(this.javaPlugin)
        .executeTask(
            () -> {
              Optional<PlayerData> playerDataOptional =
                  this.playerDataModel.getPlayerData(player.getUniqueId());
              if (!playerDataOptional.isPresent())
                throw new IllegalArgumentException(
                    "No player data for player " + player.getUniqueId());
              PlayerData playerData = playerDataOptional.get();

              playerData.changePlayerName(player.getName());
              try {
                this.playerDataDAO.updatePlayerData(playerData);
              } catch (PlayerDataException e) {
                e.printStackTrace();
              }
            });
  }

  @Override
  public void reload() {
    this.playerDataModel.clearCache();
    this.loadData();
    Bukkit.getOnlinePlayers()
        .forEach(
            player -> {
              if (!this.playerDataModel.containsPlayerData(player.getName())) {
                this.playerDataModel.addPlayerData(new DefaultPlayerData(player));
              }
            });
  }
}
