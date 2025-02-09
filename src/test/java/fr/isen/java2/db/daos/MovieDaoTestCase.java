package fr.isen.java2.db.daos;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.isen.java2.db.entities.Genre;
import fr.isen.java2.db.entities.Movie;

public class MovieDaoTestCase {

	private MovieDao movieDao = new MovieDao();

	@BeforeEach
	public void initDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS genre (idgenre INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , name VARCHAR(50) NOT NULL);");
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS movie (\r\n"
				+ "  idmovie INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + "  title VARCHAR(100) NOT NULL,\r\n"
				+ "  release_date DATETIME NULL,\r\n" + "  genre_id INT NOT NULL,\r\n" + "  duration INT NULL,\r\n"
				+ "  director VARCHAR(100) NOT NULL,\r\n" + "  summary MEDIUMTEXT NULL,\r\n"
				+ "  CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genre (idgenre));");
		stmt.executeUpdate("DELETE FROM movie");
		stmt.executeUpdate("DELETE FROM genre");
		stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='movie'");
		stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='genre'");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (1,'Drama')");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (2,'Comedy')");
		stmt.executeUpdate("INSERT INTO movie(idmovie,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (1, 'Title 1', '2015-11-26 12:00:00.000', 1, 120, 'director 1', 'summary of the first movie')");
		stmt.executeUpdate("INSERT INTO movie(idmovie,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (2, 'My Title 2', '2015-11-14 12:00:00.000', 2, 114, 'director 2', 'summary of the second movie')");
		stmt.executeUpdate("INSERT INTO movie(idmovie,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (3, 'Third title', '2015-12-12 12:00:00.000', 2, 176, 'director 3', 'summary of the third movie')");
		stmt.close();
		connection.close();
	}
	
	 @Test
	 public void shouldListMovies() {

		 // WHEN
		 List<Movie> movies = movieDao.listMovies();
		 // THEN
		 assertThat(movies).hasSize(3);
		 assertThat(movies).extracting("title", "director").containsOnly(
				 tuple("Title 1", "director 1"),
				 tuple("My Title 2", "director 2"),
				 tuple("Third title", "director 3")
		 );
	 }
	
	 @Test
	 public void shouldListMoviesByGenre() {
		 // WHEN
		 List<Movie> movies = movieDao.listMoviesByGenre("Comedy");
		 // THEN
		 assertThat(movies).hasSize(2);
		 assertThat(movies).extracting("title", "director").containsOnly(
				 tuple("My Title 2", "director 2"),
				 tuple("Third title", "director 3")
		 );
	 }
	
	 @Test
	 public void shouldAddMovie() throws Exception {
		 // GIVEN
		 Movie movie = new Movie("New Movie", LocalDate.of(2023, 10, 10), new Genre(1, "Drama"), 150, "New Director", "New Summary");
		 // WHEN
		 movieDao.addMovie(movie);
		 // THEN
		 Connection connection = DataSourceFactory.getDataSource().getConnection();
		 Statement statement = connection.createStatement();
		 ResultSet resultSet = statement.executeQuery("SELECT * FROM movie WHERE title='New Movie'");
		 assertThat(resultSet.next()).isTrue();
		 assertThat(resultSet.getInt("idmovie")).isNotNull();
		 assertThat(resultSet.getString("title")).isEqualTo("New Movie");
		 assertThat(resultSet.getString("director")).isEqualTo("New Director");
		 assertThat(resultSet.next()).isFalse();
		 resultSet.close();
		 statement.close();
		 connection.close();
	 }
}
