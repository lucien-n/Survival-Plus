package me.scaffus.survivalplus.sql;

import me.scaffus.survivalplus.Helper;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseGetterSetter {
    private Connection connection;
    private Helper helper = new Helper();

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
                PreparedStatement psBank = ps("INSERT IGNORE INTO players_bank (UUID) VALUES (?)");
                psBank.setString(1, uuid.toString());
                psBank.executeUpdate();

                PreparedStatement psSkills = ps("INSERT IGNORE INTO players_points (UUID) VALUES (?)");
                psSkills.setString(1, uuid.toString());
                psSkills.executeUpdate();

                PreparedStatement psLevels = ps("INSERT IGNORE INTO players_levels (UUID) VALUES (?)");
                psLevels.setString(1, uuid.toString());
                psLevels.executeUpdate();

                PreparedStatement psData = ps("INSERT IGNORE INTO players_data (UUID) VALUES (?)");
                psData.setString(1, uuid.toString());
                psData.executeUpdate();

                PreparedStatement psUpgrades = ps("INSERT IGNORE INTO players_upgrades (UUID) VALUES (?)");
                psUpgrades.setString(1, uuid.toString());
                psUpgrades.executeUpdate();

                PreparedStatement psPlayers = ps("INSERT IGNORE INTO players (UUID, NAME) VALUES (?, ?)");
                psPlayers.setString(1, uuid.toString());
                psPlayers.setString(2, p.getDisplayName());
                psPlayers.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean playerExists(UUID uuid) {
        try {
            PreparedStatement pS = ps("SELECT * FROM players WHERE UUID=?");
            pS.setString(1, uuid.toString());
            ResultSet result = pS.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getPlayerBalance(UUID uuid) {
        if (uuid == null) return 0;
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

    public Boolean addPlayerBalance(UUID uuid, Integer amount) {
        if (uuid == null || amount == null) return false;
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

    public void setPlayerBalance(UUID uuid, Integer amount) {
        if (uuid == null || amount == null) return;
        try {
            PreparedStatement pS = ps("UPDATE players_bank SET balance=? WHERE UUID=?");
            pS.setInt(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void incrementPlayerSkillPoints(UUID uuid, String skill, Double amount) {
        if (uuid == null || skill == null || amount == null) return;
        try {
            PreparedStatement pS = ps("UPDATE players_points SET " + skill + " = " + skill + " + ? WHERE UUID=?");
            pS.setDouble(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerSkillPoints(UUID uuid, String skill, Double amount) {
        if (uuid == null || skill == null || amount == null) return;
        try {
            PreparedStatement pS = ps("UPDATE players_points SET " + skill + " = ? WHERE UUID=?");
            pS.setDouble(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();

            incrementPlayerTokens(uuid, 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getPlayerSkillPoints(UUID uuid, String skill) {
        if (uuid == null || skill == null) return 0.0;
        try {
            PreparedStatement pS = ps("SELECT " + skill + " FROM players_points WHERE UUID=?");
            pS.setString(1, uuid.toString());
            ResultSet result = pS.executeQuery();
            if (result.next()) {
                return helper.round(result.getDouble(skill), 2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }

    public void incrementPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        if (uuid == null || skill == null || amount == null) return;
        try {
            PreparedStatement pS = ps("UPDATE players_levels SET " + skill + " = " + skill + " + ? WHERE UUID=?");
            pS.setInt(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        if (uuid == null || skill == null || amount == null) return;
        try {
            PreparedStatement pS = ps("UPDATE players_levels SET " + skill + " = ? WHERE UUID=?");
            pS.setInt(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();

            incrementPlayerTokens(uuid, 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getPlayerSkillLevel(UUID uuid, String skill) {
        if (uuid == null || skill == null) return 0;
        try {
            PreparedStatement pS = ps("SELECT " + skill + " FROM players_levels WHERE UUID=?");
            pS.setString(1, uuid.toString());
            ResultSet result = pS.executeQuery();
            if (result.next()) {
                return result.getInt(skill);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void incrementPlayerTokens(UUID uuid, Integer amount) {
        if (uuid == null || amount == null) return;
        try {
            PreparedStatement pS = ps("UPDATE players_data SET tokens = tokens + ? WHERE UUID=?");
            pS.setInt(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerTokens(UUID uuid, Integer amount) {
        if (uuid == null || amount == null) return;
        try {
            PreparedStatement pS = ps("UPDATE players_data SET tokens = ? WHERE UUID=?");
            pS.setInt(1, amount);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getPlayerTokens(UUID uuid) {
        if (uuid == null) return 0;
        try {
            PreparedStatement pS = ps("SELECT tokens FROM players_data WHERE UUID=?");
            pS.setString(1, uuid.toString());
            ResultSet result = pS.executeQuery();
            if (result.next()) {
                return result.getInt("tokens");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void setPlayerUpgrade(UUID uuid, String upgradeName, Integer status) {
        if (uuid == null || upgradeName == null || status == null) return;
        try {
            PreparedStatement pS = ps("UPDATE players_upgrades SET " + upgradeName + "= ? WHERE UUID=?");
            pS.setInt(1, status);
            pS.setString(2, uuid.toString());
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getPlayerUpgrade(UUID uuid, String upgradeName) {
        if (uuid == null || upgradeName == null) return 0;
        try {
            PreparedStatement pS = ps("SELECT * FROM players_upgrades WHERE UUID=?");
            pS.setString(1, uuid.toString());
            ResultSet result = pS.executeQuery();
            if (result.next()) {
                return result.getInt(upgradeName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
