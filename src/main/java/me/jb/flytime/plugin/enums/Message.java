package me.jb.flytime.plugin.enums;

import org.jetbrains.annotations.NotNull;

public enum Message {
  RELOAD("reload"),
  HELP("help"),
  FLY_ON("flyOn"),
  FLY_OFF("flyOff"),
  FLY_DISABLED("flyDisable"),
  FLY_ENABLED("flyEnabled"),
  FLY_OBTAINED("flyObtained"),
  FLY_GIVEN("flyGiven"),
  FLY_REMOVED_SENDER("flyRemovedSender"),
  FLY_REMOVED("flyRemoved"),
  NOT_ENOUGH_FLY_TIME("notEnoughFlytime"),
  CANNOT_ADD_FLY_TIME("cannotAddFlyTime"),
  CANNOT_REMOVE_FLY_TIME("cannotRemoveFlyTime"),
  ALREADY_UNLIMITED_FLY_TIME("alreadyUnlimitedFlyTime"),
  DOESNT_UNLIMITED_FLY_TIME("doesntUnlimitedFlyTime"),
  UNLIMITED_FLY_TIME_GIVE("unlimitedFlyTimeGiven"),
  UNLIMITED_FLY_TIME_GOT("unlimitedFlyTimeGet"),
  UNLIMITED_FLY_TIME_TAKEN("unlimitedFlyTimeTaken"),
  UNLIMITED_FLY_TIME_LOST("unlimitedFlyTimeLost"),
  PLAYER_HAVE_BYPASS("playerHaveBypass"),
  ;

  private final String key;

  Message(String key) {
    this.key = key;
  }

  @NotNull
  public String getKey() {
    return this.key;
  }
}
