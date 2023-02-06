package me.jb.flytime.plugin.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import javax.inject.Singleton;

@Singleton
public class FlagManager {

  private StateFlag flyFlag;

  public FlagManager() {}

  public StateFlag getFlyFlag() {
    return flyFlag;
  }

  public void initFlags() {
    FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
    try {
      // create a flag with the name "my-custom-flag", defaulting to true
      StateFlag flag = new StateFlag("flytime", true);
      registry.register(flag);
      flyFlag = flag; // only set our field if there was no error
    } catch (FlagConflictException e) {
      // some other plugin registered a flag by the same name already.
      // you can use the existing flag, but this may cause conflicts - be sure to check type
      Flag<?> existing = registry.get("flytime");
      if (existing instanceof StateFlag) {
        flyFlag = (StateFlag) existing;
      } else {
        System.out.println("Error while trying to get FLY flag.");
        // types don't match - this is bad news! some other plugin conflicts with you
        // hopefully this never actually happens
      }
    }
  }
}
