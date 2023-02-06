package me.jb.flytime.plugin.playerdata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.database.DbConnection;
import me.jb.flytime.api.exception.PlayerDataException;
import me.jb.flytime.api.playerdata.PlayerData;
import me.jb.flytime.api.playerdata.PlayerDataDAO;
import me.jb.flytime.plugin.playerdata.DefaultPlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SQLPlayerDataDAO implements PlayerDataDAO {

  private final DbConnection dbConnection;

  @Inject
  public SQLPlayerDataDAO(@NotNull DbConnection dbConnection) {
    this.dbConnection = dbConnection;

    this.initTable();
  }

  @Override
  public List<PlayerData> getPersistantData() throws PlayerDataException {
    String stringSQL = "SELECT * FROM player_fly_data;";
    Connection connection = dbConnection.getConnection();

    List<PlayerData> playerDataList = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(stringSQL)) {

      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        UUID playerUUID = UUID.fromString(resultSet.getString("player_uuid"));
        String playerName = resultSet.getString("player_name");
        boolean hasUnlimitedFly = resultSet.getBoolean("unlimited_fly_state");
        int remainingFlyTime = resultSet.getInt("remaining_fly_time");

        PlayerData playerData =
            new DefaultPlayerData(playerUUID, playerName, hasUnlimitedFly, remainingFlyTime);
        playerDataList.add(playerData);
      }

    } catch (SQLException troubles) {
      throw new PlayerDataException("Cannot load persistant data.", troubles);
    }

    return playerDataList;
  }

  @Override
  public void createPlayerData(PlayerData playerData) throws PlayerDataException {
    String stringSQL =
        "INSERT INTO player_fly_data"
            + " (player_uuid,player_name,unlimited_fly_state,remaining_fly_time)"
            + " VALUES  (?,?,?,?);";
    Connection connection = this.dbConnection.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(stringSQL)) {
      statement.setString(1, playerData.getPlayerUUID().toString());
      statement.setString(2, playerData.getPlayerName());
      statement.setBoolean(3, playerData.hasUnlimitedFly());
      statement.setInt(4, playerData.getRemainingFlyTime());

      statement.execute();
    } catch (SQLException troubles) {
      throw new PlayerDataException(
          String.format("Cannot create player data in database : %s", playerData.getPlayerUUID()),
          troubles);
    }
  }

  @Override
  public void removePlayerData(@NotNull PlayerData playerData) throws PlayerDataException {
    String stringSQL = "DELETE FROM player_fly_data WHERE player_uuid=?;";

    Connection connection = this.dbConnection.getConnection();

    try (PreparedStatement preparedStatement = connection.prepareStatement(stringSQL)) {
      preparedStatement.setString(1, playerData.getPlayerUUID().toString());
      preparedStatement.execute();
    } catch (SQLException troubles) {
      throw new PlayerDataException(
          "Cannot delete data for player : " + playerData.getPlayerName(), troubles);
    }
  }

  @Override
  public void updatePlayerData(@NotNull PlayerData playerData) throws PlayerDataException {

    String stringSQL =
        "UPDATE player_fly_data " + "SET remaining_fly_time=? " + "WHERE player_uuid=?;";

    Connection connection = this.dbConnection.getConnection();

    try (PreparedStatement preparedStatement = connection.prepareStatement(stringSQL)) {
      preparedStatement.setInt(1, playerData.getRemainingFlyTime());
      preparedStatement.setString(2, playerData.getPlayerUUID().toString());

      preparedStatement.execute();
    } catch (SQLException troubles) {
      throw new PlayerDataException(
          String.format("Cannot create player data in database : %s", playerData.getPlayerUUID()),
          troubles);
    }
  }

  @Override
  public void updateNickname(Player player) throws PlayerDataException {
    String stringSQL = "UPDATE player_fly_data SET player_name=? WHERE player_uuid=?;";

    Connection connection = this.dbConnection.getConnection();

    try (PreparedStatement preparedStatement = connection.prepareStatement(stringSQL)) {
      preparedStatement.setString(1, player.getName());
      preparedStatement.setString(2, player.getUniqueId().toString());

      preparedStatement.execute();
    } catch (SQLException troubles) {
      throw new PlayerDataException(
          String.format("Cannot get player data in database : %s", player.getName()), troubles);
    }
  }

  @Override
  public boolean containsPlayer(String playerName) throws PlayerDataException {
    String stringSQL = "SELECT COUNT(1) FROM player_fly_data WHERE player_name=?;";

    Connection connection = this.dbConnection.getConnection();

    try (PreparedStatement preparedStatement = connection.prepareStatement(stringSQL)) {

      preparedStatement.setString(1, playerName);

      ResultSet resultSet = preparedStatement.executeQuery();

      return resultSet.getInt(1) == 1;
    } catch (SQLException troubles) {
      throw new PlayerDataException(
          String.format("Cannot get player data in database : %s", playerName), troubles);
    }
  }

  private void initTable() {

    String sqlQuery =
        "CREATE TABLE IF NOT EXISTS player_fly_data"
            + "("
            + "player_uuid VARCHAR(16) NOT NULL,"
            + "player_name VARCHAR(16) NOT NULL,"
            + "unlimited_fly_state BOOLEAN,"
            + "remaining_fly_time INTEGER ,"
            + "PRIMARY KEY (player_uuid)"
            + ");";

    Connection connection = this.dbConnection.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
      statement.execute();
    } catch (SQLException troubles) {
      troubles.printStackTrace();
    }
  }
}
