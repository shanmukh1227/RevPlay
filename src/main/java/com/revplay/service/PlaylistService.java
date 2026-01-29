package com.revplay.service;

import com.revplay.dao.PlaylistDAO;
import com.revplay.exception.PlaylistServiceException;

import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

    private PlaylistDAO playlistDAO;

    // Used by REAL application
    public PlaylistService() {
        this.playlistDAO = new PlaylistDAO();
    }

    // Used ONLY by tests (Mockito / manual mock)
    public PlaylistService(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    public int createPlaylist(int userId, String name, String desc,
                              String privacy) {

        if (userId <= 0) {
            throw new PlaylistServiceException("Invalid user id");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new PlaylistServiceException("Playlist name cannot be empty");
        }

        String p = normalizePrivacy(privacy);
        if (p == null) {
            throw new PlaylistServiceException("Invalid privacy value");
        }

        if (desc == null) {
            desc = "";
        }

        int playlistId = playlistDAO.createPlaylist(
                userId,
                name.trim(),
                desc.trim(),
                p
        );

        if (playlistId <= 0) {
            throw new PlaylistServiceException("Failed to create playlist");
        }

        return playlistId;
    }

    public List<String> viewMyPlaylists(int userId) {

        if (userId <= 0) {
            throw new PlaylistServiceException("Invalid user id");
        }

        List<String> playlists = playlistDAO.viewMyPlaylists(userId);

        return playlists == null ? new ArrayList<String>() : playlists;
    }

    public List<String> viewPlaylistSongs(int playlistId) {

        if (playlistId <= 0) {
            throw new PlaylistServiceException("Invalid playlist id");
        }

        List<String> songs = playlistDAO.viewPlaylistSongs(playlistId);

        return songs == null ? new ArrayList<String>() : songs;
    }

    public boolean addSongToPlaylist(int playlistId, int songId) {

        if (playlistId <= 0 || songId <= 0) {
            throw new PlaylistServiceException("Invalid playlist or song id");
        }

        boolean success = playlistDAO.addSongToPlaylist(playlistId, songId);

        if (!success) {
            throw new PlaylistServiceException("Song already exists in playlist");
        }

        return true;
    }

    public boolean removeSongFromPlaylist(int playlistId, int songId) {

        if (playlistId <= 0 || songId <= 0) {
            throw new PlaylistServiceException("Invalid playlist or song id");
        }

        boolean success = playlistDAO.removeSongFromPlaylist(playlistId, songId);

        if (!success) {
            throw new PlaylistServiceException("Song not found in playlist");
        }

        return true;
    }

    public boolean updatePlaylist(int playlistId, int userId, String newName,
                                  String newDesc, String privacy) {

        if (playlistId <= 0 || userId <= 0) {
            throw new PlaylistServiceException("Invalid playlist or user id");
        }

        if (newName == null || newName.trim().isEmpty()) {
            throw new PlaylistServiceException("Playlist name cannot be empty");
        }

        String p = normalizePrivacy(privacy);
        if (p == null) {
            throw new PlaylistServiceException("Invalid privacy value");
        }

        if (newDesc == null) {
            newDesc = "";
        }

        boolean updated = playlistDAO.updatePlaylist(
                playlistId,
                userId,
                newName.trim(),
                newDesc.trim(),
                p
        );

        if (!updated) {
            throw new PlaylistServiceException("Failed to update playlist");
        }

        return true;
    }

    public boolean deletePlaylist(int playlistId, int userId) {

        if (playlistId <= 0 || userId <= 0) {
            throw new PlaylistServiceException("Invalid playlist or user id");
        }

        boolean deleted = playlistDAO.deletePlaylist(playlistId, userId);

        if (!deleted) {
            throw new PlaylistServiceException("Failed to delete playlist");
        }

        return true;
    }

    public List<String> viewPublicPlaylists(int currentUserId) {

        if (currentUserId <= 0) {
            throw new PlaylistServiceException("Invalid user id");
        }

        List<String> playlists = playlistDAO.viewPublicPlaylists(currentUserId);

        return playlists == null ? new ArrayList<String>() : playlists;
    }

    public List<String> viewPlaylistSongsPublic(int playlistId) {

        if (playlistId <= 0) {
            throw new PlaylistServiceException("Invalid playlist id");
        }

        List<String> songs = playlistDAO.viewPlaylistSongsPublic(playlistId);

        return songs == null ? new ArrayList<String>() : songs;
    }

    private String normalizePrivacy(String privacy) {

        if (privacy == null) {
            return null;
        }

        String p = privacy.trim().toUpperCase();
        if ("PUBLIC".equals(p) || "PRIVATE".equals(p)) {
            return p;
        }
        return null;
    }
}
