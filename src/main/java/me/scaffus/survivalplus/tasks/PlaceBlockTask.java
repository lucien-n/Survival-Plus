package me.scaffus.survivalplus.tasks;

import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaceBlockTask extends BukkitRunnable {
    private SurvivalPlus plugin;
    private Location loc;
    private Material block;

    public PlaceBlockTask(SurvivalPlus plugin, Location loc, Material block) {
        this.plugin = plugin;
        this.loc = loc;
        this.block = block;
    }

    public void run() {
        loc.getBlock().setType(block);
    }
}
