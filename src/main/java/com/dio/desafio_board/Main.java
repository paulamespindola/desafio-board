package com.dio.desafio_board;


import com.dio.desafio_board.persistence.migration.MigrationStrategy;
import com.dio.desafio_board.ui.MainMenu;

import java.sql.SQLException;

import static com.dio.desafio_board.persistence.config.DatabaseConnectionManager.getConnection;

public class Main {

    public static void main(String[] args) throws SQLException {
        try(var connection = getConnection()){
            new MigrationStrategy(connection).executeMigration();
        }
        new MainMenu().execute();
    }
}