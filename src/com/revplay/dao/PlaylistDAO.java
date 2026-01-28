package com.revplay.dao;

import com.revplay.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

	public int createPlaylist(int userId, String name, String desc,
			String privacy) {

		String insertSql = "INSERT INTO playlists "
				+ "(playlist_id, playlist_name, description, privacy, user_id) "
				+ "VALUES (playlists_seq.NEXTVAL, ?, ?, ?, ?)";

		String selectSql = "SELECT playlists_seq.CURRVAL FROM dual";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();

			ps = con.prepareStatement(insertSql);
			ps.setString(1, name);
			ps.setString(2, desc);
			ps.setString(3, privacy);
			ps.setInt(4, userId);
			ps.executeUpdate();
			ps.close();

			ps = con.prepareStatement(selectSql);
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

	public List<String> viewMyPlaylists(int userId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT playlist_id, playlist_name, privacy, created_at "
				+ "FROM playlists " + "WHERE user_id=? "
				+ "ORDER BY created_at DESC";

		List<String> list = new ArrayList<String>();

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);

			rs = ps.executeQuery();
			while (rs.next()) {
				String row = rs.getInt("playlist_id") + " | "
						+ rs.getString("playlist_name") + " | "
						+ rs.getString("privacy") + " | "
						+ rs.getString("created_at");
				list.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public boolean addSongToPlaylist(int playlistId, int songId) {

		Connection con = null;
		PreparedStatement ps = null;

		String sql = "INSERT INTO playlist_songs(playlist_id, song_id) VALUES(?, ?)";

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, playlistId);
			ps.setInt(2, songId);
			ps.executeUpdate();
			return true;

		} catch (SQLException e) {
			System.out
					.println("Song already in playlist OR invalid playlist/song id.");
			return false;

		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean removeSongFromPlaylist(int playlistId, int songId) {

		Connection con = null;
		PreparedStatement ps = null;

		String sql = "DELETE FROM playlist_songs WHERE playlist_id=? AND song_id=?";

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, playlistId);
			ps.setInt(2, songId);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> viewPlaylistSongs(int playlistId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT s.song_id, s.title, u.name AS artist_name, s.genre "
				+ "FROM playlist_songs ps "
				+ "JOIN songs s ON ps.song_id = s.song_id "
				+ "JOIN users u ON s.artist_id = u.user_id "
				+ "WHERE ps.playlist_id=? " + "ORDER BY s.title";

		List<String> list = new ArrayList<String>();

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, playlistId);

			rs = ps.executeQuery();
			while (rs.next()) {
				String row = rs.getInt("song_id") + " | "
						+ rs.getString("title") + " | " + rs.getString("genre")
						+ " | Artist: " + rs.getString("artist_name");
				list.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public boolean updatePlaylist(int playlistId, int userId, String newName,
			String newDesc, String privacy) {

		Connection con = null;
		PreparedStatement ps = null;

		String sql = "UPDATE playlists SET playlist_name=?, description=?, privacy=? "
				+ "WHERE playlist_id=? AND user_id=?";

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);

			ps.setString(1, newName);
			ps.setString(2, newDesc);
			ps.setString(3, privacy);
			ps.setInt(4, playlistId);
			ps.setInt(5, userId);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean deletePlaylist(int playlistId, int userId) {

		Connection con = null;
		PreparedStatement ps = null;

		String sql = "DELETE FROM playlists WHERE playlist_id=? AND user_id=?";

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, playlistId);
			ps.setInt(2, userId);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> viewPublicPlaylists(int currentUserId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT p.playlist_id, p.playlist_name, u.name AS owner_name, p.created_at "
				+ "FROM playlists p "
				+ "JOIN users u ON p.user_id = u.user_id "
				+ "WHERE p.privacy='PUBLIC' AND p.user_id <> ? "
				+ "ORDER BY p.created_at DESC";

		List<String> list = new ArrayList<String>();

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, currentUserId);

			rs = ps.executeQuery();
			while (rs.next()) {
				String row = rs.getInt("playlist_id") + " | "
						+ rs.getString("playlist_name") + " | Owner: "
						+ rs.getString("owner_name") + " | "
						+ rs.getString("created_at");
				list.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public List<String> viewPlaylistSongsPublic(int playlistId) {
		return viewPlaylistSongs(playlistId);
	}
}
