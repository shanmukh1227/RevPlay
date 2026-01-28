package com.revplay.app;

import com.revplay.model.Song;
import com.revplay.model.User;
import com.revplay.service.PlaylistService;
import com.revplay.service.SongService;

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
            System.out.println("Welcome: " + user.getName()
                    + " (ID: " + user.getUserId() + ")");
            System.out.println("1. Search Songs");
            System.out.println("2. Play Song (with controls)");
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
        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine().trim();

        List<Song> songs = songService.searchSongs(keyword);
        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        System.out.println("\n--- SEARCH RESULTS ---");
        for (Song s : songs) {
            System.out.println(
                    s.getSongId() + " | " + s.getTitle()
                            + " | " + s.getGenre()
                            + " | " + s.getDurationSeconds() + " sec"
                            + " | Artist: " + s.getArtistName()
            );
        }
    }

    private void playSongFlow() {
        System.out.print("Enter Song ID: ");
        int songId;

        try {
            songId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid song id!");
            return;
        }

        String title = songService.getSongTitleById(songId);
        if (title == null) {
            System.out.println("Song not found!");
            return;
        }

        boolean ok = songService.playSong(user.getUserId(), songId);
        if (!ok) {
            System.out.println("Failed to play song.");
            return;
        }

        System.out.println("▶ Now Playing: " + title);
        playerControls(songId, title);
    }

    private void playerControls(int currentSongId, String currentTitle) {
        boolean paused = false;
        boolean repeat = false;

        while (true) {
            System.out.println("\n--- PLAYER CONTROLS ---");
            System.out.println("Now: " + currentTitle
                    + " (Song ID: " + currentSongId + ")");
            System.out.println("1. " + (paused ? "Resume" : "Pause"));
            System.out.println("2. Next (simulate)");
            System.out.println("3. Previous (simulate)");
            System.out.println("4. Repeat: " + (repeat ? "ON" : "OFF"));
            System.out.println("0. Stop Player");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine(); 

            switch (ch) {

                case 1:
                    paused = !paused;
                    if (paused) {
                        System.out.println("Paused...");
                    } else {
                        System.out.println("Resumed...");
                    }
                    break;

                case 2:
                    System.out.println("Skipped to next song (simulation).");
                    break;

                case 3:
                    System.out.println("Went to previous song (simulation).");
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

    private void browseByGenreFlow() {
        System.out.print("Enter Genre: ");
        String genre = sc.nextLine().trim();

        List<Song> songs = songService.browseSongsByGenre(genre);
        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        System.out.println("\n--- SONGS IN GENRE: " + genre + " ---");
        for (Song s : songs) {
            System.out.println(
                    s.getSongId() + " | " + s.getTitle()
                            + " | " + s.getGenre()
                            + " | " + s.getDurationSeconds() + " sec"
                            + " | Artist: " + s.getArtistName()
            );
        }
    }

    private void favoritesMenu() {
        while (true) {
            System.out.println("\n--- FAVORITES ---");
            System.out.println("1. View");
            System.out.println("2. Add");
            System.out.println("3. Remove");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1:
                    List<String> favs = songService.viewFavorites(user.getUserId());

                    if (favs == null || favs.isEmpty()) {
                        System.out.println("No favorites yet.");
                    } else {
                        for (String f : favs) {
                            System.out.println(f);
                        }
                    }
                    break;

                case 2:
                    System.out.print("Song ID: ");
                    int sidAdd = sc.nextInt();
                    sc.nextLine(); // consume newline

                    boolean okAdd =
                            songService.addToFavorites(user.getUserId(), sidAdd);

                    if (okAdd) {
                        System.out.println("Added to favorites");
                    } else {
                        System.out.println("Failed / already added");
                    }
                    break;

                case 3:
                    System.out.print("Song ID: ");
                    int sidRem = sc.nextInt();
                    sc.nextLine(); // consume newline

                    boolean okRem =
                            songService.removeFromFavorites(user.getUserId(), sidRem);

                    if (okRem) {
                        System.out.println("Removed from favorites");
                    } else {
                        System.out.println("Not found");
                    }
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
        for (String s : list) {
            System.out.println(s);
        }
    }

    private void playlistsMenu() {
        while (true) {
            System.out.println("\n--- MY PLAYLISTS ---");
            System.out.println("1. View");
            System.out.println("2. Create");
            System.out.println("3. Open");
            System.out.println("4. Add Song");
            System.out.println("5. Remove Song");
            System.out.println("6. Update");
            System.out.println("7. Delete");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine(); // consume newline

            if (ch == 0) {
                return;
            }

            switch (ch) {

                case 1:
                    List<String> pls =
                            playlistService.viewMyPlaylists(user.getUserId());

                    if (pls == null || pls.isEmpty()) {
                        System.out.println("No playlists yet.");
                    } else {
                        for (String pl : pls) {
                            System.out.println(pl);
                        }
                    }
                    break;

                case 2:
                    System.out.print("Name: ");
                    String name = sc.nextLine();

                    System.out.print("Desc: ");
                    String desc = sc.nextLine();

                    System.out.print("Privacy (PUBLIC/PRIVATE): ");
                    String p = sc.nextLine().trim().toUpperCase();

                    int newId =
                            playlistService.createPlaylist(
                                    user.getUserId(),
                                    name,
                                    desc,
                                    p
                            );

                    if (newId > 0) {
                        System.out.println("Playlist created! ID = " + newId);
                    } else {
                        System.out.println("Failed to create playlist");
                    }
                    break;

                case 3:
                    System.out.print("Playlist ID: ");
                    int pidView = sc.nextInt();
                    sc.nextLine();

                    List<String> songs =
                            playlistService.viewPlaylistSongs(pidView);

                    if (songs == null || songs.isEmpty()) {
                        System.out.println("No songs / invalid playlist id.");
                    } else {
                        for (String s : songs) {
                            System.out.println(s);
                        }
                    }
                    break;

                case 4:
                    System.out.print("Playlist ID: ");
                    int pidAdd = sc.nextInt();

                    System.out.print("Song ID: ");
                    int sidAdd = sc.nextInt();
                    sc.nextLine();

                    boolean ok =
                            playlistService.addSongToPlaylist(pidAdd, sidAdd);

                    if (ok) {
                        System.out.println("Song added");
                    } else {
                        System.out.println("Failed / already exists");
                    }
                    break;

                case 5:
                    System.out.print("Playlist ID: ");
                    int pidRem = sc.nextInt();

                    System.out.print("Song ID: ");
                    int sidRem = sc.nextInt();
                    sc.nextLine();

                    boolean okR =
                            playlistService.removeSongFromPlaylist(pidRem, sidRem);

                    if (okR) {
                        System.out.println("Song removed");
                    } else {
                        System.out.println("Not found");
                    }
                    break;

                case 6:
                    System.out.print("Playlist ID: ");
                    int pidUpd = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New Name: ");
                    String newName = sc.nextLine();

                    System.out.print("Desc: ");
                    String newDesc = sc.nextLine();

                    System.out.print("Privacy (PUBLIC/PRIVATE): ");
                    String priv = sc.nextLine().trim().toUpperCase();

                    boolean okU =
                            playlistService.updatePlaylist(
                                    pidUpd,
                                    user.getUserId(),
                                    newName,
                                    newDesc,
                                    priv
                            );

                    if (okU) {
                        System.out.println("Playlist updated");
                    } else {
                        System.out.println("Failed / not your playlist");
                    }
                    break;

                case 7:
                    System.out.print("Playlist ID: ");
                    int pidDel = sc.nextInt();
                    sc.nextLine();

                    boolean okD =
                            playlistService.deletePlaylist(
                                    pidDel,
                                    user.getUserId());

                    if (okD) {
                        System.out.println("Playlist deleted");
                    } else {
                        System.out.println("Failed / not your playlist");
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
            System.out.println("1. View Public");
            System.out.println("2. Open");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine(); // consume newline

            if (ch == 0) {
                return;
            }

            switch (ch) {

                case 1:
                    List<String> list =
                            playlistService.viewPublicPlaylists(
                                    user.getUserId());

                    if (list == null || list.isEmpty()) {
                        System.out.println("No public playlists.");
                    } else {
                        for (String s : list) {
                            System.out.println(s);
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
                        System.out.println("No songs / invalid playlist id.");
                    } else {
                        for (String s : songs) {
                            System.out.println(s);
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid option");
            }

        }
    }
}
