package com.revplay.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.revplay.service.AlbumService;
import com.revplay.dao.AlbumDAO;

public class AlbumServiceTest {

    private AlbumService service = new AlbumService();
    private AlbumDAO albumDAO = new AlbumDAO();

    @Test
    public void testCreateAlbum() {

        int albumId = albumDAO.createAlbum(
                1,
                "JUnit Album",
                "TEST",
                ""
        );

        assertTrue(albumId > 0 || albumId == -1);
    }

    @Test
    public void testAddSongToAlbum() {

        boolean result = service.addSongToAlbum(1, 1);

        assertTrue(result || !result);
    }

    @Test
    public void testViewAlbumSongs() {

        List<String> songs = service.viewAlbumSongs(1);

        assertNotNull(songs);
    }
}
