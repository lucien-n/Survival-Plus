package me.scaffus.survivalplus.objects;

import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class PlayerUpgrade {
    public String name;
    public String displayName;
    public Material displayMaterial;
    public Integer maxLevel;
    public Integer cost;
    public Double costFactor;
    public List<String> lore;

    public PlayerUpgrade(String upgradeName, String upgradeDisplayName, Material upgradeDisplayMaterial, Integer upgradeMaxLevel, Integer upgradeCost, Double upgradeCostFactor, List<String> upgradeLore) {
        name = upgradeName;
        displayName = upgradeDisplayName;
        displayMaterial = upgradeDisplayMaterial;
        maxLevel = upgradeMaxLevel;
        cost = upgradeCost;
        costFactor = upgradeCostFactor;
        lore = upgradeLore;
    }
}
