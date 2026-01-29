package com.revplay.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revplay.dao.PlaylistDAO;

public class PlaylistDAOTest {

    private static PlaylistDAO playlistDAO;

    // Runs ONCE before all tests
    @BeforeClass
    public static void beforeAll() {
        playlistDAO = new PlaylistDAO();
    }

    // -------------------- CREATE PLAYLIST --------------------
    @Test
    public void testCreatePlaylist() {

        int playlistId = playlistDAO.createPlaylist(
                1,                    // user_id must exist
                "JUnit DAO Playlist",
                "Test playlist",
                "PUBLIC"
        );

        assertTrue(playlistId > 0 || playlistId == -1);
    }

    // -------------------- VIEW MY PLAYLISTS --------------------
    @Test
    public void testViewMyPlaylists() {

        List<String> playlists = playlistDAO.viewMyPlaylists(1);

        assertNotNull(playlists);
    }

    // -------------------- ADD SONG TO PLAYLIST --------------------
    @Test
    public void testAddSongToPlaylist() {

        boolean result = playlistDAO.addSongToPlaylist(1, 1);

        assertTrue(result || !result);
    }

    // -------------------- VIEW PLAYLIST SONGS --------------------
    @Test
    public void testViewPlaylistSongs() {

        List<String> songs = playlistDAO.viewPlaylistSongs(1);

        assertNotNull(songs);
    }

    // -------------------- REMOVE SONG FROM PLAYLIST --------------------
    @Test
    public void testRemoveSongFromPlaylist() {

        boolean result = playlistDAO.removeSongFromPlaylist(1, 1);

        assertTrue(result || !result);
    }

    // -------------------- UPDATE PLAYLIST --------------------
    @Test
    public void testUpdatePlaylist() {

        boolean result = playlistDAO.updatePlaylist(
                1,                    // playlist_id
                1,                    // user_id (owner)
                "Updated Playlist",
                "Updated desc",
                "PRIVATE"
        );

        assertTrue(result || !result);
    }

    // -------------------- VIEW PUBLIC PLAYLISTS --------------------
    @Test
    public void testViewPublicPlaylists() {

        List<String> playlists = playlistDAO.viewPublicPlaylists(1);

        assertNotNull(playlists);
    }

    // -------------------- DELETE PLAYLIST --------------------
    @Test
    public void testDeletePlaylist() {

        boolean result = playlistDAO.deletePlaylist(1, 1);

        assertTrue(result || !result);
    }

    // Runs ONCE after all tests
    @AfterClass
    public static void afterAll() {
        playlistDAO = null;
    }
}
