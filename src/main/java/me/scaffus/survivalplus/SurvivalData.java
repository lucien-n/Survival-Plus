package me.scaffus.survivalplus;

import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SurvivalData {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;

    public List<String> skills;
    public List<String> upgrades;

    public HashMap<UUID, List<HashMap<String, Boolean>>> playersUpgrades;
    public HashMap<UUID, List<HashMap<String, Double>>> playersSkillsPoints;
    public HashMap<UUID, List<HashMap<String, Integer>>> playersSkillsLevel;
    public HashMap<UUID, Integer> playersTokens;
    public HashMap<UUID, Integer> playersBalances;

    public SurvivalData(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;

        this.skills.add("farming");
        this.skills.add("mining");
        this.skills.add("combat");
        this.skills.add("running");
        this.skills.add("death");
        this.skills.add("archery");
        this.skills.add("swimming");
        this.skills.add("flying");

        this.upgrades.add("replanter");
        this.upgrades.add("replanter_fortune");
    }
    
    public void loadPlayerData(Player p) {
        List<HashMap<String, Double>> playerSkillsPointsList = null;
        List<HashMap<String, Integer>> playerSkillsLevelList = null;
        for (String skill : skills) {
            Double playerSkillPoints = data.getPlayerSkillPoints(p.getUniqueId(), skill);
            Integer playerSkillLevel = data.getPlayerSkillLevel(p.getUniqueId(), skill);
            playerSkillsPointsList.add((HashMap<String, Double>) new HashMap<>().put(skill, playerSkillPoints));
            playerSkillsLevelList.add((HashMap<String, Integer>) new HashMap<>().put(skill, playerSkillLevel));
        }
        playersSkillsPoints.put(p.getUniqueId(), playerSkillsPointsList);
        playersSkillsLevel.put(p.getUniqueId(), playerSkillsLevelList);

        List<HashMap<String, Boolean>> playerHasUpgradeList = null;
        for (String upgrade : upgrades) {
            Boolean playerHasUpgrade = data.getPlayerUpgrade(p.getUniqueId(), upgrade);
            playerHasUpgradeList.add((HashMap<String, Boolean>) new HashMap<>().put(upgrade, playerHasUpgrade));
        }
        playersUpgrades.put(p.getUniqueId(), playerHasUpgradeList);

        playersTokens.put(p.getUniqueId(), data.getPlayerTokens(p.getUniqueId()));
        playersBalances.put(p.getUniqueId(), data.getPlayerBalance(p.getUniqueId()));
    }

    public void savePlayerData(Player p) {
        List playerSkillsPointsList = playersSkillsPoints.get(p.getUniqueId());
        for (Object skill : playerSkillsPointsList) {
            Integer playerSkillLevel = data.getPlayerSkillLevel(p.getUniqueId(), skill);
        data.setPlayerUpgrade(p.getUniqueId(), );
        }
    }
}
