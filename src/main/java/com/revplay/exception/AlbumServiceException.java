package com.revplay.exception;

public class AlbumServiceException extends RuntimeException {

    public AlbumServiceException(String message) {
        super(message);
    }

    public AlbumServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
