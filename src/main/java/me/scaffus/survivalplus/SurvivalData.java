package me.scaffus.survivalplus;

import me.scaffus.survivalplus.objects.PlayerSkill;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class SurvivalData {
    public List<String> skills = Arrays.asList("farming", "mining", "combat", "explorer", "death", "chopping", "flying");

    // ? Player specific data
    public HashMap<UUID, HashMap<String, Integer>> playersLevels = new HashMap<UUID, HashMap<String, Integer>>();
    public HashMap<UUID, HashMap<String, Double>> playersPoints = new HashMap<UUID, HashMap<String, Double>>();
    public HashMap<UUID, Integer> playerTokens = new HashMap<>();
    public HashMap<UUID, Integer> playerBalance = new HashMap<>();
    public HashMap<UUID, HashMap<String, Integer>> playersUpgrades = new HashMap<>();
    public HashMap<UUID, BossBar> playerSkillBar = new HashMap<>();
    public HashMap<UUID, Long> playerLastClicked = new HashMap<>();

    // ? Instantiate all upgrades and skills
    public HashMap<String, PlayerUpgrade> allUpgrades = new HashMap<>();
    public HashMap<String, PlayerSkill> allSkills = new HashMap<>();

//    public HashMap<Location, String> allMobSpawners = new HashMap<>();

    public String upgradeBought;
    private DatabaseGetterSetter data;
    private Helper helper;
    private SkillsConfig skillsConfig;


    public SurvivalData(SurvivalPlus plugin) {
        this.data = plugin.data;
        this.helper = plugin.helper;
        this.skillsConfig = plugin.skillsConfig;
        this.upgradeBought = plugin.getConfig().getString("skills.upgrade_bought");
        createSkills();
        createUpgrades();
    }

//    public void loadWorldData() {
//        allMobSpawners = data.getAllMobSpawners();
//    }
//
//    public void saveWorldData() {
//        data.setAllMobSpawners(allMobSpawners);
//    }

    public void loadPlayerData(Player p) {
        UUID uuid = p.getUniqueId();

        loadPlayerPoints(uuid);
        loadPlayerLevels(uuid);
        loadPlayerUpgrades(uuid);
        createPlayerBossBar(uuid);

        playerTokens.put(uuid, data.getPlayerTokens(uuid));
        playerBalance.put(uuid, data.getPlayerBalance(uuid));

        setPlayerLastClicked(uuid);

        if (getPlayerUpgrade(uuid, "double_jump") > 0) p.setAllowFlight(true);

        if (getPlayerUpgrade(uuid, "fast_attack" ) > 0) p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0 + getPlayerUpgrade(uuid, "fast_attack"));
        if (getPlayerUpgrade(uuid, "cat_life") > 0) p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20 + (getPlayerUpgrade(uuid, "cat_life") * 2));
        if (getPlayerUpgrade(uuid, "speed") > 0) p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1 + (getPlayerUpgrade(uuid, "speed") * 0.035));
    }

    public void savePlayerData(Player p) {
        UUID uuid = p.getUniqueId();

        savePlayerPoints(uuid);
        savePlayerLevels(uuid);
        savePlayerUpgrades(uuid);

        data.setPlayerTokens(uuid, playerTokens.get(uuid));
        data.setPlayerBalance(uuid, playerBalance.get(uuid));
    }

    public void createPlayerBossBar(UUID uuid) {
        BossBar bar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        bar.setVisible(false);
        bar.addPlayer(Bukkit.getPlayer(uuid));
        setPlayerSkillBar(uuid, bar);
    }

    public void createSkills() {
        FileConfiguration config = skillsConfig.get();
        Set<String> skillsKeySet = config.getConfigurationSection("skills").getKeys(false);
        ArrayList<String> skillsKeysList = new ArrayList<>(skillsKeySet);
        for (String skill : skillsKeysList) {
            String id = config.getString("skills." + skill + ".id");
            String displayName = config.getString("skills." + skill + ".display_name");
            String displayItem = config.getString("skills." + skill + ".display_item");
            PlayerSkill playerSkill = new PlayerSkill(id, displayName, Material.getMaterial(displayItem));
            allSkills.put(skill, playerSkill);
        }
    }

    public void createUpgrades() {
        FileConfiguration config = skillsConfig.get();
        Set<String> upgradesKeySet = config.getConfigurationSection("upgrades").getKeys(false);
        ArrayList<String> upgradesKeysList = new ArrayList<>(upgradesKeySet);
        for (String upgrade : upgradesKeysList) {
            String id = upgrade;
            String displayName = config.getString("upgrades." + upgrade + ".display_name");
            String displayItem = config.getString("upgrades." + upgrade + ".display_item");
            Integer maxLevel = config.getInt("upgrades." + upgrade + ".max_level");
            Integer cost = config.getInt("upgrades." + upgrade + ".cost");
            Double costFactor = config.getDouble("upgrades." + upgrade + ".cost_factor");
            List<String> lore = config.getStringList("upgrades." + upgrade + ".lore");

            List<Integer> ranges = new ArrayList<>();
            if (config.get("upgrades." + upgrade + ".ranges") != null) {
                ranges = config.getIntegerList("upgrades." + upgrade + ".ranges");
            }

            PlayerUpgrade playerUpgrade = new PlayerUpgrade(id, displayName, Material.getMaterial(displayItem), maxLevel, cost, costFactor, lore, ranges);
            allUpgrades.put(upgrade, playerUpgrade);
        }
    }

    public PlayerSkill getSkill(String skillName) {
        return allSkills.get(skillName);
    }

    public PlayerUpgrade getUpgrade(String upgradeName) {
        return allUpgrades.get(upgradeName);
    }

    public void setPlayerSkillBar(UUID uuid, BossBar bar) {
        playerSkillBar.put(uuid, bar);
    }

    public BossBar getPlayerSkillBar(UUID uuid) {
        return playerSkillBar.get(uuid);
    }

    public Boolean canPlayerClick(UUID uuid) {
        if ((System.currentTimeMillis() - getPlayerLastClicked(uuid)) > 200) return true;
        else helper.sendActionBar(Bukkit.getPlayer(uuid), "§cAttends §n200ms§c entre chaque action");
        return false;
    }

    public Long getPlayerLastClicked(UUID uuid) {
        return playerLastClicked.get(uuid);
    }

    public void setPlayerLastClicked(UUID uuid) {
        playerLastClicked.put(uuid, System.currentTimeMillis());
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

    public void incrementPlayerTokens(UUID uuid, Integer amount) {
        playerTokens.put(uuid, playerTokens.get(uuid) + amount);
    }

    public Integer getPlayerBalance(UUID uuid) {
        return playerBalance.get(uuid);
    }

    public void incrementPlayerBalance(UUID uuid, Integer amount) {
        playerBalance.put(uuid, playerBalance.get(uuid) + amount);
    }

    public Double getPlayerSkillPoints(UUID uuid, String skill) {
        return helper.round(playersPoints.get(uuid).get(skill), 2);
    }

    public void incrementPlayerSkillPoints(UUID uuid, String skill, Double amount) {
        playersPoints.get(uuid).put(skill, playersPoints.get(uuid).get(skill) + amount);
    }

    public Integer getPlayerSkillLevel(UUID uuid, String skill) {
        return playersLevels.get(uuid).get(skill);
    }

    public void incrementPlayerSkillLevel(UUID uuid, String skill, Integer amount) {
        playersLevels.get(uuid).put(skill, playersLevels.get(uuid).get(skill) + amount);
    }
}
