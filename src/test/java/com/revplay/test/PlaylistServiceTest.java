package com.revplay.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revplay.dao.PlaylistDAO;
import com.revplay.service.PlaylistService;

public class PlaylistServiceTest {

    private PlaylistDAO playlistDAOMock;
    private PlaylistService playlistService;

    @Before
    public void setUp() {
        // create mock DAO
        playlistDAOMock = mock(PlaylistDAO.class);

        // inject mock into service
        playlistService = new PlaylistService(playlistDAOMock);
    }

    @Test
    public void testCreatePlaylist() {

        when(playlistDAOMock.createPlaylist(
                1, "JUnit Playlist", "Testing playlist", "PUBLIC"))
                .thenReturn(15);

        int playlistId = playlistService.createPlaylist(
                1, "JUnit Playlist", "Testing playlist", "PUBLIC");

        assertEquals(15, playlistId);

        verify(playlistDAOMock).createPlaylist(
                1, "JUnit Playlist", "Testing playlist", "PUBLIC");
    }

    @Test
    public void testViewMyPlaylists() {

        List<String> mockPlaylists = new ArrayList<String>();
        mockPlaylists.add("15 | JUnit Playlist | PUBLIC");

        when(playlistDAOMock.viewMyPlaylists(1))
                .thenReturn(mockPlaylists);

        List<String> playlists = playlistService.viewMyPlaylists(1);

        assertNotNull(playlists);
        assertEquals(1, playlists.size());

        verify(playlistDAOMock).viewMyPlaylists(1);
    }

    @Test
    public void testAddSongToPlaylist() {

        when(playlistDAOMock.addSongToPlaylist(1, 3))
                .thenReturn(true);

        boolean result = playlistService.addSongToPlaylist(1, 3);

        assertTrue(result);

        verify(playlistDAOMock).addSongToPlaylist(1, 3);
    }

    @Test
    public void testAddSongToPlaylistInvalid() {

        boolean result = playlistService.addSongToPlaylist(0, 0);

        assertFalse(result);

        verify(playlistDAOMock, never())
                .addSongToPlaylist(anyInt(), anyInt());
    }

    @After
    public void tearDown() {
        playlistDAOMock = null;
        playlistService = null;
    }
}
