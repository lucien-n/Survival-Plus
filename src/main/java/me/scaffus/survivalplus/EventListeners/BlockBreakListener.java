package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockBreakListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final Helper helper;
    private final Set ores;
    private final Map oresPoints;
    private final Set crops;
    private final Map cropsPoints;
    private final List levels;
    private final List<String> farmingTools;
    private final List<String> replantableCrops;
    private final String skillsGainedXpMessage;
    private final String skillsPassedLevelMessage;

    public BlockBreakListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        this.helper = plugin.helper;
        ores = skillsConfig.get().getConfigurationSection("mining.blocks").getKeys(false);
        oresPoints = skillsConfig.get().getConfigurationSection("mining.blocks").getValues(false);
        crops = skillsConfig.get().getConfigurationSection("farming.crops").getKeys(false);
        cropsPoints = skillsConfig.get().getConfigurationSection("farming.crops").getValues(false);
        levels = (List) skillsConfig.get().get("points_for_level");
        farmingTools = (List) skillsConfig.get().get("farming.hoes");
        replantableCrops = (List) skillsConfig.get().get("farming.replantables");

        skillsGainedXpMessage = plugin.getConfig().getString("skills.gained");
        skillsPassedLevelMessage = plugin.getConfig().getString("skills.passed_level");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();

        // MINING
        if (ores.contains(block.getType().toString())) {
            // Points
            Double pointsGained = helper.round((Double) oresPoints.get(block.getType().toString()), 2);
            survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "mining", pointsGained);
            helper.sendActionBar(p, skillsGainedXpMessage.replace("%amount%", String.valueOf(pointsGained)).replace("%skill%", "minage"));

            // Levels
            int playerSkillLevel = survivalData.getPlayerSkillLevel(p.getUniqueId(), "mining");
            Double playerSkillPoints = survivalData.getPlayerSkillPoints(p.getUniqueId(), "mining");
            for (int i = 0; i <= levels.size(); i++) {
                if (playerSkillLevel == levels.size()) return;
                if (playerSkillLevel == i && playerSkillPoints >= (int) levels.get(i)) {
                    survivalData.incrementPlayerSkillLevel(p.getUniqueId(), "mining", 1);
                    survivalData.incrementPlayerTokens(p.getUniqueId(), 1);
                    p.sendMessage(skillsPassedLevelMessage.replace("%level%", String.valueOf(i + 1)));
                }
            }
            return;
        }

        // FARMING
        ItemStack itemInMainHand = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
        if (itemInMainHand != null && farmingTools.contains(itemInMainHand.getType().toString())) {
            // Checking crop growth (WHY TH IS "Crops" DEPRECATED ARRGGGGGHHHHHHHHHHHH)
            Ageable ageable = (Ageable) block.getBlockData();
            if (ageable.getAge() != ageable.getMaximumAge()) return;

            // Points
            Double pointsGained = helper.round((Double) cropsPoints.getOrDefault(block.getType().toString(), 0.0), 2);
            survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "farming", pointsGained);
            helper.sendActionBar(p, skillsGainedXpMessage.replace("%amount%", String.valueOf(pointsGained)).replace("%skill%", "agriculture"));

            // Levels
            int playerSkillLevel = survivalData.getPlayerSkillLevel(p.getUniqueId(), "farming");
            Double playerSkillPoints = survivalData.getPlayerSkillPoints(p.getUniqueId(), "farming");
            for (int i = 0; i <= levels.size(); i++) {
                if (playerSkillLevel == levels.size()) return;
                if (playerSkillLevel == i && playerSkillPoints >= (int) levels.get(i)) {
                    survivalData.incrementPlayerSkillLevel(p.getUniqueId(), "farming", 1);
                    survivalData.incrementPlayerTokens(p.getUniqueId(), 1);
                    p.sendMessage(skillsPassedLevelMessage.replace("%level%", String.valueOf(i + 1)));
                }
            }

            // Replant
            if (replantableCrops.contains(block.getType().toString())) {
                Location loc = block.getLocation();
                loc.getBlock().setType(Material.GOLD_BLOCK);
            }
            return;
        }
    }
}