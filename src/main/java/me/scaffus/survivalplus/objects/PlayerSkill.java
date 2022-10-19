package me.scaffus.survivalplus.objects;

import org.bukkit.Material;

public class PlayerSkill {
    public String id;
    public String displayName;
    public Material displayMaterial;

    public PlayerSkill(String skillId, String skillDisplayName, Material skillDisplayMaterial) {
        id = skillId;
        displayName = skillDisplayName;
        displayMaterial = skillDisplayMaterial;
    }
}
