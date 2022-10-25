package me.scaffus.survivalplus.tasks;

import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.scheduler.BukkitRunnable;

public class DayCountTask extends BukkitRunnable {
    private final SurvivalPlus plugin;
    public long dayPassed;

    public DayCountTask(SurvivalPlus plugin) {
        this.plugin = plugin;
    }

    public void run() {
        dayPassed = plugin.getServer().getWorlds().get(0).getFullTime() / 24_000;
    }
}
