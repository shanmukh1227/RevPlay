package com.revplay.app;

import com.revplay.dao.AlbumDAO;
import com.revplay.dao.ArtistProfileDAO;
import com.revplay.model.ArtistProfile;
import com.revplay.model.User;
import com.revplay.service.AlbumService;
import com.revplay.service.SongService;

import java.util.List;
import java.util.Scanner;

public class ArtistMenu {

	private User artist;
	private Scanner sc;

	private SongService songService;
	private AlbumDAO albumDAO;
	private ArtistProfileDAO profileDAO;

	public ArtistMenu(User artist) {
		this.artist = artist;
		this.sc = new Scanner(System.in);
		this.songService = new SongService();
		this.albumDAO = new AlbumDAO();
		this.profileDAO = new ArtistProfileDAO();
	}

	public void show() {
		while (true) {
			System.out.println("\n----- ARTIST MENU------");
			System.out.println("Welcome: " + artist.getName() + " (Artist ID: "
					+ artist.getUserId() + ")");
			System.out.println("1. Upload Song");
			System.out.println("2. View My Songs");
			System.out.println("3. Create Album");
			System.out.println("4. View My Albums");
			System.out.println("5. Song Statistics (Top Plays)");
			System.out.println("6. Artist Profile (View/Update)");
			System.out.println("7. Add Song to Album");
			System.out.println("8. View Album Songs");

			System.out.println("0. Logout");

			System.out.print("Choose: ");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				uploadSongFlow();
				break;
			case 2:
				viewMySongsFlow();
				break;
			case 3:
				createAlbumFlow();
				break;
			case 4:
				viewMyAlbumsFlow();
				break;
			case 5:
				statsFlow();
				break;
			case 6:
				profileFlow();
				break;
			case 7:
				addSongToAlbumFlow();
				break;
			case 8:
				viewAlbumSongsFlow();
				break;

			case 0:
				System.out.println("Logged out.");
				return;
			default:
				System.out.println("Invalid option");
			}
		}
	}

	private void createAlbumFlow() {

		System.out.println("\n--- Create Album ---");

		System.out.print("Album name: ");
		String name = sc.nextLine();

		System.out.print("Genre: ");
		String genre = sc.nextLine();

		System.out.print("Release date (yyyy-mm-dd) OR blank: ");
		String releaseDate = sc.nextLine();

		AlbumService albumService = new AlbumService();

		int albumId = albumService.createAlbum(artist.getUserId(), name, genre,
				releaseDate);

		if (albumId > 0) {
			System.out.println("Album created successfully!");
			System.out.println("Album ID : " + albumId);
		} else {
			System.out.println("Failed to create album.");
		}
	}

	private void addSongToAlbumFlow() {

		System.out.println("\n--- ADD SONG TO ALBUM ---");

		System.out.print("Album ID: ");
		int albumId;
		try {
			albumId = Integer.parseInt(sc.nextLine().trim());
		} catch (Exception e) {
			System.out.println("Invalid album id");
			return;
		}

		System.out.print("Song ID: ");
		int songId;
		try {
			songId = Integer.parseInt(sc.nextLine().trim());
		} catch (Exception e) {
			System.out.println("Invalid song id");
			return;
		}

		AlbumService albumService = new AlbumService();

		boolean ok = albumService.addSongToAlbum(albumId, songId);

		if (ok) {
			System.out.println("Song added to album successfully!");
		} else {
			System.out
					.println("Failed (song may already be in album or invalid IDs)");
		}
	}

	private void uploadSongFlow() {

		System.out.println("\n--- Upload Song ---");

		System.out.print("Title: ");
		String title = sc.nextLine();

		System.out.print("Genre: ");
		String genre = sc.nextLine();

		System.out.print("Duration seconds: ");
		int duration;
		try {
			duration = Integer.parseInt(sc.nextLine().trim());
		} catch (Exception e) {
			System.out.println("Invalid duration");
			return;
		}

		System.out.print("Release date (yyyy-mm-dd) OR blank: ");
		String releaseDate = sc.nextLine();

		int songId = songService.uploadSong(title, genre, duration,
				releaseDate, artist.getUserId());

		if (songId > 0) {
			System.out.println("Song uploaded successfully!");
			System.out.println("Song ID : " + songId);
		} else {
			System.out.println("Upload failed!");
		}
	}

	private void viewMySongsFlow() {
		List<String> songs = songService.viewMySongs(artist.getUserId());

		if (songs == null || songs.isEmpty()) {
			System.out.println("No songs uploaded yet.");
			return;
		}

		System.out.println("\n--- My Songs ---");
		for (String s : songs) {
			System.out.println(s);
		}
	}

	private void viewMyAlbumsFlow() {
		List<String> albums = albumDAO.viewMyAlbums(artist.getUserId());

		if (albums == null || albums.isEmpty()) {
			System.out.println("No albums created yet.");
			return;
		}

		System.out.println("\n--- My Albums ---");
		for (String a : albums) {
			System.out.println(a);
		}
	}

	private void statsFlow() {
		List<String> top = songService
				.getTopPlayedSongs(artist.getUserId(), 10);

		if (top == null || top.isEmpty()) {
			System.out.println("No songs found for stats.");
			return;
		}

		System.out.println("\n--- Top Played Songs ---");
		for (String s : top) {
			System.out.println(s);
		}
	}

	private void viewAlbumSongsFlow() {

		System.out.println("\n--- VIEW ALBUM SONGS ---");

		System.out.print("Enter Album ID: ");
		int albumId;

		try {
			albumId = Integer.parseInt(sc.nextLine().trim());
		} catch (Exception e) {
			System.out.println("Invalid album id");
			return;
		}

		AlbumService albumService = new AlbumService();

		List<String> songs = albumService.viewAlbumSongs(albumId);

		if (songs == null || songs.isEmpty()) {
			System.out.println("No songs found in this album.");
			return;
		}

		System.out.println("\nSongs in Album:");
		for (String s : songs) {
			System.out.println(s);
		}
	}

	private void profileFlow() {
	    while (true) {
	        System.out.println("\n--- ARTIST PROFILE ---");
	        System.out.println("1. View Profile");
	        System.out.println("2. Update Profile");
	        System.out.println("0. Back");
	        System.out.print("Choose: ");

	        int ch = sc.nextInt();
	        sc.nextLine();

	        switch (ch) {

	        case 1:
	            ArtistProfile p = profileDAO.getProfile(artist.getUserId());

	            if (p == null) {
	                System.out.println("No profile yet. Update to create.");
	            } else {
	                System.out.println("Bio: " + p.getBio());
	                System.out.println("Instagram: " + p.getInstagram());
	                System.out.println("YouTube: " + p.getYoutube());
	                System.out.println("Spotify: " + p.getSpotify());
	            }
	            break;

	        case 2:
	            System.out.print("Bio: ");
	            String bio = sc.nextLine();

	            System.out.print("Instagram link: ");
	            String insta = sc.nextLine();

	            System.out.print("YouTube link: ");
	            String yt = sc.nextLine();

	            System.out.print("Spotify link: ");
	            String sp = sc.nextLine();

	            ArtistProfile profile = new ArtistProfile(
	                    artist.getUserId(),
	                    bio,
	                    insta,
	                    yt,
	                    sp
	            );

	            boolean ok = profileDAO.upsertProfile(profile);

	            if (ok) {
	                System.out.println("Profile saved");
	            } else {
	                System.out.println("Failed");
	            }
	            break;

	        case 0:
	            return;

	        default:
	            System.out.println("Invalid option");
	        }
	    }
	}

}
