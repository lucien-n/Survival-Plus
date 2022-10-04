package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeathListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;

    public PlayerDeathListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        UUID uuid = p.getUniqueId();

        survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "death", 200.0);
    }
}
