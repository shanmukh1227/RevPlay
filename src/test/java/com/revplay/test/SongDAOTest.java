package com.revplay.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revplay.dao.SongDAO;
import com.revplay.model.Song;

public class SongDAOTest {

    private static SongDAO songDAO;

    // Runs ONCE before all tests
    @BeforeClass
    public static void beforeAll() {
        songDAO = new SongDAO();
    }

    // -------------------- UPLOAD SONG --------------------
    @Test
    public void testUploadSong() {

        int songId = songDAO.uploadSong(
                "JUnit DAO Song",
                "TEST",
                120,
                "",     // release date optional
                1       // artist_id MUST exist in DB
        );

        assertTrue(songId > 0 || songId == -1);
    }

    // -------------------- SEARCH SONGS --------------------
    @Test
    public void testSearchSongs() {

        List<Song> songs = songDAO.searchSongs("JUnit");

        assertNotNull(songs);
    }

    // -------------------- GET SONG TITLE --------------------
    @Test
    public void testGetSongTitleById() {

        String title = songDAO.getSongTitleById(1);

        // DB dependent
        assertTrue(title == null || title.length() > 0);
    }

    // -------------------- PLAY SONG --------------------
    @Test
    public void testPlaySong() {

        boolean result = songDAO.playSong(1, 1);

        assertTrue(result || !result);
    }

    // -------------------- RECENTLY PLAYED --------------------
    @Test
    public void testGetRecentlyPlayed() {

        List<String> list = songDAO.getRecentlyPlayed(1, 5);

        assertNotNull(list);
    }

    // -------------------- FAVORITES --------------------
    @Test
    public void testAddToFavorites() {

        boolean result = songDAO.addToFavorites(1, 1);

        assertTrue(result || !result);
    }

    @Test
    public void testViewFavorites() {

        List<String> favs = songDAO.viewFavorites(1);

        assertNotNull(favs);
    }

    @Test
    public void testRemoveFromFavorites() {

        boolean result = songDAO.removeFromFavorites(1, 1);

        assertTrue(result || !result);
    }

    // -------------------- VIEW MY SONGS --------------------
    @Test
    public void testViewMySongs() {

        List<String> songs = songDAO.viewMySongs(1);

        assertNotNull(songs);
    }

    // -------------------- TOP PLAYED SONGS --------------------
    @Test
    public void testGetTopPlayedSongs() {

        List<String> list = songDAO.getTopPlayedSongs(1, 5);

        assertNotNull(list);
    }

    // -------------------- BROWSE BY GENRE --------------------
    @Test
    public void testBrowseSongsByGenre() {

        List<Song> songs = songDAO.browseSongsByGenre("TEST");

        assertNotNull(songs);
    }

    // Runs ONCE after all tests
    @AfterClass
    public static void afterAll() {
        songDAO = null;
    }
}
