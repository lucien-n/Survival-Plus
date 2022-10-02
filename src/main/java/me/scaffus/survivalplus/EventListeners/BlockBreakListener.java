package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockBreakListener implements Listener {
    private SurvivalPlus plugin;
    private SurvivalData survivalData;
    private SkillsConfig skillsConfig;
    private Helper helper;
    private Set blocks;
    private Map points;
    private List levels;

    public BlockBreakListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        this.helper = plugin.helper;
        blocks = skillsConfig.get().getConfigurationSection("mining.blocks").getKeys(false);
        points = skillsConfig.get().getConfigurationSection("mining.blocks").getValues(false);
        levels = (List) skillsConfig.get().get("points_for_level");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (blocks.contains(event.getBlock().getType().toString())) {
            Player p = event.getPlayer();
            // Points
            Double pointsGained = helper.round((Double) points.get(event.getBlock().getType().toString()), 2);
            survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "mining", pointsGained);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getConfig().getString("skills.gained").replace("%amount%", String.valueOf(pointsGained)).replace("%skill%", "minage")));

            // Levels
            int playerSkillLevel = survivalData.getPlayerSkillLevel(p.getUniqueId(), "mining");
            Double playerSkillPoints = survivalData.getPlayerSkillPoints(p.getUniqueId(), "mining");
            for (int i = 0; i <= levels.size(); i++) {
                if (playerSkillLevel == levels.size()) return;
                if (playerSkillLevel == i && playerSkillPoints >= (int) levels.get(i)) {
                    survivalData.incrementPlayerSkillLevel(p.getUniqueId(), "mining", 1);
                    survivalData.incrementPlayerTokens(p.getUniqueId(), 1);
                    p.sendMessage(plugin.getConfig().getString("skills.passed_level").replace("%level%", String.valueOf(i+1)));
                }
            }
        }
    }
}