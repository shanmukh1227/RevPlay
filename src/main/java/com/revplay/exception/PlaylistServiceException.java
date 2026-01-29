package com.revplay.exception;

public class PlaylistServiceException extends RuntimeException {

    public PlaylistServiceException(String message) {
        super(message);
    }

    public PlaylistServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
