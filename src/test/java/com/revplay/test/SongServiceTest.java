package com.revplay.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revplay.dao.SongDAO;
import com.revplay.model.Song;
import com.revplay.service.SongService;

public class SongServiceTest {

    private SongDAO songDAOMock;
    private SongService songService;

    @Before
    public void setUp() {
        songDAOMock = mock(SongDAO.class);
        songService = new SongService(songDAOMock);
    }

    @Test
    public void testUploadSong() {

        when(songDAOMock.uploadSong(
                "JUnit Song", "POP", 180, "", 1))
                .thenReturn(10);

        int songId = songService.uploadSong(
                "JUnit Song", "POP", 180, "", 1);

        assertEquals(10, songId);

        verify(songDAOMock).uploadSong(
                "JUnit Song", "POP", 180, "", 1);
    }

    @Test
    public void testSearchSongs() {

        List<Song> mockSongs = new ArrayList<Song>();
        mockSongs.add(new Song(1, "JUnit Song", "POP", 180, "Artist"));

        when(songDAOMock.searchSongs("JUnit"))
                .thenReturn(mockSongs);

        List<Song> songs = songService.searchSongs("JUnit");

        assertNotNull(songs);
        assertEquals(1, songs.size());

        verify(songDAOMock).searchSongs("JUnit");
    }

    @Test
    public void testBrowseByGenre() {

        List<Song> mockSongs = new ArrayList<Song>();
        mockSongs.add(new Song(2, "Pop Song", "POP", 200, "Artist"));

        when(songDAOMock.browseSongsByGenre("POP"))
                .thenReturn(mockSongs);

        List<Song> songs = songService.browseSongsByGenre("POP");

        assertNotNull(songs);
        assertEquals(1, songs.size());

        verify(songDAOMock).browseSongsByGenre("POP");
    }

    @Test
    public void testPlaySong() {

        when(songDAOMock.playSong(1, 1))
                .thenReturn(true);

        boolean result = songService.playSong(1, 1);

        assertTrue(result);

        verify(songDAOMock).playSong(1, 1);
    }

    @Test
    public void testPlaySongInvalid() {

        boolean result = songService.playSong(0, 0);

        assertFalse(result);

        verify(songDAOMock, never())
                .playSong(anyInt(), anyInt());
    }

    @After
    public void tearDown() {
        songDAOMock = null;
        songService = null;
    }
}
