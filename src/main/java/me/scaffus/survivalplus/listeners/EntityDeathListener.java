package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.Set;

public class EntityDeathListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final Helper helper;
    private final Set<String> mobs;
    private final Map points;

    public EntityDeathListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        this.helper = plugin.helper;
        mobs = skillsConfig.get().getConfigurationSection("combat.mobs").getKeys(false);
        points = skillsConfig.get().getConfigurationSection("combat.mobs").getValues(false);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!mobs.contains(event.getEntity().toString().replace("Craft", "")) || event.getEntity().getKiller() == null)
            return;

        Player p = event.getEntity().getKiller();
        Double pointsGained = helper.round((Double) points.get(event.getEntity().toString().replace("Craft", "")), 2);
        survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "combat", pointsGained);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getConfig().getString("skills.gained")
                .replace("%amount%", String.valueOf(pointsGained)).replace("%skill%", "combat")));
    }
}
