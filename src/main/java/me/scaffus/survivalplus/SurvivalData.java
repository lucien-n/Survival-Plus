package me.scaffus.survivalplus;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SurvivalData {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;

    public HashMap<UUID, Double> playerSkillPointsFarming = new HashMap<>();
    public HashMap<UUID, Double> playerSkillPointsMining = new HashMap<>();
    public HashMap<UUID, Double> playerSkillPointsCombat = new HashMap<>();
    public HashMap<UUID, Double> playerSkillPointsRunning = new HashMap<>();
    public HashMap<UUID, Double> playerSkillPointsDeath = new HashMap<>();
    public HashMap<UUID, Double> playerSkillPointsArchery = new HashMap<>();
    public HashMap<UUID, Double> playerSkillPointsSwimming = new HashMap<>();
    public HashMap<UUID, Double> playerSkillPointsFlying = new HashMap<>();

    public HashMap<UUID, Integer> playerSkillLevelsFarming = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelsMining = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelsCombat = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelsRunning = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelsDeath = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelsArchery = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelsSwimming = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelsFlying = new HashMap<>();

    public HashMap<UUID, Boolean> playerHasUpgradeReplanter = new HashMap<>();
    public HashMap<UUID, Boolean> playerHasUpgradeReplanterFortune = new HashMap<>();
    public HashMap<UUID, Integer> playerTokens = new HashMap<>();
    public HashMap<UUID, Integer> playerBalance = new HashMap<>();

    public SurvivalData(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
    }

    public void loadPlayerData(Player p) {
        UUID uuid = p.getUniqueId();

        playerSkillPointsFarming.put(uuid, data.getPlayerSkillPoints(uuid, "farming"));
        playerSkillPointsMining.put(uuid, data.getPlayerSkillPoints(uuid, "mining"));
        playerSkillPointsCombat.put(uuid, data.getPlayerSkillPoints(uuid, "combat"));
        playerSkillPointsRunning.put(uuid, data.getPlayerSkillPoints(uuid, "running"));
        playerSkillPointsDeath.put(uuid, data.getPlayerSkillPoints(uuid, "death"));
        playerSkillPointsArchery.put(uuid, data.getPlayerSkillPoints(uuid, "archery"));
        playerSkillPointsSwimming.put(uuid, data.getPlayerSkillPoints(uuid, "swimming"));
        playerSkillPointsFlying.put(uuid, data.getPlayerSkillPoints(uuid, "flying"));

        playerSkillLevelsFarming.put(uuid, data.getPlayerSkillLevel(uuid, "farming"));
        playerSkillLevelsMining.put(uuid, data.getPlayerSkillLevel(uuid, "mining"));
        playerSkillLevelsCombat.put(uuid, data.getPlayerSkillLevel(uuid, "combat"));
        playerSkillLevelsRunning.put(uuid, data.getPlayerSkillLevel(uuid, "running"));
        playerSkillLevelsDeath.put(uuid, data.getPlayerSkillLevel(uuid, "death"));
        playerSkillLevelsArchery.put(uuid, data.getPlayerSkillLevel(uuid, "archery"));
        playerSkillLevelsSwimming.put(uuid, data.getPlayerSkillLevel(uuid, "swimming"));
        playerSkillLevelsFlying.put(uuid, data.getPlayerSkillLevel(uuid, "flying"));

        playerHasUpgradeReplanter.put(uuid, data.getPlayerUpgrade(uuid, "replanter"));
        playerHasUpgradeReplanterFortune.put(uuid, data.getPlayerUpgrade(uuid, "replanter_fortune"));

        playerTokens.put(uuid, data.getPlayerTokens(uuid));
        playerBalance.put(uuid, data.getPlayerBalance(uuid));
    }

    public void savePlayerData(Player p) {
        UUID uuid = p.getUniqueId();
        data.setPlayerSkillLevel(uuid, "farming", playerSkillLevelsFarming.get(uuid));
        data.setPlayerSkillLevel(uuid, "mining", playerSkillLevelsMining.get(uuid));
        data.setPlayerSkillLevel(uuid, "combat", playerSkillLevelsCombat.get(uuid));
        data.setPlayerSkillLevel(uuid, "running", playerSkillLevelsRunning.get(uuid));
        data.setPlayerSkillLevel(uuid, "death", playerSkillLevelsDeath.get(uuid));
        data.setPlayerSkillLevel(uuid, "archery", playerSkillLevelsArchery.get(uuid));
        data.setPlayerSkillLevel(uuid, "swimming", playerSkillLevelsSwimming.get(uuid));
        data.setPlayerSkillLevel(uuid, "flying", playerSkillLevelsFlying.get(uuid));

        data.setPlayerSkillPoints(uuid, "farming", playerSkillPointsFarming.get(uuid));
        data.setPlayerSkillPoints(uuid, "mining", playerSkillPointsMining.get(uuid));
        data.setPlayerSkillPoints(uuid, "combat", playerSkillPointsCombat.get(uuid));
        data.setPlayerSkillPoints(uuid, "running", playerSkillPointsRunning.get(uuid));
        data.setPlayerSkillPoints(uuid, "death", playerSkillPointsDeath.get(uuid));
        data.setPlayerSkillPoints(uuid, "archery", playerSkillPointsArchery.get(uuid));
        data.setPlayerSkillPoints(uuid, "swimming", playerSkillPointsSwimming.get(uuid));
        data.setPlayerSkillPoints(uuid, "flying", playerSkillPointsFlying.get(uuid));

        data.setPlayerUpgrade(uuid, "replanter", playerHasUpgradeReplanter.get(uuid));
        data.setPlayerUpgrade(uuid, "replanter_fortune", playerHasUpgradeReplanterFortune.get(uuid));

        data.setPlayerTokens(uuid, playerTokens.get(uuid));
        data.setPlayerBalance(uuid, playerBalance.get(uuid));
    }

    public Integer getPlayerTokens(UUID uuid) {
        return playerTokens.get(uuid);
    }

    public void setPlayerTokens(UUID uuid, Integer amount) {
        playerTokens.put(uuid, amount);
    }

    public void incrementPlayerTokens(UUID uuid, Integer amount) {
        playerTokens.put(uuid, playerTokens.get(uuid) + amount);
    }

    public Integer getPlayerBalance(UUID uuid) {
        return playerBalance.get(uuid);
    }

    public void setPlayerBalance(UUID uuid, Integer amount) {
        playerBalance.put(uuid, amount);
    }

    public void incrementPlayerBalance(UUID uuid, Integer amount) {
        playerBalance.put(uuid, playerBalance.get(uuid) + amount);
    }

    public Double getPlayerSkillPoints(UUID uuid, String skill) {
        switch (skill) {
            case "farming":
                return playerSkillPointsFarming.get(uuid);
            case "mining":
                return playerSkillPointsMining.get(uuid);
            case "combat":
                return playerSkillPointsCombat.get(uuid);
            case "running":
                return playerSkillPointsRunning.get(uuid);
            case "death":
                return playerSkillPointsDeath.get(uuid);
            case "archery":
                return playerSkillPointsArchery.get(uuid);
            case "swimming":
                return playerSkillPointsSwimming.get(uuid);
            case "flying":
                return playerSkillPointsFlying.get(uuid);
        }
        return 0.0;
    }

    public void setPlayerSkillPoints(UUID uuid, String skill, Double amount) {
        switch (skill) {
            case "farming":
                playerSkillPointsFarming.put(uuid, amount);
                break;
            case "mining":
                playerSkillPointsMining.put(uuid, amount);
                break;
            case "combat":
                playerSkillPointsCombat.put(uuid, amount);
                break;
            case "running":
                playerSkillPointsRunning.put(uuid, amount);
                break;
            case "death":
                playerSkillPointsDeath.put(uuid, amount);
                break;
            case "archery":
                playerSkillPointsArchery.put(uuid, amount);
                break;
            case "swimming":
                playerSkillPointsSwimming.put(uuid, amount);
                break;
            case "flying":
                playerSkillPointsFlying.put(uuid, amount);
                break;
        }
    }

    public void incrementPlayerSkillPoints(UUID uuid, String skill, Double amount) {
        switch (skill) {
            case "farming":
                playerSkillPointsFarming.put(uuid, playerSkillPointsFarming.get(uuid) + amount);
                break;
            case "mining":
                playerSkillPointsMining.put(uuid, playerSkillPointsMining.get(uuid) + amount);
                break;
            case "combat":
                playerSkillPointsCombat.put(uuid, playerSkillPointsCombat.get(uuid) + amount);
                break;
            case "running":
                playerSkillPointsRunning.put(uuid, playerSkillPointsRunning.get(uuid) + amount);
                break;
            case "death":
                playerSkillPointsDeath.put(uuid, playerSkillPointsDeath.get(uuid) + amount);
                break;
            case "archery":
                playerSkillPointsArchery.put(uuid, playerSkillPointsArchery.get(uuid) + amount);
                break;
            case "swimming":
                playerSkillPointsSwimming.put(uuid, playerSkillPointsSwimming.get(uuid) + amount);
                break;
            case "flying":
                playerSkillPointsFlying.put(uuid, playerSkillPointsFlying.get(uuid) + amount);
                break;
        }
    }

    public Integer getPlayerSkillLevel(UUID uuid, String skill) {
        switch (skill) {
            case "farming":
                return playerSkillLevelsFarming.get(uuid);
            case "mining":
                return playerSkillLevelsMining.get(uuid);
            case "combat":
                return playerSkillLevelsCombat.get(uuid);
            case "running":
                return playerSkillLevelsRunning.get(uuid);
            case "death":
                return playerSkillLevelsDeath.get(uuid);
            case "archery":
                return playerSkillLevelsArchery.get(uuid);
            case "swimming":
                return playerSkillLevelsSwimming.get(uuid);
            case "flying":
                return playerSkillLevelsFlying.get(uuid);
        }
        return 0;
    }

    public void setPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        switch (skill) {
            case "farming":
                playerSkillLevelsFarming.put(uuid, amount);
                break;
            case "mining":
                playerSkillLevelsMining.put(uuid, amount);
                break;
            case "combat":
                playerSkillLevelsCombat.put(uuid, amount);
                break;
            case "running":
                playerSkillLevelsRunning.put(uuid, amount);
                break;
            case "death":
                playerSkillLevelsDeath.put(uuid, amount);
                break;
            case "archery":
                playerSkillLevelsArchery.put(uuid, amount);
                break;
            case "swimming":
                playerSkillLevelsSwimming.put(uuid, amount);
                break;
            case "flying":
                playerSkillLevelsFlying.put(uuid, amount);
                break;
        }
    }

    public void incrementPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        switch (skill) {
            case "farming":
                playerSkillLevelsFarming.put(uuid, playerSkillLevelsFarming.get(uuid) + amount);
                break;
            case "mining":
                playerSkillLevelsMining.put(uuid, playerSkillLevelsMining.get(uuid) + amount);
                break;
            case "combat":
                playerSkillLevelsCombat.put(uuid, playerSkillLevelsCombat.get(uuid) + amount);
                break;
            case "running":
                playerSkillLevelsRunning.put(uuid, playerSkillLevelsRunning.get(uuid) + amount);
                break;
            case "death":
                playerSkillLevelsDeath.put(uuid, playerSkillLevelsDeath.get(uuid) + amount);
                break;
            case "archery":
                playerSkillLevelsArchery.put(uuid, playerSkillLevelsArchery.get(uuid) + amount);
                break;
            case "swimming":
                playerSkillLevelsSwimming.put(uuid, playerSkillLevelsSwimming.get(uuid) + amount);
                break;
            case "flying":
                playerSkillLevelsFlying.put(uuid, playerSkillLevelsFlying.get(uuid) + amount);
                break;
        }
    }
}
