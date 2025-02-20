package com.dio.desafio_board.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final String DB_URL = "jdbc:postgresql://localhost:5433/board-dio";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "root";

    static {
        try {
            // Registrar o driver PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
