package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyingListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillHelper skillHelper;
    private final SkillsConfig skillsConfig;
    private final Helper helper;

    public FlyingListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillHelper = plugin.skillHelper;
        this.skillsConfig = plugin.skillsConfig;
        this.helper = plugin.helper;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (!(p.isFlying())) return;
        skillHelper.handleSkillGain(p, 0.2, "flying");
    }
}
