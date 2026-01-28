package com.revplay.dao;

import com.revplay.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

    public boolean createAlbum(int artistId,
                               String albumName,
                               String genre,
                               String releaseDate) {

        Connection con = null;
        PreparedStatement ps = null;

        String sql =
            "INSERT INTO albums(album_name, genre, release_date, artist_id) " +
            "VALUES(?,?,?,?)";

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, albumName);
            ps.setString(2, genre);

            if (releaseDate == null || releaseDate.trim().length() == 0) {
                ps.setNull(3, java.sql.Types.DATE);
            } else {
                ps.setDate(3, java.sql.Date.valueOf(releaseDate));
            }

            ps.setInt(4, artistId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> viewMyAlbums(int artistId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql =
            "SELECT album_id, album_name, genre, release_date, created_at " +
            "FROM albums " +
            "WHERE artist_id=? " +
            "ORDER BY created_at DESC";

        List<String> list = new ArrayList<String>();

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, artistId);

            rs = ps.executeQuery();
            while (rs.next()) {
                String row =
                        rs.getInt("album_id")
                                + " | "
                                + rs.getString("album_name")
                                + " | "
                                + rs.getString("genre")
                                + " | "
                                + rs.getString("release_date")
                                + " | "
                                + rs.getString("created_at");
                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
