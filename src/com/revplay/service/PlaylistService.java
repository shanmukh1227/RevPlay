package com.revplay.service;

import com.revplay.dao.PlaylistDAO;

import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

    private PlaylistDAO playlistDAO;

    public PlaylistService() {
        this.playlistDAO = new PlaylistDAO();
    }

    public int createPlaylist(int userId, String name, String desc, String privacy) {
        if (userId <= 0) return -1;
        if (name == null || name.trim().isEmpty()) return -1;

        String p = normalizePrivacy(privacy);
        if (p == null) return -1;

        if (desc == null) desc = "";

        return playlistDAO.createPlaylist(
                userId,
                name.trim(),
                desc.trim(),
                p
        );
    }

    public List<String> viewMyPlaylists(int userId) {
        if (userId <= 0) {
            return new ArrayList<String>();
        }
        return playlistDAO.viewMyPlaylists(userId);
    }

    public List<String> viewPlaylistSongs(int playlistId) {
        if (playlistId <= 0) {
            return new ArrayList<String>();
        }
        return playlistDAO.viewPlaylistSongs(playlistId);
    }

    public boolean addSongToPlaylist(int playlistId, int songId) {
        if (playlistId <= 0 || songId <= 0) return false;
        return playlistDAO.addSongToPlaylist(playlistId, songId);
    }

    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        if (playlistId <= 0 || songId <= 0) return false;
        return playlistDAO.removeSongFromPlaylist(playlistId, songId);
    }

    public boolean updatePlaylist(int playlistId,
                                  int userId,
                                  String newName,
                                  String newDesc,
                                  String privacy) {

        if (playlistId <= 0 || userId <= 0) return false;
        if (newName == null || newName.trim().isEmpty()) return false;

        String p = normalizePrivacy(privacy);
        if (p == null) return false;

        if (newDesc == null) newDesc = "";

        return playlistDAO.updatePlaylist(
                playlistId,
                userId,
                newName.trim(),
                newDesc.trim(),
                p
        );
    }

    public boolean deletePlaylist(int playlistId, int userId) {
        if (playlistId <= 0 || userId <= 0) return false;
        return playlistDAO.deletePlaylist(playlistId, userId);
    }

  

    public List<String> viewPublicPlaylists(int currentUserId) {
        if (currentUserId <= 0) {
            return new ArrayList<String>();
        }
        return playlistDAO.viewPublicPlaylists(currentUserId);
    }

    public List<String> viewPlaylistSongsPublic(int playlistId) {
        if (playlistId <= 0) {
            return new ArrayList<String>();
        }
        return playlistDAO.viewPlaylistSongsPublic(playlistId);
    }

 

    private String normalizePrivacy(String privacy) {
        if (privacy == null) return null;

        String p = privacy.trim().toUpperCase();
        if ("PUBLIC".equals(p) || "PRIVATE".equals(p)) {
            return p;
        }
        return null;
    }
}
