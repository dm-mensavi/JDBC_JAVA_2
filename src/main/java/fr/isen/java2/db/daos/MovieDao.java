package fr.isen.java2.db.daos;

import java.util.List;

import fr.isen.java2.db.entities.Movie;
import fr.isen.java2.db.entities.Genre;

import java.sql.*;
import java.util.ArrayList;

/**
 * MovieDao is a Data Access Object class that provides methods to interact with the movie table in the database.
 */
public class MovieDao {

	/**
	 * Lists all movies in the database.
	 *
	 * @return a list of Movie objects
	 */
	public List<Movie> listMovies() {
		List<Movie> movies = new ArrayList<>();
		String sql = "SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre";
		try (Connection connection = DataSourceFactory.getConnection();
			 Statement stmt = connection.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				Genre genre = new Genre(rs.getInt("idgenre"), rs.getString("name"));
				Movie movie = new Movie(rs.getInt("idmovie"), rs.getString("title"), rs.getDate("release_date").toLocalDate(),
						genre, rs.getInt("duration"), rs.getString("director"), rs.getString("summary"));
				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}

	/**
	 * Lists all movies of a specific genre.
	 *
	 * @param genreName the name of the genre
	 * @return a list of Movie objects
	 */
	public List<Movie> listMoviesByGenre(String genreName) {
		List<Movie> movies = new ArrayList<>();
		String sql = "SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre WHERE genre.name = ?";
		try (Connection connection = DataSourceFactory.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, genreName);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Genre genre = new Genre(rs.getInt("idgenre"), rs.getString("name"));
					Movie movie = new Movie(rs.getInt("idmovie"), rs.getString("title"), rs.getDate("release_date").toLocalDate(),
							genre, rs.getInt("duration"), rs.getString("director"), rs.getString("summary"));
					movies.add(movie);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}

	/**
	 * Adds a new movie to the database.
	 *
	 * @param movie the Movie object to add
	 * @return the added Movie object
	 */
	public Movie addMovie(Movie movie) {
		String sql = "INSERT INTO movie (title, release_date, genre_id, duration, director, summary) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = DataSourceFactory.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, movie.getTitle());
			pstmt.setDate(2, Date.valueOf(movie.getReleaseDate()));
			pstmt.setInt(3, movie.getGenre().getId());
			pstmt.setInt(4, movie.getDuration());
			pstmt.setString(5, movie.getDirector());
			pstmt.setString(6, movie.getSummary());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return movie;
    }
}
