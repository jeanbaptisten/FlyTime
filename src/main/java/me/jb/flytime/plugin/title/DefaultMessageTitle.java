package me.jb.flytime.plugin.title;

import me.jb.flytime.api.title.MessageTitle;
import me.jb.flytime.api.utils.IntegerUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DefaultMessageTitle implements MessageTitle {

  private final String id;

  private final String message;

  private final String title, subtitle;
  private final int fadeIn, stay, fadeOut;
  private final boolean activeMessage, activeTitle;

  public DefaultMessageTitle(
      @NotNull String id,
      String message,
      @NotNull String title,
      @NotNull String subtitle,
      int fadeIn,
      int stay,
      int fadeOut,
      boolean activeMessage,
      boolean activeTitle) {
    this.id = id;
    this.message = message;
    this.title = title;
    this.subtitle = subtitle;
    this.fadeIn = fadeIn;
    this.stay = stay;
    this.fadeOut = fadeOut;
    this.activeMessage = activeMessage;
    this.activeTitle = activeTitle;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void sendAdd(@NotNull Player player, Integer flyTime) {
    String titleFormatted = this.title.replace("%fly_time_add%", IntegerUtils.parseTime(flyTime));
    String subtitleFormatted =
        this.subtitle.replace("%fly_time_add%", IntegerUtils.parseTime(flyTime));
    player.sendTitle(titleFormatted, subtitleFormatted, this.fadeIn, this.stay, this.fadeOut);
  }

  @Override
  public void sendCountdown(@NotNull Player player) {
    if (this.activeMessage) player.sendMessage(this.message);
    if (this.activeTitle) player.sendTitle(title, subtitle, this.fadeIn, this.stay, this.fadeOut);
  }
}
