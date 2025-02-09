package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DataSourceFactory is a utility class that provides a method to get a connection to the SQLite database.
 * This class should not be instantiated.
 */
public class DataSourceFactory {

	private static final String URL = "jdbc:sqlite:sqlite.db";

	private DataSourceFactory() {
		// This is a static class that should not be instantiated.
		// Here's a way to remember it when this class will have 2K lines and you come
		// back to it in 2 years
		throw new IllegalStateException("This is a static class that should not be instantiated");
	}

	/**
	 * Gets a connection to the SQLite database.
	 *
	 * @return a Connection object to the SQLite database
	 * @throws SQLException if a database access error occurs or the URL is null
	 */
	public static Connection getConnection() throws SQLException {
		try {
			return DriverManager.getConnection(URL);
		} catch (SQLException e) {
			throw new SQLException("SQLite driver not found. Ensure the SQLite dependency is included.", e);
		}
	}
}