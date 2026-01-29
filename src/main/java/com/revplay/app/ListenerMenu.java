package com.revplay.app;

import com.revplay.model.Song;
import com.revplay.model.User;
import com.revplay.service.PlaylistService;
import com.revplay.service.SongService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ListenerMenu {

    private User user;
    private Scanner sc;
    private SongService songService;
    private PlaylistService playlistService;

    public ListenerMenu(User user) {
        this.user = user;
        this.sc = new Scanner(System.in);
        this.songService = new SongService();
        this.playlistService = new PlaylistService();
    }

    public void show() {
        while (true) {
            System.out.println("\n------- LISTENER MENU -------");
            System.out.println("Welcome: " + user.getName() +
                    " (ID: " + user.getUserId() + ")");
            System.out.println("1. Search Songs");
            System.out.println("2. Play Song");
            System.out.println("3. Browse Songs by Genre");
            System.out.println("4. My Favorites");
            System.out.println("5. My Playlists");
            System.out.println("6. Recently Played");
            System.out.println("7. Public Playlists");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    searchSongsFlow();
                    break;
                case 2:
                    playSongFlow();
                    break;
                case 3:
                    browseByGenreFlow();
                    break;
                case 4:
                    favoritesMenu();
                    break;
                case 5:
                    playlistsMenu();
                    break;
                case 6:
                    recentlyPlayedFlow();
                    break;
                case 7:
                    publicPlaylistsMenu();
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
   
    
    private void searchSongsFlow() {

        System.out.print("Enter song name or keyword: ");
        String keyword = sc.nextLine().trim();

        List<Song> songs = songService.searchSongs(keyword);

        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        System.out.println("\n--- SEARCH RESULTS ---");
        for (Song s : songs) {
            System.out.println(
                s.getSongId() + " | " +
                s.getTitle() + " | " +
                s.getGenre() + " | " +
                s.getDurationSeconds() + " sec | Artist: " +
                s.getArtistName()
            );
        }
    }


 

    private void playSongFlow() {
        System.out.print("Enter Song ID: ");
        int songId;

        try {
            songId = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid song id!");
            return;
        }

        List<Song> songs = songService.searchSongs("");

        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs available.");
            return;
        }

        // Java 6 compatible anonymous comparator
        Collections.sort(songs, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getSongId() - b.getSongId();
            }
        });

        boolean exists = false;
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getSongId() == songId) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            System.out.println("Song not found!");
            return;
        }

        songService.playSong(user.getUserId(), songId);
        playerControls(songs, songId);
    }
     
    private void browseByGenreFlow() {

        System.out.print("Enter Genre: ");
        String genre = sc.nextLine().trim();

        List<Song> songs = songService.browseSongsByGenre(genre);

        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found for this genre.");
            return;
        }

        System.out.println("\n--- SONGS IN GENRE: " + genre + " ---");
        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            System.out.println(
                    s.getSongId() + " | " +
                    s.getTitle() + " | " +
                    s.getGenre() + " | " +
                    s.getDurationSeconds() + " sec | Artist: " +
                    s.getArtistName()
            );
        }
    }

    private void playerControls(List<Song> songs, int startSongId) {

        int index = 0;
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getSongId() == startSongId) {
                index = i;
                break;
            }
        }

        boolean paused = false;
        boolean repeat = false;

        while (true) {
            Song current = songs.get(index);

            System.out.println("\n--- PLAYER CONTROLS ---");
            System.out.println("Now: " + current.getTitle()
                    + " (Song ID: " + current.getSongId() + ")");
            System.out.println("1. " + (paused ? "Resume" : "Pause"));
            System.out.println("2. Next");
            System.out.println("3. Previous");
            System.out.println("4. Repeat: " + (repeat ? "ON" : "OFF"));
            System.out.println("0. Stop Player");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    paused = !paused;
                    System.out.println(paused ? "Paused..." : "Resumed...");
                    break;

                case 2:
                    if (repeat || index < songs.size() - 1) {
                        if (!repeat) {
                            index = index + 1;
                        }
                        System.out.println("Playing next song...");
                    } else {
                        System.out.println("Reached end of playlist.");
                    }
                    break;

                case 3:
                    if (repeat || index > 0) {
                        if (!repeat) {
                            index = index - 1;
                        }
                        System.out.println("Playing previous song...");
                    } else {
                        System.out.println("At first song.");
                    }
                    break;

                case 4:
                    repeat = !repeat;
                    System.out.println("Repeat is now: "
                            + (repeat ? "ON" : "OFF"));
                    break;

                case 0:
                    System.out.println("Player stopped.");
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
    
    private void favoritesMenu() {

        while (true) {
            System.out.println("\n--- FAVORITES ---");
            System.out.println("1. View Favorites");
            System.out.println("2. Add to Favorites");
            System.out.println("3. Remove from Favorites");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1:
                    List<String> favs =
                            songService.viewFavorites(user.getUserId());

                    if (favs == null || favs.isEmpty()) {
                        System.out.println("No favorites yet.");
                    } else {
                        for (int i = 0; i < favs.size(); i++) {
                            System.out.println(favs.get(i));
                        }
                    }
                    break;

                case 2:
                    System.out.print("Song ID: ");
                    int addId = sc.nextInt();
                    sc.nextLine();

                    boolean added =
                            songService.addToFavorites(user.getUserId(), addId);

                    System.out.println(added
                            ? "Added to favorites."
                            : "Already in favorites or invalid song.");
                    break;

                case 3:
                    System.out.print("Song ID: ");
                    int remId = sc.nextInt();
                    sc.nextLine();

                    boolean removed =
                            songService.removeFromFavorites(user.getUserId(), remId);

                    System.out.println(removed
                            ? "Removed from favorites."
                            : "Song not found in favorites.");
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
    
    private void recentlyPlayedFlow() {

        List<String> list =
                songService.getRecentlyPlayed(user.getUserId(), 10);

        if (list == null || list.isEmpty()) {
            System.out.println("No recently played songs.");
            return;
        }

        System.out.println("\n--- RECENTLY PLAYED ---");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }


 

    private void playlistsMenu() {
        while (true) {
            System.out.println("\n--- MY PLAYLISTS ---");
            System.out.println("1. View");
            System.out.println("2. Create");
            System.out.println("3. Open");
            System.out.println("4. Add Song");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine();

            if (ch == 0) {
                return;
            }

            switch (ch) {

                case 1: // VIEW
                    List<String> pls =
                            playlistService.viewMyPlaylists(user.getUserId());

                    if (pls == null || pls.isEmpty()) {
                        System.out.println("No playlists yet.");
                    } else {
                        for (int i = 0; i < pls.size(); i++) {
                            System.out.println(pls.get(i));
                        }
                    }
                    break;

                case 3: 
                    System.out.print("Playlist ID: ");
                    int pidView = sc.nextInt();
                    sc.nextLine();

                    List<String> songs =
                            playlistService.viewPlaylistSongs(pidView);

                    if (songs == null || songs.isEmpty()) {
                        System.out.println("Playlist exists but has no songs.");
                        System.out.println("Please add songs first.");
                    } else {
                        System.out.println("\n--- PLAYLIST SONGS ---");
                        for (int i = 0; i < songs.size(); i++) {
                            System.out.println(songs.get(i));
                        }
                    }
                    break;

                case 4: 
                    System.out.print("Playlist ID: ");
                    int pidAdd = sc.nextInt();

                    System.out.print("Song ID: ");
                    int sidAdd = sc.nextInt();
                    sc.nextLine();

                    boolean added =
                            playlistService.addSongToPlaylist(pidAdd, sidAdd);

                    if (added) {
                        System.out.println("Song added to playlist successfully!");
                    } else {
                        System.out.println("Failed to add song (already exists or invalid IDs).");
                    }
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
    private void publicPlaylistsMenu() {

        while (true) {
            System.out.println("\n--- PUBLIC PLAYLISTS ---");
            System.out.println("1. View Public Playlists");
            System.out.println("2. Open Playlist");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1:
                    List<String> list =
                            playlistService.viewPublicPlaylists(user.getUserId());

                    if (list == null || list.isEmpty()) {
                        System.out.println("No public playlists.");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(list.get(i));
                        }
                    }
                    break;

                case 2:
                    System.out.print("Playlist ID: ");
                    int pid = sc.nextInt();
                    sc.nextLine();

                    List<String> songs =
                            playlistService.viewPlaylistSongsPublic(pid);

                    if (songs == null || songs.isEmpty()) {
                        System.out.println("No songs or invalid playlist.");
                    } else {
                        System.out.println("\n--- PLAYLIST SONGS ---");
                        for (int i = 0; i < songs.size(); i++) {
                            System.out.println(songs.get(i));
                        }
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
