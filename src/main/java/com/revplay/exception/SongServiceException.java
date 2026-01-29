package com.revplay.exception;

public class SongServiceException extends RuntimeException {

    public SongServiceException(String message) {
        super(message);
    }

    public SongServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
