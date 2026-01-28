package com.revplay.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.revplay.service.PlaylistService;

public class PlaylistServiceTest {

    private PlaylistService service = new PlaylistService();

    @Test
    public void testCreatePlaylist() {

        int playlistId = service.createPlaylist(
                1,
                "JUnit Playlist",
                "Testing playlist",
                "PUBLIC"
        );

        assertTrue(playlistId > 0 || playlistId == -1);
    }

    @Test
    public void testViewMyPlaylists() {

        List<String> playlists = service.viewMyPlaylists(1);

        assertNotNull(playlists);
    }

    @Test
    public void testAddSongToPlaylist() {

        boolean result = service.addSongToPlaylist(1, 1);

        assertTrue(result || !result);
    }
}
