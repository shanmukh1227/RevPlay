package com.revplay.dao;

import com.revplay.db.DBConnection;
import com.revplay.model.ArtistProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistProfileDAO {

    public ArtistProfile getProfile(int artistId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql =
            "SELECT artist_id, bio, instagram, youtube, spotify " +
            "FROM artist_profile " +
            "WHERE artist_id=?";

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, artistId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return new ArtistProfile(
                        rs.getInt("artist_id"),
                        rs.getString("bio"),
                        rs.getString("instagram"),
                        rs.getString("youtube"),
                        rs.getString("spotify")
                );
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
        return null;
    }

    public boolean upsertProfile(ArtistProfile p) {

        Connection con = null;
        PreparedStatement ps = null;

        String sql =
            "MERGE INTO artist_profile ap " +
            "USING ( " +
            "   SELECT ? AS artist_id, ? AS bio, ? AS instagram, " +
            "          ? AS youtube, ? AS spotify " +
            "   FROM dual " +
            ") src " +
            "ON (ap.artist_id = src.artist_id) " +
            "WHEN MATCHED THEN UPDATE SET " +
            "   ap.bio = src.bio, " +
            "   ap.instagram = src.instagram, " +
            "   ap.youtube = src.youtube, " +
            "   ap.spotify = src.spotify " +
            "WHEN NOT MATCHED THEN INSERT " +
            "   (artist_id, bio, instagram, youtube, spotify) " +
            "VALUES " +
            "   (src.artist_id, src.bio, src.instagram, src.youtube, src.spotify)";

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, p.getArtistId());
            ps.setString(2, p.getBio());
            ps.setString(3, p.getInstagram());
            ps.setString(4, p.getYoutube());
            ps.setString(5, p.getSpotify());

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
}
