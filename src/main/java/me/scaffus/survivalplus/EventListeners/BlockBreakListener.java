package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockBreakListener implements Listener {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;
    private SkillsConfig skillsConfig;
    private Set materials;
    private Map points;

    public BlockBreakListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.skillsConfig = plugin.skillsConfig;
        materials = skillsConfig.get().getConfigurationSection("mining.blocks").getKeys(false);
        points = skillsConfig.get().getConfigurationSection("mining.blocks").getValues(false);
        plugin.getLogger().info(materials.toString());
        plugin.getLogger().info(points.toString());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material material = event.getBlock().getBlockData().getMaterial();
        if (materials.contains(material.toString())) {
            Player p = event.getPlayer();
            Double pointsGained = (Double) points.getOrDefault(material.toString(), 0.0);
            data.incrementPlayerSkill(p.getUniqueId(), "mining", pointsGained);
            // TODO: Send to action bar
            p.sendMessage(plugin.getConfig().getString("skills.mining.gained").replace("%amount%", String.valueOf(pointsGained)));
        }
    }
}