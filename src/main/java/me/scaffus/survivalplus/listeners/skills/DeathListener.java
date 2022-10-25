package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.*;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player p = (Player) event.getEntity();
        UUID uuid = p.getUniqueId();

        if (survivalData.getPlayerUpgrade(uuid, "guardian_angel") > 0) {
            if ((p.getHealth() - event.getFinalDamage()) < 0.0) {
                event.setCancelled(true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 30 * 20, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15 * 20, 2));
                p.playEffect(EntityEffect.TOTEM_RESURRECT);
                p.sendMessage("§eTu as échappé de peu à la mort. Ton §6ange gardien§e t'as sauvé, tu l'as donc perdu.");
                survivalData.setPlayerUpgrade(uuid, "guardian_angel", 0);
            }
        }
    }
}
