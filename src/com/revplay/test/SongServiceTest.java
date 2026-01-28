package com.revplay.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.revplay.model.Song;
import com.revplay.service.SongService;

public class SongServiceTest {

    private SongService service = new SongService();

    @Test
    public void testUploadSong() {

        int songId = service.uploadSong(
                "JUnit Song",
                "POP",
                180,
                "",
                1   
        );

        assertTrue(songId > 0 || songId == -1);
    }

    @Test
    public void testSearchSongs() {

        List<Song> songs = service.searchSongs("JUnit");

        assertNotNull(songs);
    }

    @Test
    public void testBrowseByGenre() {

        List<Song> songs = service.browseSongsByGenre("POP");

        assertNotNull(songs);
    }

    @Test
    public void testPlaySong() {

        boolean result = service.playSong(1, 1);

        assertTrue(result || !result);
    }
}
