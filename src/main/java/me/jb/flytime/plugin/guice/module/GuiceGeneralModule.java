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

package me.jb.flytime.plugin.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import me.jb.flytime.api.PluginManager;
import me.jb.flytime.api.configuration.FileHandler;
import me.jb.flytime.api.nofall.NoFallManager;
import me.jb.flytime.api.playerdata.PlayerDataDAO;
import me.jb.flytime.api.playerdata.PlayerDataModel;
import me.jb.flytime.api.playerdata.PlayerDataService;
import me.jb.flytime.api.task.TaskManager;
import me.jb.flytime.api.title.MessageTitleDAO;
import me.jb.flytime.api.title.MessageTitleManager;
import me.jb.flytime.api.title.MessageTitleModel;
import me.jb.flytime.plugin.DefaultPluginManager;
import me.jb.flytime.plugin.task.DefaultTaskManager;
import me.jb.flytime.plugin.nofall.DefaultNoFallManager;
import me.jb.flytime.plugin.playerdata.DefaultPlayerDataModel;
import me.jb.flytime.plugin.playerdata.DefaultPlayerDataService;
import me.jb.flytime.plugin.playerdata.dao.SQLPlayerDataDAO;
import me.jb.flytime.plugin.title.DefaultMessageTitleManager;
import me.jb.flytime.plugin.title.DefaultMessageTitleModel;
import me.jb.flytime.plugin.title.YamlMessageTitleDAO;

/** General Guice module. */
public class GuiceGeneralModule extends AbstractModule {

  private final FileHandler fileHandler;

  public GuiceGeneralModule(FileHandler fileHandler) {
    this.fileHandler = fileHandler;
  }

  @Override
  public void configure() {
    // Binding DAO from configured type
    super.bind(PlayerDataDAO.class).to(SQLPlayerDataDAO.class);
    super.bind(PlayerDataService.class).to(DefaultPlayerDataService.class);
    super.bind(PlayerDataModel.class).to(DefaultPlayerDataModel.class);
    super.bind(MessageTitleManager.class).to(DefaultMessageTitleManager.class);
    super.bind(MessageTitleModel.class).to(DefaultMessageTitleModel.class);
    super.bind(MessageTitleDAO.class).to(YamlMessageTitleDAO.class);
    super.bind(TaskManager.class).to(DefaultTaskManager.class);
    super.bind(PluginManager.class).to(DefaultPluginManager.class);
    super.bind(NoFallManager.class).to(DefaultNoFallManager.class);
  }

  @Provides
  public FileHandler provideFileHandler() {
    return this.fileHandler;
  }
}
