package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class DeathListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillHelper skillHelper;
    private final SkillsConfig skillsConfig;
    private final Helper helper;

    public DeathListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillHelper = plugin.skillHelper;
        this.skillsConfig = plugin.skillsConfig;
        this.helper = plugin.helper;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        UUID uuid = p.getUniqueId();

        skillHelper.handleSkillGain(p, 200.0, "death");
    }
}
