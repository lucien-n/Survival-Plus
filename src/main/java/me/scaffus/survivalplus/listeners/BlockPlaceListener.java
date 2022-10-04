package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;
import java.util.Set;

public class BlockPlaceListener implements Listener {
    private SurvivalPlus plugin;
    private SkillsConfig skillsConfig;
    private SurvivalData survivalData;
    private Helper helper;
    private Set<String> miningOres;
    private Map miningPoints;
    private Set<String> choppingLogs;
    private Map choppingPoints;

    public BlockPlaceListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.skillsConfig = plugin.skillsConfig;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        miningOres = skillsConfig.get().getConfigurationSection("mining.blocks").getKeys(false);
        miningPoints = skillsConfig.get().getConfigurationSection("mining.blocks").getValues(false);
        choppingLogs = skillsConfig.get().getConfigurationSection("chopping.logs").getKeys(false);
        choppingPoints = skillsConfig.get().getConfigurationSection("chopping.logs").getValues(false);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player p = event.getPlayer();

        if (miningOres.contains(block.getType().toString())) {
            Double pointsLost = -helper.round((Double) miningPoints.get(block.getType().toString()), 2);
            survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "mining", pointsLost);
            p.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getConfig().getString("skills.gained")
                            .replace("%amount%", String.valueOf(pointsLost)).replace("%skill%", "minage")));
        } else if (choppingLogs.contains(block.getType().toString())) {
            Double pointsLost = -helper.round((Double) choppingPoints.get(block.getType().toString()), 2);
            survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "chopping", pointsLost);
            p.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getConfig().getString("skills.gained")
                            .replace("%amount%", String.valueOf(pointsLost)).replace("%skill%", "bûcheronnage")));
        }
    }
}
