package me.jb.flytime.api.title;

public interface MessageTitleDAO {
    MessageTitle getTitle(String id);

    MessageTitle getCountdown(String id);
}
