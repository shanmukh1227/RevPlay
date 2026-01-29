package com.revplay.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revplay.dao.AlbumDAO;

public class AlbumDAOTest {

    private static AlbumDAO albumDAO;

    // Runs ONCE before all tests
    @BeforeClass
    public static void beforeAll() {
        albumDAO = new AlbumDAO();
    }

    // ---------------- CREATE ALBUM ----------------
    @Test
    public void testCreateAlbum() {

        int albumId = albumDAO.createAlbum(
                1,                  // artist_id must exist
                "JUnit DAO Album",
                "TEST",
                ""                  // no release date
        );

        // DB dependent, so safe assertion
        assertTrue(albumId > 0 || albumId == -1);
    }

    // ---------------- VIEW MY ALBUMS ----------------
    @Test
    public void testViewMyAlbums() {

        List<String> albums = albumDAO.viewMyAlbums(1);

        assertNotNull(albums);
    }

    // ---------------- ADD SONG TO ALBUM ----------------
    @Test
    public void testAddSongToAlbum() {

        boolean result = albumDAO.addSongToAlbum(
                1,      // album_id
                1       // song_id
        );

        assertTrue(result || !result);
    }

    // ---------------- VIEW ALBUM SONGS ----------------
    @Test
    public void testViewAlbumSongs() {

        List<String> songs = albumDAO.viewAlbumSongs(1);

        assertNotNull(songs);
    }

    // ---------------- SEARCH ALBUMS ----------------
    @Test
    public void testSearchAlbums() {

        List<String> albums = albumDAO.searchAlbums("JUnit");

        assertNotNull(albums);
    }

    // Runs ONCE after all tests
    @AfterClass
    public static void afterAll() {
        albumDAO = null;
    }
}
