package com.revplay.dao;

import com.revplay.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {

    public List<String> searchArtists(String keyword) {

        String sql =
            "SELECT u.user_id, u.name, ap.genre, ap.bio " +
            "FROM users u " +
            "LEFT JOIN artist_profile ap ON u.user_id = ap.artist_id " +
            "WHERE u.role = 'ARTIST' " +
            "AND (LOWER(u.name) LIKE ? " +
            "OR LOWER(ap.genre) LIKE ? " +
            "OR LOWER(ap.bio) LIKE ?) " +
            "ORDER BY u.name";

        List<String> list = new ArrayList<String>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String like = "%" + keyword.toLowerCase() + "%";

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            rs = ps.executeQuery();

            while (rs.next()) {
                String row =
                        rs.getInt("user_id")
                        + " | "
                        + rs.getString("name")
                        + " | Genre: "
                        + rs.getString("genre")
                        + " | Bio: "
                        + rs.getString("bio");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return list;
    }
}
