package com.revplay.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revplay.dao.AlbumDAO;
import com.revplay.service.AlbumService;

public class AlbumServiceTest {

    private AlbumDAO albumDAOMock;
    private AlbumService albumService;

    @Before
    public void setUp() {
        // create mock DAO
        albumDAOMock = mock(AlbumDAO.class);

        // inject mock into service
        albumService = new AlbumService(albumDAOMock);
    }

    @Test
    public void testCreateAlbumSuccess() {

        // mock behavior
        when(albumDAOMock.createAlbum(1, "JUnit Album", "TEST", ""))
                .thenReturn(10);

        int albumId = albumService.createAlbum(
                1, "JUnit Album", "TEST", "");

        assertEquals(10, albumId);

        // verify DAO call
        verify(albumDAOMock).createAlbum(1, "JUnit Album", "TEST", "");
    }

    @Test
    public void testCreateAlbumInvalidArtist() {

        int albumId = albumService.createAlbum(
                0, "Album", "POP", "");

        assertEquals(-1, albumId);

        // DAO should NOT be called
        verify(albumDAOMock, never()).createAlbum(
                anyInt(), anyString(), anyString(), anyString());
    }

    @Test
    public void testAddSongToAlbumSuccess() {

        when(albumDAOMock.addSongToAlbum(1, 5)).thenReturn(true);

        boolean result = albumService.addSongToAlbum(1, 5);

        assertTrue(result);

        verify(albumDAOMock).addSongToAlbum(1, 5);
    }

    @Test
    public void testAddSongToAlbumInvalid() {

        boolean result = albumService.addSongToAlbum(0, 0);

        assertFalse(result);

        verify(albumDAOMock, never()).addSongToAlbum(anyInt(), anyInt());
    }

    @Test
    public void testViewAlbumSongs() {

        List<String> mockSongs = new ArrayList<String>();
        mockSongs.add("1 | Song A | POP");

        when(albumDAOMock.viewAlbumSongs(1)).thenReturn(mockSongs);

        List<String> songs = albumService.viewAlbumSongs(1);

        assertNotNull(songs);
        assertEquals(1, songs.size());

        verify(albumDAOMock).viewAlbumSongs(1);
    }

    @After
    public void tearDown() {
        albumDAOMock = null;
        albumService = null;
    }
}
