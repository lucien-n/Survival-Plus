package me.scaffus.survivalplus;

import me.scaffus.survivalplus.objects.PlayerUpgrade;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class SurvivalData {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;
    private Helper helper;
    private SkillsConfig skillsConfig;

    public List<String> skills = Arrays.asList("farming", "mining", "combat", "running", "death", "chopping", "swimming", "flying");
    public HashMap<UUID, HashMap<String, Integer>> playersLevels = new HashMap<UUID, HashMap<String, Integer>>();
    public HashMap<UUID, HashMap<String, Double>> playersPoints = new HashMap<UUID, HashMap<String, Double>>();

    public HashMap<UUID, Integer> playerTokens = new HashMap<>();
    public HashMap<UUID, Integer> playerBalance = new HashMap<>();

    public HashMap<UUID, HashMap<String, Integer>> playersUpgrades = new HashMap<>();

    public String upgradeBought;
    public HashMap<String, PlayerUpgrade> allUpgrades = new HashMap<>();

    public SurvivalData(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.helper = plugin.helper;
        this.skillsConfig = plugin.skillsConfig;
        this.upgradeBought = plugin.getConfig().getString("skills.upgrade_bought");
        createUpgrades();
    }

    public void loadPlayerData(Player p) {
        UUID uuid = p.getUniqueId();

        loadPlayerPoints(uuid);
        loadPlayerLevels(uuid);
        loadPlayerUpgrades(uuid);

        playerTokens.put(uuid, data.getPlayerTokens(uuid));
        playerBalance.put(uuid, data.getPlayerBalance(uuid));
    }

    public void savePlayerData(Player p) {
        UUID uuid = p.getUniqueId();

        savePlayerPoints(uuid);
        savePlayerLevels(uuid);
        savePlayerUpgrades(uuid);

        data.setPlayerTokens(uuid, playerTokens.get(uuid));
        data.setPlayerBalance(uuid, playerBalance.get(uuid));
    }

    public void createUpgrades() {
        FileConfiguration config = skillsConfig.get();
        Set<String> upgradesKeysSet = config.getConfigurationSection("upgrades").getKeys(false);
        ArrayList<String> upgradeKeysList = new ArrayList<String>(upgradesKeysSet);
        for (String upgrade : upgradeKeysList) {
            String name = upgrade;
            String displayName = (String) config.get("upgrades." + upgrade + ".display_name");
            String displayItem = (String) config.get("upgrades." + upgrade + ".display_item");
            Integer maxLevel = (Integer) config.get("upgrades." + upgrade + ".max_level");
            Integer cost = (Integer) config.get("upgrades." + upgrade + ".cost");
            Double costFactor = (Double) config.get("upgrades." + upgrade + ".cost_factor");
            PlayerUpgrade playerUpgrade = new PlayerUpgrade(name, displayName, Material.getMaterial(displayItem), maxLevel, cost, costFactor);
            allUpgrades.put(upgrade, playerUpgrade);
        }
    }

    public PlayerUpgrade getUpgrade(String upgradeName) {
        return allUpgrades.get(upgradeName);
    }

    public void loadPlayerPoints(UUID uuid) {
        HashMap<String, Double> pointsMap = new HashMap<>();
        for (String skill : skills) {
            pointsMap.put(skill, helper.round(data.getPlayerSkillPoints(uuid, skill), 2));
        }
        playersPoints.put(uuid, pointsMap);
    }

    public void loadPlayerLevels(UUID uuid) {
        HashMap<String, Integer> levelsMap = new HashMap<>();
        for (String skill : skills) {
            levelsMap.put(skill, data.getPlayerSkillLevel(uuid, skill));
        }
        playersLevels.put(uuid, levelsMap);
    }

    public void savePlayerPoints(UUID uuid) {
        HashMap<String, Double> playerPoints = playersPoints.get(uuid);
        for (String skill : skills) {
            if (playerPoints.get(skill) != 0) {
                data.setPlayerSkillPoints(uuid, skill, helper.round(playerPoints.get(skill), 2));
            }
        }
    }

    public void savePlayerLevels(UUID uuid) {
        HashMap<String, Integer> playerLevels = playersLevels.get(uuid);
        for (String skill : skills) {
            if (playerLevels.get(skill) != 0) {
                data.setPlayerSkillLevel(uuid, skill, playerLevels.get(skill));
            }
        }
    }

    public void loadPlayerUpgrades(UUID uuid) {
        HashMap<String, Integer> upgradeMap = new HashMap<>();
        for (String upgradeName : allUpgrades.keySet()) {
            upgradeMap.put(upgradeName, data.getPlayerUpgrade(uuid, upgradeName));
        }
        playersUpgrades.put(uuid, upgradeMap);
    }

    public void savePlayerUpgrades(UUID uuid) {
        HashMap<String, Integer> upgradeMap = playersUpgrades.get(uuid);
        for (String upgradeName : allUpgrades.keySet()) {
            data.setPlayerUpgrade(uuid, upgradeName, upgradeMap.get(upgradeName));
        }
    }

    public Integer getPlayerUpgrade(UUID uuid, String upgradeName) {
        HashMap<String, Integer> playerUpgradeMap = playersUpgrades.get(uuid);
        return playerUpgradeMap.get(upgradeName);
    }

    public void setPlayerUpgrade(UUID uuid, String upgradeName, Integer value) {
        HashMap<String, Integer> upgradeMap = playersUpgrades.get(uuid);
        upgradeMap.put(upgradeName, value);
        playersUpgrades.put(uuid, upgradeMap);
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

    public void setPlayerUpgrade(UUID uuid, String upgrade, Integer status, HashMap playerUpgrade) {
        data.setPlayerUpgrade(uuid, upgrade, status);
        playerUpgrade.put(uuid, status);
    }

    public Double getPlayerSkillPoints(UUID uuid, String skill) {
        return helper.round(playersPoints.get(uuid).get(skill), 2);
    }

    public void setPlayerSkillPoints(UUID uuid, String skill, Double amount) {
        playersPoints.get(uuid).put(skill, amount);
    }

    public void incrementPlayerSkillPoints(UUID uuid, String skill, Double amount) {
        playersPoints.get(uuid).put(skill, playersPoints.get(uuid).get(skill) + amount);
    }

    public Integer getPlayerSkillLevel(UUID uuid, String skill) {
        return playersLevels.get(uuid).get(skill);
    }

    public void setPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        playersLevels.get(uuid).put(skill, amount);
    }

    public void incrementPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        playersLevels.get(uuid).put(skill, playersLevels.get(uuid).get(skill) + amount);
    }
}
