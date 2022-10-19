package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;
import java.util.Set;

public class BlockPlaceListener implements Listener {
    private final Set choppingSaplings;
    private final Map choppingSaplingsPoints;
    private SurvivalPlus plugin;
    private SkillsConfig skillsConfig;
    private SurvivalData survivalData;
    private SkillHelper skillHelper;
    private Helper helper;
    private Set<String> miningOres;
    private Map miningPoints;
    private Set<String> choppingLogs;
    private Map choppingPoints;

    public BlockPlaceListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.skillsConfig = plugin.skillsConfig;
        this.survivalData = plugin.survivalData;
        this.skillHelper = plugin.skillHelper;
        this.helper = plugin.helper;
        miningOres = skillsConfig.get().getConfigurationSection("blocks.mining").getKeys(false);
        miningPoints = skillsConfig.get().getConfigurationSection("blocks.mining").getValues(false);

        choppingLogs = skillsConfig.get().getConfigurationSection("blocks.chopping").getKeys(false);
        choppingPoints = skillsConfig.get().getConfigurationSection("blocks.chopping").getValues(false);

        choppingSaplings = skillsConfig.get().getConfigurationSection("chopping.saplings").getKeys(false);
        choppingSaplingsPoints = skillsConfig.get().getConfigurationSection("chopping.saplings").getValues(false);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player p = event.getPlayer();

        if (miningOres.contains(block.getType().toString())) {
            Double pointsLost = -helper.round((Double) miningPoints.get(block.getType().toString()), 2);
            skillHelper.handleSkillGain(p, pointsLost, "mining");
        } else if (choppingLogs.contains(block.getType().toString())) {
            Double pointsLost = -helper.round((Double) choppingPoints.get(block.getType().toString()), 2);
            skillHelper.handleSkillGain(p, pointsLost, "chopping");
        }
    }
}
