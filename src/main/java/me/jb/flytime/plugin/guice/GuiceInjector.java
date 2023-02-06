/*
 * Copyright (c) 2022 - Loïc DUBOIS-TERMOZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.jb.flytime.plugin.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.database.DbConnection;
import me.jb.flytime.plugin.guice.module.GuiceBukkitLibsModule;
import me.jb.flytime.plugin.guice.module.GuiceBukkitModule;
import me.jb.flytime.plugin.guice.module.GuiceGeneralModule;
import me.jb.flytime.plugin.guice.module.GuiceFlyTimeModule;
import me.jb.flytime.plugin.worldguard.FlagManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/** Guice injector for Bukkit plugin. */
public final class GuiceInjector {

  /** Private constructor. */
  private GuiceInjector() {}

  /**
   * Inject already instantiated stuff into Guice (e.g. {@link JavaPlugin}).
   *
   * @param plugin The plugin to inject into Guice.
   */
  public static void inject(
      @NotNull JavaPlugin plugin,
      @NotNull FileHandler fileHandler,
      @NotNull DbConnection dbConnection,
      @NotNull FlagManager flagManager) {
    Injector injector =
        Guice.createInjector(
            new GuiceGeneralModule(fileHandler),
            new GuiceBukkitModule(plugin),
            new GuiceBukkitLibsModule(plugin),
            new GuiceFlyTimeModule(dbConnection, flagManager));
    injector.injectMembers(plugin);
  }
}
