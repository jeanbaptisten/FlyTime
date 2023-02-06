package me.jb.flytime.api.customevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class FlyEvent extends Event implements Cancellable {


    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled = false;

    private final Player player;
    private final int remainingFlyTime;
    private final boolean forced;

    public FlyEvent(
            @NotNull Player player,
            @Range(from = 0, to = Integer.MAX_VALUE) int remainingFlyTime,
            boolean forced) {
        this.player = player;
        this.remainingFlyTime = remainingFlyTime;
        this.forced = forced;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    public int getRemainingFlyTime() {
        return this.remainingFlyTime;
    }

    public boolean isForced() {
        return this.forced;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
