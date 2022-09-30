package me.scaffus.survivalplus.sql;

import me.scaffus.survivalplus.SurvivalPlus;

import java.sql.SQLException;

public class DatabaseManager {
    public DatabaseConnection playerConnection;

    public DatabaseManager(SurvivalPlus plugin) {
        this.playerConnection = new DatabaseConnection(new DatabaseCredentials(
                plugin.getConfig().getString("database.host"),
                plugin.getConfig().getString("database.user"),
                plugin.getConfig().getString("database.password"),
                plugin.getConfig().getString("database.database"),
                plugin.getConfig().getInt("database.port")
        ));
    }

    public void close() throws SQLException {
        this.playerConnection.close();
    }
}
