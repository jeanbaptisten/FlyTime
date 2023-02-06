package me.jb.flytime.plugin.placeholder;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.api.utils.IntegerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class FlyTimeExpansion extends PlaceholderExpansion {

  private final PlayerDataModel playerDataModel;
  private final FileHandler fileHandler;

  @Inject
  public FlyTimeExpansion(
      @NotNull FileHandler fileHandler, @NotNull PlayerDataModel playerDataModel) {
    this.fileHandler = fileHandler;
    this.playerDataModel = playerDataModel;
  }

  @Override
  public @NotNull String getIdentifier() {
    return "fly";
  }

  @Override
  public @NotNull String getAuthor() {
    return "jb";
  }

  @Override
  public @NotNull String getVersion() {
    return "1.0.0";
  }

  @Override
  public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
    Player onlinePlayer = Bukkit.getPlayer(offlinePlayer.getUniqueId());

    if (params.equals("statut") && onlinePlayer != null) {
      return String.valueOf(onlinePlayer.getAllowFlight());
    }

    if (params.startsWith("time_remaining")) {

      FileConfiguration mainConfig = this.fileHandler.getMainConfig().getConfig();
      String infiniteString = mainConfig.getString("placeholder.infinite");
      String[] paramsSplit = params.split("time_remaining");

      Optional<PlayerData> playerDataOptional =
          this.playerDataModel.getPlayerData(offlinePlayer.getUniqueId());

      if (paramsSplit.length == 0 && params.equals("time_remaining")) {
        if (onlinePlayer != null && onlinePlayer.hasPermission("flytime.bypass"))
          return infiniteString;

        return playerDataOptional
            .map(playerData -> IntegerUtils.parseTime(playerData.getRemainingFlyTime()))
            .orElse("✘");
      }

      if (paramsSplit.length == 2 && paramsSplit[1].equals("_nf")) {
        if (onlinePlayer != null && onlinePlayer.hasPermission("flytime.bypass"))
          return infiniteString;

        return playerDataOptional
            .map(playerData -> String.valueOf(playerData.getRemainingFlyTime()))
            .orElse("✘");
      }
    }

    return null;
  }
}
