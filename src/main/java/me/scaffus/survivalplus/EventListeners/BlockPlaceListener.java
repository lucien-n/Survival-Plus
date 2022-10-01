package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;
import java.util.Set;

public class BlockPlaceListener implements Listener {
    private SurvivalPlus plugin;
    private SkillsConfig skillsConfig;
    private DatabaseGetterSetter data;
    private Helper helper;
    private Set<String> blocks;
    private Map points;

    public BlockPlaceListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.skillsConfig = plugin.skillsConfig;
        this.data = plugin.data;
        this.helper = plugin.helper;
        blocks = skillsConfig.get().getConfigurationSection("mining.blocks").getKeys(false);
        points = skillsConfig.get().getConfigurationSection("mining.blocks").getValues(false);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (blocks.contains(event.getBlock().getType().toString())) {
            Double pointsLost = -helper.round((Double) points.get(event.getBlock().getType().toString()), 2);
            data.incrementPlayerSkill(event.getPlayer().getUniqueId(), "mining", pointsLost);
            event.getPlayer().spigot().sendMessage(
                    ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getConfig().getString("skills.gained")
                            .replace("%amount%", String.valueOf(pointsLost)).replace("%skill%", "minage")));
        }
    }
}
