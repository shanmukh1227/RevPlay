package com.revplay.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.revplay.dao.ArtistDAO;
import com.revplay.service.ArtistService;

public class ArtistServiceTest {

    private ArtistDAO artistDAO;
    private ArtistService artistService;

    @Before
    public void setUp() {
        artistDAO = mock(ArtistDAO.class);
        artistService = new ArtistService(artistDAO);
    }

    
    @Test
    public void testSearchArtistsWithNullKeyword() {

        List<String> result = artistService.searchArtists(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        // DAO should NOT be called
        verify(artistDAO, never()).searchArtists(anyString());
    }

    
    @Test
    public void testSearchArtistsWithEmptyKeyword() {

        List<String> result = artistService.searchArtists("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(artistDAO, never()).searchArtists(anyString());
    }

    //  valid keyword → DAO is called
    @Test
    public void testSearchArtistsWithValidKeyword() {

        List<String> mockResult = new ArrayList<String>();
        mockResult.add("10 | AR Rahman | Bio: Music legend");

        when(artistDAO.searchArtists("rahman")).thenReturn(mockResult);

        List<String> result = artistService.searchArtists("  rahman ");

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(artistDAO, times(1)).searchArtists("rahman");
    }

    // DAO returns empty list
    @Test
    public void testSearchArtistsNoResults() {

        when(artistDAO.searchArtists("unknown"))
                .thenReturn(new ArrayList<String>());

        List<String> result = artistService.searchArtists("unknown");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
