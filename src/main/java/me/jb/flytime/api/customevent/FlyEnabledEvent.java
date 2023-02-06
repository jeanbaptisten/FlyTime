package me.jb.flytime.api.customevent;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class FlyEnabledEvent extends FlyEvent {

  public FlyEnabledEvent(
      @NotNull Player player,
      @Range(from = 0, to = Integer.MAX_VALUE) int remainingFlyTime,
      boolean forced) {
    super(player, remainingFlyTime, forced);
  }
}
