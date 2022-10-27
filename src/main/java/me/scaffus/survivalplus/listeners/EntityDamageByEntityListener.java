package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {
    private final SurvivalPlus plugin;

    public EntityDamageByEntityListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player || !(event.getEntity() instanceof LivingEntity)) return;
        event.getEntity().setCustomName("§6" + event.getEntity().getName() + "§e | " + ((LivingEntity) event.getEntity()).getHealth() + "❤");
    }
}
