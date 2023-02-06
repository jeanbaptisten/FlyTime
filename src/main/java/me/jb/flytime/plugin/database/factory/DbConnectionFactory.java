package me.jb.flytime.plugin.database.factory;

import com.google.common.base.Verify;
import me.jb.flytime.api.database.DbConnection;
import me.jb.flytime.plugin.database.MySQLDbConnection;
import me.jb.flytime.plugin.database.SQLiteDbConnection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class DbConnectionFactory {

  public static DbConnection getDbConnection(
      @NotNull Plugin plugin, @NotNull FileConfiguration fileConfiguration) {

    String dbType = fileConfiguration.getString("storage.storage-method");
    Verify.verify(dbType != null);

    switch (dbType) {
      case "MySQL":
        String host = fileConfiguration.getString("mysql.host");
        String database = fileConfiguration.getString("mysql.database");
        String user = fileConfiguration.getString("mysql.username");
        String password = fileConfiguration.getString("mysql.password");
        return new MySQLDbConnection(host, database, user, password);

      case "SQLite":
        return new SQLiteDbConnection(plugin);

      default:
        throw new IllegalStateException("Db type " + dbType + " isn't supported or doesn't exist.");
    }
  }
}
