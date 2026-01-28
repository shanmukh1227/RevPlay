package com.revplay.model;

public class ArtistProfile {

    private int artistId;
    private String bio;
    private String genre;
    private String instagram;
    private String youtube;
    private String spotify;

    public ArtistProfile(int artistId,
                         String bio,
                         String genre,
                         String instagram,
                         String youtube,
                         String spotify) {
        this.artistId = artistId;
        this.bio = bio;
        this.genre = genre;
        this.instagram = instagram;
        this.youtube = youtube;
        this.spotify = spotify;
    }

    public int getArtistId() {
        return artistId;
    }

    public String getBio() {
        return bio;
    }

    public String getGenre() {
        return genre;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getSpotify() {
        return spotify;
    }
}
