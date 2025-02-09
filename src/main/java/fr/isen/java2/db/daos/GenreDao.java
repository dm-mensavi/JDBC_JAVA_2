package fr.isen.java2.db.daos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {
		List<Genre> genres = new ArrayList<>();
		String sql = "SELECT * FROM genre";
		try (Connection connection = DataSourceFactory.getDataSource().getConnection();
			 Statement stmt = connection.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				Genre genre = new Genre(rs.getInt("idgenre"), rs.getString("name"));
				genres.add(genre);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return genres;
	}

	public Genre getGenre(String name) {
		Genre genre = null;
		String sql = "SELECT * FROM genre WHERE name = ?";
		try (Connection connection = DataSourceFactory.getDataSource().getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, name);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					genre = new Genre(rs.getInt("idgenre"), rs.getString("name"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return genre;
	}

	public void addGenre(String name) {
		String sql = "INSERT INTO genre (name) VALUES (?)";
		try (Connection connection = DataSourceFactory.getDataSource().getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
