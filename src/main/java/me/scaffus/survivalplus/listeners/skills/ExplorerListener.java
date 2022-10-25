package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class ExplorerListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillHelper skillHelper;
    private PotionEffect waterBreathingEffect;
    private PotionEffect dolphinGraceEffect;

    public ExplorerListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.skillHelper = plugin.skillHelper;
        this.survivalData = plugin.survivalData;
        waterBreathingEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, 1 * 20, 0);
        dolphinGraceEffect = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1 * 20, 0);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        double coordsFromTotal = (event.getFrom().getX() + event.getFrom().getY() + event.getFrom().getZ());
        double coordsToTotal = (event.getTo().getX() + event.getTo().getY() + event.getTo().getZ());

        if (!p.isFlying() && !p.isRiptiding() && coordsFromTotal != coordsToTotal) {
            if (p.isSprinting()) {
                skillHelper.handleSkillGain(p, 0.4, "explorer");
            }
            if (p.isInWater() && p.isSwimming()) {
                skillHelper.handleSkillGain(p, 0.8, "explorer");
            }
        }

        if (p.isInWater() && survivalData.getPlayerUpgrade(uuid, "breath") > 0) {
            p.addPotionEffect(waterBreathingEffect);
        }

        if (p.isInWater() && p.isSwimming() && survivalData.getPlayerUpgrade(uuid ,"dolphin") > 0) {
            p.addPotionEffect(dolphinGraceEffect);
        }
    }
}
