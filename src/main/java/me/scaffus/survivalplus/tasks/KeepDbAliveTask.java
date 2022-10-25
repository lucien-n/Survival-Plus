package me.scaffus.survivalplus.tasks;

import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.scheduler.BukkitRunnable;

public class KeepDbAliveTask extends BukkitRunnable {
    private final SurvivalPlus plugin;
    private final DatabaseGetterSetter data;

    public KeepDbAliveTask(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
    }

    @Override
    public void run() {
        data.query("SELECT * FROM players WHERE UUID=\"d4b36b28-b847-4a14-8851-3025f07373fc\"");
        plugin.getLogger().info("Sent keep alive query to database.");
    }
}
