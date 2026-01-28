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

    /* ================= PLAY SONG ================= */

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

    /* ================= PLAYLIST ================= */

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

                case 3: // OPEN
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

                case 4: // ✅ ADD SONG (MISSING PART)
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


    /* ===== Stub methods (unchanged) ===== */

    private void searchSongsFlow() {}
    private void browseByGenreFlow() {}
    private void favoritesMenu() {}
    private void recentlyPlayedFlow() {}
    private void publicPlaylistsMenu() {}
}
