package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class EntityDamageListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;

    public EntityDamageListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player p = (Player) event.getEntity();
        UUID uuid = p.getUniqueId();

        if (survivalData.getPlayerUpgrade(uuid, "limited_immortality") > 0) {
            if ((p.getHealth() - event.getFinalDamage()) < 0.0) {
                event.setCancelled(true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 30 * 20, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15 * 20, 2));
                p.playEffect(EntityEffect.TOTEM_RESURRECT);
                p.sendMessage("§eTu as échappé de peu à la mort. Ton §6totem gardien§e t'as sauvé, tu l'as donc perdu.");
                survivalData.setPlayerUpgrade(uuid, "limited_immortality", 0);
            }
        }
    }
}
