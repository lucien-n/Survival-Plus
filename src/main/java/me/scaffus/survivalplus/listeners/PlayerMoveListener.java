package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final Double moveXp;

    public PlayerMoveListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        moveXp = skillsConfig.get().getDouble("running.xp");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (!p.isFlying() && !p.isRiptiding() && !p.isSwimming()) {
            survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "running", moveXp);
        }
    }
}
