package me.scaffus.survivalplus.objects;

import org.bukkit.Material;

public class PlayerUpgrade {
    public String name;
    public String displayName;
    public Material displayItem;
    public Integer maxLevel;
    public Integer cost;
    public Double costFactor;

    public PlayerUpgrade(String upgradeName, String upgradeDisplayName, Material upgradeDisplayItem, Integer upgradeMaxLevel, Integer upgradeCost, Double upgradeCostFactor) {
        name = upgradeName;
        displayName = upgradeDisplayName;
        displayItem = upgradeDisplayItem;
        maxLevel = upgradeMaxLevel;
        cost = upgradeCost;
        costFactor = upgradeCostFactor;
    }
}
