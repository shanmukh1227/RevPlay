package com.revplay.service;

import com.revplay.dao.AlbumDAO;
import com.revplay.exception.AlbumServiceException;

import java.util.ArrayList;
import java.util.List;

public class AlbumService {

    private AlbumDAO albumDAO;

    // Used by REAL application
    public AlbumService() {
        this.albumDAO = new AlbumDAO();
    }

    // Used ONLY by tests (Mockito / manual mock)
    public AlbumService(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    public int createAlbum(int artistId, String name, String genre,
                           String releaseDate) {

        if (artistId <= 0) {
            throw new AlbumServiceException("Invalid artist id");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new AlbumServiceException("Album name cannot be empty");
        }

        int albumId = albumDAO.createAlbum(
                artistId,
                name.trim(),
                genre == null ? "" : genre.trim(),
                releaseDate
        );

        if (albumId <= 0) {
            throw new AlbumServiceException("Failed to create album");
        }

        return albumId;
    }

    public boolean addSongToAlbum(int albumId, int songId) {

        if (albumId <= 0 || songId <= 0) {
            throw new AlbumServiceException("Invalid album or song id");
        }

        boolean success = albumDAO.addSongToAlbum(albumId, songId);

        if (!success) {
            throw new AlbumServiceException("Song already exists in album");
        }

        return true;
    }

    public List<String> viewAlbumSongs(int albumId) {

        if (albumId <= 0) {
            throw new AlbumServiceException("Invalid album id");
        }

        List<String> songs = albumDAO.viewAlbumSongs(albumId);

        if (songs == null) {
            return new ArrayList<String>();
        }

        return songs;
    }
}
