package me.jb.flytime.plugin.title;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import me.jb.flytime.api.title.MessageTitle;
import me.jb.flytime.api.title.MessageTitleModel;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefaultMessageTitleModel implements MessageTitleModel {

  private final Map<String, MessageTitle> titleHashMap = new HashMap<>();

  @Inject
  public DefaultMessageTitleModel() {}

  @Override
  public void addMessageTitle(@NotNull MessageTitle messageTitle) {
    Preconditions.checkArgument(!this.titleHashMap.containsKey(messageTitle.getId()));

    this.titleHashMap.put(messageTitle.getId(), messageTitle);
  }

  @Override
  public MessageTitle getMessageTitle(@NotNull String id) {
    Preconditions.checkArgument(this.titleHashMap.containsKey(id));

    return this.titleHashMap.get(id);
  }

  @Override
  public boolean containsId(@NotNull String id) {
    return this.titleHashMap.containsKey(id);
  }

  @Override
  public void clearCache() {
    this.titleHashMap.clear();
  }
}
