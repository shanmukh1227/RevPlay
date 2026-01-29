package com.revplay.service;

import java.util.ArrayList;
import java.util.List;
import com.revplay.dao.ArtistDAO;

public class ArtistService {
	private ArtistDAO artistDAO;

	// Used by real application
	public ArtistService() {
		this.artistDAO = new ArtistDAO();
	}

	// Used for unit testing (Mockito / manual mock)
	public ArtistService(ArtistDAO artistDAO) {
		this.artistDAO = artistDAO;
	}

	// Search artists by name or bio
	public List<String> searchArtists(String keyword) {
		if (keyword == null) {
			keyword = "";
		}
		keyword = keyword.trim();
		if (keyword.length() == 0) {
			// optional: return empty list instead of all artists
			return new ArrayList<String>();
		}
		return artistDAO.searchArtists(keyword);
	}

}
