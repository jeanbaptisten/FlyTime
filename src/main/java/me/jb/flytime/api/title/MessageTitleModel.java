package me.jb.flytime.api.title;

import org.jetbrains.annotations.NotNull;

public interface MessageTitleModel {
    void addMessageTitle(@NotNull MessageTitle messageTitle);

    MessageTitle getMessageTitle(@NotNull String id);

    boolean containsId(@NotNull String id);

    void clearCache();
}
