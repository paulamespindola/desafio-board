package com.dio.desafio_board.persistence.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationStrategy {

    private final Connection connection;

    public MigrationStrategy(Connection connection) {
        this.connection = connection;
    }

    public void executeMigration() {
        try {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.yml",
                    new ClassLoaderResourceAccessor(),
                    database
            );
            liquibase.update();
        } catch (LiquibaseException e) {
            e.printStackTrace();
        }
    }
}
