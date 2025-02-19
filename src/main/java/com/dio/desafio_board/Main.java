package com.dio.desafio_board;


import com.dio.desafio_board.persistence.migration.MigrationStrategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost:5433/board-dio";
        String user = "postgres";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            new MigrationStrategy(connection).executeMigration();
        }

    }
}