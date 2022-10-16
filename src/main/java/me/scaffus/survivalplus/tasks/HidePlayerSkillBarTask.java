package me.scaffus.survivalplus.tasks;

import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class HidePlayerSkillBarTask extends BukkitRunnable {
    private final BossBar bar;

    public HidePlayerSkillBarTask(BossBar bar) {
        this.bar = bar;
    }

    @Override
    public void run() {
        bar.setVisible(false);
        bar.removeAll();
    }
}
