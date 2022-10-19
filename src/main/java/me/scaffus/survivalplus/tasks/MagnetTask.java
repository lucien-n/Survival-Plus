package me.scaffus.survivalplus.tasks;

import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MagnetTask extends BukkitRunnable {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final Integer itemThreshold = 384;

    public MagnetTask(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!(survivalData.getPlayerUpgrade(p.getUniqueId(), "magnet") > 0)) return;
            double range = survivalData.getUpgrade("magnet").ranges.get(survivalData.getPlayerUpgrade(p.getUniqueId(), "magnet") - 1);
            List<Entity> nearbyEntities = p.getNearbyEntities(range, range, range);
            // Limits the amount of item to the itemThreshold to limit lag.
            if (nearbyEntities.size() >= itemThreshold) nearbyEntities = nearbyEntities.subList(0, itemThreshold);
            for (Entity e : nearbyEntities) {
                if (!(e instanceof Item)) return;
                ((Item) e).setPickupDelay(0);
                e.teleport(p.getLocation());
            }
        }
    }
}
