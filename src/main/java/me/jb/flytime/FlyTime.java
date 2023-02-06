package me.jb.flytime;

import com.sk89q.worldguard.WorldGuard;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.inject.Inject;

import me.jb.flytime.plugin.placeholder.FlyTimeExpansion;
import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.database.DbConnection;
import me.jb.flytime.api.playerdata.PlayerDataService;
import me.jb.flytime.api.task.TaskManager;
import me.jb.flytime.api.title.MessageTitleManager;
import me.jb.flytime.plugin.database.SQLiteDbConnection;
import me.jb.flytime.plugin.guice.GuiceInjector;
import me.jb.flytime.plugin.registers.CommandRegister;
import me.jb.flytime.plugin.registers.ListenerRegister;
import me.jb.flytime.plugin.worldguard.Entry;
import me.jb.flytime.plugin.worldguard.FlagManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlyTime extends JavaPlugin {

  private final FileHandler fileHandler = new FileHandler(this);
  private final FlagManager flagManager = new FlagManager();
  private DbConnection dbConnection;
  @Inject private CommandRegister commandRegister;
  @Inject private ListenerRegister listenerRegister;
  @Inject private PlayerDataService playerDataService;
  @Inject private MessageTitleManager messageTitleManager;
  @Inject private TaskManager taskManager;

  @Inject private FlyTimeExpansion flyTimeExpansion;

  @Override
  public void onEnable() {
    this.getLogger().log(Level.INFO, "§3Initializing " + this.getName() + " plugin.");

    this.setupDatabaseConnection();
    this.getLogger().log(Level.INFO, "§3Database connection setup.");

    GuiceInjector.inject(this, this.fileHandler, this.dbConnection, this.flagManager);
    this.getLogger().log(Level.INFO, "§3Guice injected.");

    this.playerDataService.loadData();
    this.getLogger().log(Level.INFO, "§3Data setup.");

    this.taskManager.loadTasks();
    this.messageTitleManager.loadTitles();
    this.registerPlaceholders();
    this.getLogger().log(Level.INFO, "§3Plugin system setup.");

    commandRegister.registerCommands();
    commandRegister.loadCommandMessages(this.fileHandler.getMainConfig().getConfig());
    this.getLogger().log(Level.INFO, "§3Commands registered.");

    listenerRegister.registerListeners();
    WorldGuard.getInstance().getPlatform().getSessionManager().registerHandler(Entry.factory, null);
    this.getLogger().log(Level.INFO, "§3Listeners registered.");

    this.getLogger().log(Level.INFO, "§3" + this.getName() + " initialized.");
  }

  @Override
  public void onLoad() {
    this.flagManager.initFlags();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  private void registerPlaceholders() {
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      this.flyTimeExpansion.register();
    }
  }

  private void setupDatabaseConnection() {

    this.dbConnection = new SQLiteDbConnection(this);

    try {
      this.dbConnection.open();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
