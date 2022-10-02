package me.scaffus.survivalplus;

import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.entity.Player;

import java.util.HashMap;
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

    public HashMap<UUID, Integer> playerSkillLevelFarming = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelMining = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelCombat = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelRunning = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelDeath = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelArchery = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelSwimming = new HashMap<>();
    public HashMap<UUID, Integer> playerSkillLevelFlying = new HashMap<>();

    public HashMap<UUID, Boolean> playerHasUpgradeReplanter = new HashMap<>();
    public HashMap<UUID, Boolean> playerHasUpgradeReplanterFortune = new HashMap<>();
    public HashMap<UUID, Integer> playerTokens = new HashMap<>();
    public HashMap<UUID, Integer> playerBalance = new HashMap<>();

    public String upgradeBought;

    public SurvivalData(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.upgradeBought = plugin.getConfig().getString("skills.upgrade_bought");
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

        playerSkillLevelFarming.put(uuid, data.getPlayerSkillLevel(uuid, "farming"));
        playerSkillLevelMining.put(uuid, data.getPlayerSkillLevel(uuid, "mining"));
        playerSkillLevelCombat.put(uuid, data.getPlayerSkillLevel(uuid, "combat"));
        playerSkillLevelRunning.put(uuid, data.getPlayerSkillLevel(uuid, "running"));
        playerSkillLevelDeath.put(uuid, data.getPlayerSkillLevel(uuid, "death"));
        playerSkillLevelArchery.put(uuid, data.getPlayerSkillLevel(uuid, "archery"));
        playerSkillLevelSwimming.put(uuid, data.getPlayerSkillLevel(uuid, "swimming"));
        playerSkillLevelFlying.put(uuid, data.getPlayerSkillLevel(uuid, "flying"));

        playerHasUpgradeReplanter.put(uuid, data.getPlayerUpgrade(uuid, "replanter"));
        playerHasUpgradeReplanterFortune.put(uuid, data.getPlayerUpgrade(uuid, "replanter_fortune"));

        playerTokens.put(uuid, data.getPlayerTokens(uuid));
        playerBalance.put(uuid, data.getPlayerBalance(uuid));
    }

    public void savePlayerData(Player p) {
        UUID uuid = p.getUniqueId();
        if (playerSkillLevelFarming.get(uuid) != 0) data.setPlayerSkillLevel(uuid, "farming", playerSkillLevelFarming.get(uuid));
        if (playerSkillLevelMining.get(uuid) != 0) data.setPlayerSkillLevel(uuid, "mining", playerSkillLevelMining.get(uuid));
        if (playerSkillLevelCombat.get(uuid) != 0) data.setPlayerSkillLevel(uuid, "combat", playerSkillLevelCombat.get(uuid));
        if (playerSkillLevelRunning.get(uuid) != 0) data.setPlayerSkillLevel(uuid, "running", playerSkillLevelRunning.get(uuid));
        if (playerSkillLevelDeath.get(uuid) != 0) data.setPlayerSkillLevel(uuid, "death", playerSkillLevelDeath.get(uuid));
        if (playerSkillLevelArchery.get(uuid) != 0) data.setPlayerSkillLevel(uuid, "archery", playerSkillLevelArchery.get(uuid));
        if (playerSkillLevelSwimming.get(uuid) != 0) data.setPlayerSkillLevel(uuid, "swimming", playerSkillLevelSwimming.get(uuid));
        if (playerSkillLevelFlying.get(uuid) != 0) data.setPlayerSkillLevel(uuid, "flying", playerSkillLevelFlying.get(uuid));

        if (playerSkillPointsFarming.get(uuid) != 0) data.setPlayerSkillPoints(uuid, "farming", playerSkillPointsFarming.get(uuid));
        if (playerSkillPointsMining.get(uuid) != 0) data.setPlayerSkillPoints(uuid, "mining", playerSkillPointsMining.get(uuid));
        if (playerSkillPointsCombat.get(uuid) != 0) data.setPlayerSkillPoints(uuid, "combat", playerSkillPointsCombat.get(uuid));
        if (playerSkillPointsRunning.get(uuid) != 0) data.setPlayerSkillPoints(uuid, "running", playerSkillPointsRunning.get(uuid));
        if (playerSkillPointsDeath.get(uuid) != 0) data.setPlayerSkillPoints(uuid, "death", playerSkillPointsDeath.get(uuid));
        if (playerSkillPointsArchery.get(uuid) != 0) data.setPlayerSkillPoints(uuid, "archery", playerSkillPointsArchery.get(uuid));
        if (playerSkillPointsSwimming.get(uuid) != 0) data.setPlayerSkillPoints(uuid, "swimming", playerSkillPointsSwimming.get(uuid));
        if (playerSkillPointsFlying.get(uuid) != 0) data.setPlayerSkillPoints(uuid, "flying", playerSkillPointsFlying.get(uuid));

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

    public void setPlayerUpgrade(UUID uuid, String upgrade, Boolean status, HashMap playerUpgrade) {
        data.setPlayerUpgrade(uuid, upgrade, status);
        playerUpgrade.put(uuid, status);
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
                return playerSkillLevelFarming.get(uuid);
            case "mining":
                return playerSkillLevelMining.get(uuid);
            case "combat":
                return playerSkillLevelCombat.get(uuid);
            case "running":
                return playerSkillLevelRunning.get(uuid);
            case "death":
                return playerSkillLevelDeath.get(uuid);
            case "archery":
                return playerSkillLevelArchery.get(uuid);
            case "swimming":
                return playerSkillLevelSwimming.get(uuid);
            case "flying":
                return playerSkillLevelFlying.get(uuid);
        }
        return 0;
    }

    public void setPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        switch (skill) {
            case "farming":
                playerSkillLevelFarming.put(uuid, amount);
                break;
            case "mining":
                playerSkillLevelMining.put(uuid, amount);
                break;
            case "combat":
                playerSkillLevelCombat.put(uuid, amount);
                break;
            case "running":
                playerSkillLevelRunning.put(uuid, amount);
                break;
            case "death":
                playerSkillLevelDeath.put(uuid, amount);
                break;
            case "archery":
                playerSkillLevelArchery.put(uuid, amount);
                break;
            case "swimming":
                playerSkillLevelSwimming.put(uuid, amount);
                break;
            case "flying":
                playerSkillLevelFlying.put(uuid, amount);
                break;
        }
    }

    public void incrementPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        switch (skill) {
            case "farming":
                playerSkillLevelFarming.put(uuid, playerSkillLevelFarming.get(uuid) + amount);
                break;
            case "mining":
                playerSkillLevelMining.put(uuid, playerSkillLevelMining.get(uuid) + amount);
                break;
            case "combat":
                playerSkillLevelCombat.put(uuid, playerSkillLevelCombat.get(uuid) + amount);
                break;
            case "running":
                playerSkillLevelRunning.put(uuid, playerSkillLevelRunning.get(uuid) + amount);
                break;
            case "death":
                playerSkillLevelDeath.put(uuid, playerSkillLevelDeath.get(uuid) + amount);
                break;
            case "archery":
                playerSkillLevelArchery.put(uuid, playerSkillLevelArchery.get(uuid) + amount);
                break;
            case "swimming":
                playerSkillLevelSwimming.put(uuid, playerSkillLevelSwimming.get(uuid) + amount);
                break;
            case "flying":
                playerSkillLevelFlying.put(uuid, playerSkillLevelFlying.get(uuid) + amount);
                break;
        }
    }
}
