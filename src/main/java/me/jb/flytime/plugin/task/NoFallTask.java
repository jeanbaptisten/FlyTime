package me.jb.flytime.plugin.task;

import java.util.UUID;

import me.jb.flytime.api.nofall.NoFallManager;
import org.jetbrains.annotations.NotNull;

public class NoFallTask implements Runnable {

  private final NoFallManager noFallManager;
  private final UUID playerUUID;

  public NoFallTask(@NotNull NoFallManager noFallManager, @NotNull UUID playerUUID) {
    this.noFallManager = noFallManager;
    this.playerUUID = playerUUID;
  }

  @Override
  public void run() {
    this.noFallManager.removePlayer(this.playerUUID);
  }
}
