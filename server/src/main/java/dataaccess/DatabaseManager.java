package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                createUserTable();
                createGameTable();
                createAuthTable();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createUserTable() throws DataAccessException {
        try {
            var statement = "CREATE TABLE `users` (`username` varchar(45) NOT NULL,`password`" +
                    " varchar(100) DEFAULT NULL,`email` varchar(45)DEFAULT NULL, PRIMARY KEY (`username`)," +
                    " UNIQUE KEY `username_UNIQUE` (`username`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
            var conn = DriverManager.getConnection(CONNECTION_URL + "/" + DATABASE_NAME, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createGameTable() throws DataAccessException {
        try {
            var statement = "CREATE TABLE `games` (`gameID` int NOT NULL, `whiteUsername` " +
                    "varchar(45) DEFAULT NULL, `blackUsername` varchar(45)DEFAULT NULL, `gameName`" +
                    " varchar(45) DEFAULT NULL, `game` longblob, PRIMARY KEY (`gameID`), " +
                    "UNIQUE KEY `gameID_UNIQUE` (`gameID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
            var conn = DriverManager.getConnection(CONNECTION_URL + "/" + DATABASE_NAME, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createAuthTable() throws DataAccessException {
        try {
            var statement = "CREATE TABLE `tokens` (`authtoken` varchar(100) NOT NULL, " +
                    "`username` varchar(45) DEFAULT NULL, PRIMARY KEY (`authtoken`), " +
                    "UNIQUE KEY `authtoken_UNIQUE` (`authtoken`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
            var conn = DriverManager.getConnection(CONNECTION_URL + "/" + DATABASE_NAME, USER, PASSWORD);

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);

            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
