package com.revplay.dao;

import com.revplay.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

	public int createAlbum(int artistId, String albumName, String genre,
			String releaseDate) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String insertSql = "INSERT INTO albums(album_id, album_name, genre, release_date, artist_id) "
				+ "VALUES (albums_seq.NEXTVAL, ?, ?, ?, ?)";

		String idSql = "SELECT albums_seq.CURRVAL FROM dual";

		try {
			con = DBConnection.getConnection();

			ps = con.prepareStatement(insertSql);
			ps.setString(1, albumName);
			ps.setString(2, genre);

			if (releaseDate == null || releaseDate.trim().length() == 0) {
				ps.setNull(3, java.sql.Types.DATE);
			} else {
				ps.setDate(3, java.sql.Date.valueOf(releaseDate));
			}

			ps.setInt(4, artistId);
			ps.executeUpdate();
			ps.close();

			ps = con.prepareStatement(idSql);
			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return -1;
	}

	public List<String> viewMyAlbums(int artistId) {

		List<String> list = new ArrayList<String>();

		String sql = "SELECT album_id, album_name, genre, release_date, created_at "
				+ "FROM albums WHERE artist_id=? ORDER BY created_at DESC";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, artistId);

			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt("album_id") + " | "
						+ rs.getString("album_name") + " | "
						+ rs.getString("genre") + " | "
						+ rs.getString("release_date"));
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return list;
	}

	public boolean addSongToAlbum(int albumId, int songId) {

		String sql = "INSERT INTO album_songs(album_id, song_id) VALUES(?, ?)";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, albumId);
			ps.setInt(2, songId);

			ps.executeUpdate();
			return true;

		} catch (Exception e) {
			System.out.println("Song already in album or invalid ID.");
			return false;

		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
	}

	public List<String> viewAlbumSongs(int albumId) {

		List<String> list = new ArrayList<String>();

		String sql = "SELECT s.song_id, s.title, s.genre "
				+ "FROM album_songs a "
				+ "JOIN songs s ON a.song_id = s.song_id "
				+ "WHERE a.album_id=? " + "ORDER BY s.title";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, albumId);

			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt("song_id") + " | " + rs.getString("title")
						+ " | " + rs.getString("genre"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return list;
	}

	public List<String> searchAlbums(String keyword) {

		List<String> list = new ArrayList<String>();

		String sql = "SELECT a.album_id, a.album_name, a.genre, u.name AS artist_name "
				+ "FROM albums a "
				+ "JOIN users u ON a.artist_id = u.user_id "
				+ "WHERE LOWER(a.album_name) LIKE ? "
				+ "OR LOWER(a.genre) LIKE ? "
				+ "OR LOWER(u.name) LIKE ? "
				+ "ORDER BY a.album_name";

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
				list.add(rs.getInt("album_id") + " | "
						+ rs.getString("album_name") + " | "
						+ rs.getString("genre") + " | Artist: "
						+ rs.getString("artist_name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return list;
	}
}
