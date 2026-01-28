package com.revplay.service;

import com.revplay.dao.SongDAO;
import com.revplay.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongService {

    private SongDAO songDAO;

    public SongService() {
        this.songDAO = new SongDAO();
    }

    public List<Song> searchSongs(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        return songDAO.searchSongs(keyword.trim());
    }

    public List<Song> browseSongsByGenre(String genre) {
        if (genre == null) {
            genre = "";
        }
        return songDAO.browseSongsByGenre(genre.trim());
    }

    public String getSongTitleById(int songId) {
        if (songId <= 0) {
            return null;
        }
        return songDAO.getSongTitleById(songId);
    }

    public boolean playSong(int userId, int songId) {
        if (userId <= 0 || songId <= 0) {
            return false;
        }
        return songDAO.playSong(userId, songId);
    }

    public List<String> getRecentlyPlayed(int userId, int limit) {
        if (userId <= 0) {
            return new ArrayList<String>();
        }
        if (limit <= 0) {
            limit = 10;
        }
        return songDAO.getRecentlyPlayed(userId, limit);
    }

    public boolean addToFavorites(int userId, int songId) {
        if (userId <= 0 || songId <= 0) {
            return false;
        }
        return songDAO.addToFavorites(userId, songId);
    }

    public boolean removeFromFavorites(int userId, int songId) {
        if (userId <= 0 || songId <= 0) {
            return false;
        }
        return songDAO.removeFromFavorites(userId, songId);
    }

    public List<String> viewFavorites(int userId) {
        if (userId <= 0) {
            return new ArrayList<String>();
        }
        return songDAO.viewFavorites(userId);
    }

    public boolean uploadSong(String title,
                              String genre,
                              int durationSeconds,
                              String releaseDate,
                              int artistId) {

        if (artistId <= 0) return false;
        if (title == null || title.trim().isEmpty()) return false;
        if (durationSeconds <= 0) return false;

        if (genre == null) {
            genre = "";
        }

        return songDAO.uploadSong(
                title.trim(),
                genre.trim(),
                durationSeconds,
                releaseDate,
                artistId
        );
    }

    public List<String> viewMySongs(int artistId) {
        if (artistId <= 0) {
            return new ArrayList<String>();
        }
        return songDAO.viewMySongs(artistId);
    }

    public List<String> getTopPlayedSongs(int artistId, int limit) {
        if (artistId <= 0) {
            return new ArrayList<String>();
        }
        if (limit <= 0) {
            limit = 10;
        }
        return songDAO.getTopPlayedSongs(artistId, limit);
    }
}
