package com.revplay.service;

import com.revplay.dao.AlbumDAO;

import java.util.ArrayList;
import java.util.List;

public class AlbumService {

    private AlbumDAO albumDAO;

    public AlbumService() {
        this.albumDAO = new AlbumDAO();
    }
     
    public int createAlbum(int artistId,
            String name,
            String genre,
            String releaseDate) {

        if (artistId <= 0) return -1;
        if (name == null || name.trim().isEmpty()) return -1;

         return albumDAO.createAlbum(
         artistId,
        name.trim(),
         genre == null ? "" : genre.trim(),
         releaseDate
         );
       }

    public boolean addSongToAlbum(int albumId, int songId) {
        if (albumId <= 0 || songId <= 0) {
            return false;
        }
        return albumDAO.addSongToAlbum(albumId, songId);
    }

    public List<String> viewAlbumSongs(int albumId) {
        if (albumId <= 0) {
            return new ArrayList<String>();
        }
        return albumDAO.viewAlbumSongs(albumId);
    }
}
