package com.revplay.model;

public class Song {

    private final int songId;
    private final String title;
    private final String genre;
    private final int durationSeconds;
    private final String artistName;

    public Song(int songId,
                String title,
                String genre,
                int durationSeconds,
                String artistName) {

        this.songId = songId;
        this.title = title;
        this.genre = genre;
        this.durationSeconds = durationSeconds;
        this.artistName = artistName;
    }

    public int getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getArtistName() {
        return artistName;
    }
}
