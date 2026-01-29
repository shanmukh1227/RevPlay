package com.revplay.service;

import com.revplay.dao.SongDAO;
import com.revplay.exception.SongServiceException;
import com.revplay.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongService {

    private SongDAO songDAO;

    // Used by REAL application
    public SongService() {
        this.songDAO = new SongDAO();
    }

    // Used ONLY by tests (Mockito / manual mock)
    public SongService(SongDAO songDAO) {
        this.songDAO = songDAO;
    }

    public List<Song> searchSongs(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        List<Song> songs = songDAO.searchSongs(keyword.trim());
        return songs == null ? new ArrayList<Song>() : songs;
    }

    public List<Song> browseSongsByGenre(String genre) {

        if (genre == null || genre.trim().isEmpty()) {
            throw new SongServiceException("Genre cannot be empty");
        }

        List<Song> songs = songDAO.browseSongsByGenre(genre.trim());
        return songs == null ? new ArrayList<Song>() : songs;
    }

    public String getSongTitleById(int songId) {

        if (songId <= 0) {
            throw new SongServiceException("Invalid song id");
        }

        String title = songDAO.getSongTitleById(songId);

        if (title == null) {
            throw new SongServiceException("Song not found");
        }

        return title;
    }

    public boolean playSong(int userId, int songId) {

        if (userId <= 0 || songId <= 0) {
            throw new SongServiceException("Invalid user or song id");
        }

        boolean played = songDAO.playSong(userId, songId);

        if (!played) {
            throw new SongServiceException("Unable to play song");
        }

        return true;
    }

    public List<String> getRecentlyPlayed(int userId, int limit) {

        if (userId <= 0) {
            throw new SongServiceException("Invalid user id");
        }

        if (limit <= 0) {
            limit = 10;
        }

        List<String> list = songDAO.getRecentlyPlayed(userId, limit);
        return list == null ? new ArrayList<String>() : list;
    }

    public boolean addToFavorites(int userId, int songId) {

        if (userId <= 0 || songId <= 0) {
            throw new SongServiceException("Invalid user or song id");
        }

        boolean added = songDAO.addToFavorites(userId, songId);

        if (!added) {
            throw new SongServiceException("Song already in favorites");
        }

        return true;
    }

    public boolean removeFromFavorites(int userId, int songId) {

        if (userId <= 0 || songId <= 0) {
            throw new SongServiceException("Invalid user or song id");
        }

        boolean removed = songDAO.removeFromFavorites(userId, songId);

        if (!removed) {
            throw new SongServiceException("Song not found in favorites");
        }

        return true;
    }

    public List<String> viewFavorites(int userId) {

        if (userId <= 0) {
            throw new SongServiceException("Invalid user id");
        }

        List<String> list = songDAO.viewFavorites(userId);
        return list == null ? new ArrayList<String>() : list;
    }

    public int uploadSong(String title, String genre, int durationSeconds,
                          String releaseDate, int artistId) {

        if (artistId <= 0) {
            throw new SongServiceException("Invalid artist id");
        }

        if (title == null || title.trim().isEmpty()) {
            throw new SongServiceException("Song title cannot be empty");
        }

        if (durationSeconds <= 0) {
            throw new SongServiceException("Invalid song duration");
        }

        int songId = songDAO.uploadSong(
                title.trim(),
                genre == null ? "" : genre.trim(),
                durationSeconds,
                releaseDate,
                artistId
        );

        if (songId <= 0) {
            throw new SongServiceException("Failed to upload song");
        }

        return songId;
    }

    public List<String> viewMySongs(int artistId) {

        if (artistId <= 0) {
            throw new SongServiceException("Invalid artist id");
        }

        List<String> list = songDAO.viewMySongs(artistId);
        return list == null ? new ArrayList<String>() : list;
    }

    public List<String> getTopPlayedSongs(int artistId, int limit) {

        if (artistId <= 0) {
            throw new SongServiceException("Invalid artist id");
        }

        if (limit <= 0) {
            limit = 10;
        }

        List<String> list = songDAO.getTopPlayedSongs(artistId, limit);
        return list == null ? new ArrayList<String>() : list;
    }
}
