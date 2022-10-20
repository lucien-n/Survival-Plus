package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ExplorerListener implements Listener {
    private final SurvivalPlus plugin;
    private final SkillHelper skillHelper;

    public ExplorerListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.skillHelper = plugin.skillHelper;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        double coordsFromTotal = (event.getFrom().getX() + event.getFrom().getY() + event.getFrom().getZ());
        double coordsToTotal = (event.getTo().getX() + event.getTo().getY() + event.getTo().getZ());

        if (!p.isFlying() && !p.isRiptiding() && coordsFromTotal != coordsToTotal) {
            if (p.isSwimming() || p.isClimbing() || p.isSprinting()) {
                skillHelper.handleSkillGain(p, 0.2, "explorer");
            }
        }
    }
}
