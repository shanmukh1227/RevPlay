package com.revplay.dao;

import com.revplay.db.DBConnection;
import com.revplay.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {

	public List<Song> searchSongs(String keyword) {

		String sql = "SELECT s.song_id, s.title, s.genre, s.duration_seconds, u.name AS artist_name "
				+ "FROM songs s "
				+ "JOIN users u ON s.artist_id = u.user_id "
				+ "WHERE s.title LIKE ? OR s.genre LIKE ? OR u.name LIKE ? "
				+ "ORDER BY s.title";

		List<Song> results = new ArrayList<Song>();
		String like = "%" + keyword + "%";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);

			ps.setString(1, like);
			ps.setString(2, like);
			ps.setString(3, like);

			rs = ps.executeQuery();
			while (rs.next()) {
				results.add(new Song(rs.getInt("song_id"), rs
						.getString("title"), rs.getString("genre"), rs
						.getInt("duration_seconds"), rs
						.getString("artist_name")));
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

		return results;
	}

	public String getSongTitleById(int songId) {

		String sql = "SELECT title FROM songs WHERE song_id=?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, songId);

			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("title");
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

		return null;
	}

	public boolean playSong(int userId, int songId) {

		String insertHistory = "INSERT INTO listening_history(user_id, song_id) VALUES(?, ?)";
		String updateCount = "UPDATE songs SET play_count = play_count + 1 WHERE song_id=?";

		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;

		try {
			con = DBConnection.getConnection();

			ps1 = con.prepareStatement(insertHistory);
			ps1.setInt(1, userId);
			ps1.setInt(2, songId);
			ps1.executeUpdate();

			ps2 = con.prepareStatement(updateCount);
			ps2.setInt(1, songId);
			ps2.executeUpdate();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		} finally {
			try {
				if (ps1 != null)
					ps1.close();
				if (ps2 != null)
					ps2.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> getRecentlyPlayed(int userId, int limit) {

		String sql = "SELECT * FROM ( "
				+ "   SELECT s.title, u.name AS artist_name, lh.played_at "
				+ "   FROM listening_history lh "
				+ "   JOIN songs s ON lh.song_id = s.song_id "
				+ "   JOIN users u ON s.artist_id = u.user_id "
				+ "   WHERE lh.user_id = ? " + "   ORDER BY lh.played_at DESC "
				+ ") WHERE ROWNUM <= ?";

		List<String> list = new ArrayList<String>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, limit);

			rs = ps.executeQuery();
			while (rs.next()) {
				String row = rs.getString("title") + " | Artist: "
						+ rs.getString("artist_name") + " | Played at: "
						+ rs.getString("played_at");
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

	public boolean addToFavorites(int userId, int songId) {

		String sql = "INSERT INTO favorites(user_id, song_id) VALUES(?, ?)";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, songId);
			ps.executeUpdate();
			return true;

		} catch (SQLException e) {
			System.out.println("Already in favorites or invalid song id.");
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

	public boolean removeFromFavorites(int userId, int songId) {

		String sql = "DELETE FROM favorites WHERE user_id=? AND song_id=?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
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

	public List<String> viewFavorites(int userId) {

		String sql = "SELECT s.song_id, s.title, u.name AS artist_name, s.genre "
				+ "FROM favorites f "
				+ "JOIN songs s ON f.song_id = s.song_id "
				+ "JOIN users u ON s.artist_id = u.user_id "
				+ "WHERE f.user_id=? " + "ORDER BY s.title";

		List<String> list = new ArrayList<String>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);

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

	public int uploadSong(String title, String genre, int durationSeconds,
			String releaseDate, int artistId) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String insertSql = "INSERT INTO songs(song_id, title, genre, duration_seconds, release_date, artist_id) "
				+ "VALUES (songs_seq.NEXTVAL, ?, ?, ?, ?, ?)";

		String idSql = "SELECT songs_seq.CURRVAL FROM dual";

		try {
			con = DBConnection.getConnection();

			ps = con.prepareStatement(insertSql);
			ps.setString(1, title);
			ps.setString(2, genre);
			ps.setInt(3, durationSeconds);

			if (releaseDate == null || releaseDate.trim().length() == 0) {
				ps.setNull(4, java.sql.Types.DATE);
			} else {
				ps.setDate(4, java.sql.Date.valueOf(releaseDate));
			}

			ps.setInt(5, artistId);
			ps.executeUpdate();
			ps.close();

			ps = con.prepareStatement(idSql);
			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1); // song_id
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

	public List<String> viewMySongs(int artistId) {

		String sql = "SELECT song_id, title, genre, duration_seconds, play_count, created_at "
				+ "FROM songs "
				+ "WHERE artist_id=? "
				+ "ORDER BY created_at DESC";

		List<String> list = new ArrayList<String>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, artistId);

			rs = ps.executeQuery();
			while (rs.next()) {
				String row = rs.getInt("song_id") + " | "
						+ rs.getString("title") + " | " + rs.getString("genre")
						+ " | " + rs.getInt("duration_seconds")
						+ " sec | Plays: " + rs.getInt("play_count") + " | "
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

	public List<String> getTopPlayedSongs(int artistId, int limit) {

		String sql = "SELECT * FROM ( "
				+ "   SELECT song_id, title, play_count " + "   FROM songs "
				+ "   WHERE artist_id=? "
				+ "   ORDER BY play_count DESC, title " + ") WHERE ROWNUM <= ?";

		List<String> list = new ArrayList<String>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, artistId);
			ps.setInt(2, limit);

			rs = ps.executeQuery();
			while (rs.next()) {
				String row = rs.getInt("song_id") + " | "
						+ rs.getString("title") + " | Plays: "
						+ rs.getInt("play_count");
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

	public List<Song> browseSongsByGenre(String genre) {

		String sql = "SELECT s.song_id, s.title, s.genre, s.duration_seconds, u.name AS artist_name "
				+ "FROM songs s "
				+ "JOIN users u ON s.artist_id = u.user_id "
				+ "WHERE s.genre=? " + "ORDER BY s.title";

		List<Song> results = new ArrayList<Song>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, genre);

			rs = ps.executeQuery();
			while (rs.next()) {
				results.add(new Song(rs.getInt("song_id"), rs
						.getString("title"), rs.getString("genre"), rs
						.getInt("duration_seconds"), rs
						.getString("artist_name")));
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

		return results;
	}
}
