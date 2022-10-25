package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.SkillHelper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.tasks.PlaceBlockTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CombatListener implements Listener {
    private final SurvivalPlus plugin;
    private final SkillHelper skillHelper;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;

    private final Set mobs;
    private final Map points;

    public CombatListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.skillHelper = plugin.skillHelper;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        mobs = skillsConfig.get().getConfigurationSection("combat.mobs").getKeys(false);
        points = skillsConfig.get().getConfigurationSection("combat.mobs").getValues(false);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!mobs.contains(event.getEntity().toString().replace("Craft", "")) || event.getEntity().getKiller() == null)
            return;

        Player p = event.getEntity().getKiller();
        Double pointsGained = (Double) points.get(event.getEntity().toString().replace("Craft", ""));
        skillHelper.handleSkillGain(p, pointsGained, "combat");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player p = (Player) event.getDamager();
        UUID uuid = p.getUniqueId();

        double eventDamage = event.getDamage();
        double supplementaryDamage = (eventDamage * (survivalData.getPlayerUpgrade(uuid, "damage") * 10)) / 100;
        event.setDamage(eventDamage + supplementaryDamage);
    }
}