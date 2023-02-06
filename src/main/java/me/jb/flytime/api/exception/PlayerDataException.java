package me.jb.flytime.api.exception;

public class PlayerDataException extends Exception{

    public PlayerDataException(String message) {
        super(message);
    }

    public PlayerDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
