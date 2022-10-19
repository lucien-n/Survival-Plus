package me.scaffus.survivalplus.objects;

import org.bukkit.Material;

import java.util.List;

public class PlayerUpgrade {
    public String id;
    public String displayName;
    public Material displayMaterial;
    public Integer maxLevel;
    public Integer cost;
    public Double costFactor;
    public List<String> lore;
    // ex: logvity, magnet (ranges)
    public List<Integer> ranges;

    public PlayerUpgrade(String upgradeId, String upgradeDisplayName, Material upgradeDisplayMaterial, Integer upgradeMaxLevel, Integer upgradeCost, Double upgradeCostFactor, List<String> upgradeLore, Object upgradeRanges) {
        id = upgradeId;
        displayName = upgradeDisplayName;
        displayMaterial = upgradeDisplayMaterial;
        maxLevel = upgradeMaxLevel;
        cost = upgradeCost;
        costFactor = upgradeCostFactor;
        lore = upgradeLore;
        if (upgradeRanges != null) ranges = (List<Integer>) upgradeRanges;
    }
}
