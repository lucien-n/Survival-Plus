package me.scaffus.survivalplus.sql;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseGetterSetter {
    private Connection connection;

    public DatabaseGetterSetter(Connection connection) {
        this.connection = connection;
    }

    private PreparedStatement ps(String statement) {
        PreparedStatement pS = null;
        try {
            pS = connection.prepareStatement(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pS;
    }

    public void createPlayer(Player p) {
        try {

            UUID uuid = p.getUniqueId();

            if (!playerExists(uuid)) {
                PreparedStatement psBank = ps("INSERT IGNORE INTO players_bank (UUID, BALANCE) VALUES (?, ?)");
                psBank.setString(1, uuid.toString());
                psBank.setInt(2, 0);
                psBank.executeUpdate();

                PreparedStatement psSkills = ps("INSERT IGNORE INTO players_skills (UUID, FARMING, MINING, COMBAT, RUNNING, DEATH, ARCHERY, SWIMMING, FLYING) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                psSkills.setString(1, uuid.toString());
                psSkills.setInt(2, 0);
                psSkills.setInt(3, 0);
                psSkills.setInt(4, 0);
                psSkills.setInt(5, 0);
                psSkills.setInt(6, 0);
                psSkills.setInt(7, 0);
                psSkills.setInt(8, 0);
                psSkills.setInt(9, 0);
                psSkills.executeUpdate();

                PreparedStatement psPlayers = ps("INSERT IGNORE INTO players (UUID, NAME) VALUES (?, ?)");
                psPlayers.setString(1, uuid.toString());
                psPlayers.setString(2, p.getDisplayName());
                psPlayers.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement pS = ps("SELECT * FROM players WHERE UUID=?");
            pS.setString(1, uuid.toString());
            ResultSet result = pS.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPlayerBalance(UUID uuid) {
        try {
            PreparedStatement pS = ps("SELECT * FROM players_bank WHERE UUID=?");
            pS.setString(1, uuid.toString());
            ResultSet result = pS.executeQuery();
            if (result.next()) {
                return result.getInt("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public boolean addPlayerBalance(UUID uuid, int amount) {
        try {
            PreparedStatement pS = ps("UPDATE players_bank SET balance = balance + ? WHERE UUID=?");
            pS.setInt(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerBalance(UUID uuid, int amount) {
        try {
            PreparedStatement pS = ps("UPDATE players_bank SET balance=? WHERE UUID=?");
            pS.setInt(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void incrementPlayerSkill(UUID uuid, String skill, Double amount) {
        try {
            PreparedStatement pS = ps("UPDATE players_skills SET " + skill + " = " + skill + " + ? WHERE UUID=?");
            pS.setDouble(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getPlayerSkillLevel(UUID uuid, String skill) {
        try {
            PreparedStatement pS = ps("SELECT " + skill + " FROM players_skills WHERE UUID=?");
            pS.setString(1, uuid.toString());
            ResultSet result = pS.executeQuery();
            if (result.next()) {
                return result.getDouble(skill);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }
}
