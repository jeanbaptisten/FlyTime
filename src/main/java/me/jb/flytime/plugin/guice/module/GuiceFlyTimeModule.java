package me.jb.flytime.plugin.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import me.jb.flytime.api.database.DbConnection;
import me.jb.flytime.plugin.worldguard.FlagManager;
import org.jetbrains.annotations.NotNull;

public class GuiceFlyTimeModule extends AbstractModule {

  private final DbConnection dbConnection;
  private final FlagManager flagManager;

  public GuiceFlyTimeModule(@NotNull DbConnection dbConnection, @NotNull FlagManager flagManager) {
    this.dbConnection = dbConnection;
    this.flagManager = flagManager;
  }

  @Provides
  public DbConnection provideDbConnection() {
    return this.dbConnection;
  }

  @Provides
  public FlagManager providesFlagManager() {
    return this.flagManager;
  }
}
