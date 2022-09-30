package me.scaffus.survivalplus.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseConnection {
    private DatabaseCredentials databaseCredentials;
    private Connection connection;

    public DatabaseConnection(DatabaseCredentials databaseCredentials) {
        this.databaseCredentials = databaseCredentials;
        this.connect();

        Logger.getLogger("Minecraft").info("Connection to database successful");
    }

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.databaseCredentials.toUri(), this.databaseCredentials.getUser(), this.databaseCredentials.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return this.connection;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connect();
        return this.connection;
    }
}
