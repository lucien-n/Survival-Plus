package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SkillHelper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerMoveListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final SkillHelper skillHelper;

    public PlayerMoveListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        this.skillHelper = plugin.skillHelper;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        if (!p.isFlying() && !p.isRiptiding() && !p.isSwimming()
                && (event.getFrom().getX() + event.getFrom().getY() + event.getFrom().getZ()) != (event.getTo().getX() + event.getTo().getY() + event.getTo().getZ())) {
            skillHelper.handleSkillGain(p, 0.2, "running");
        }
    }
}
