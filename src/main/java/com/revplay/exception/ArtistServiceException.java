package com.revplay.exception;

public class ArtistServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ArtistServiceException(String message) {
        super(message);
    }
}
