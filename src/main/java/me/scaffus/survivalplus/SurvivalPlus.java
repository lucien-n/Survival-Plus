package me.scaffus.survivalplus;

import me.scaffus.survivalplus.commands.*;
import me.scaffus.survivalplus.listeners.*;
import me.scaffus.survivalplus.listeners.skills.*;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import me.scaffus.survivalplus.sql.DatabaseManager;
import me.scaffus.survivalplus.tasks.DayCountTask;
import me.scaffus.survivalplus.tasks.KeepDbAliveTask;
import me.scaffus.survivalplus.tasks.MagnetTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class SurvivalPlus extends JavaPlugin {

    private final DatabaseManager databaseManager = new DatabaseManager(this);
    public DatabaseGetterSetter data;
    public SkillsConfig skillsConfig = new SkillsConfig();
    public Helper helper = new Helper(this);
    public SkillHelper skillHelper;
    public SurvivalData survivalData;
    public DayCountTask dayCountTask;
    private MagnetTask magnetTask;
    private KeepDbAliveTask keepDbAliveTask;

    @Override
    public void onEnable() {
        this.data = new DatabaseGetterSetter(databaseManager.playerConnection.getConnection(), this);

        saveDefaultConfig();
        skillsConfig.setup();
        skillsConfig.get().options().copyDefaults(true);
        skillsConfig.save();

        survivalData = new SurvivalData(this);
        skillHelper = new SkillHelper(this);

        // Listeners
        new FarmingListener(this);
        new MiningListener(this);
        new CombatListener(this);
        new ExplorerListener(this);
        new DeathListener(this);
        new ChoppingListener(this);
        new FlyingListener(this);

        new CustomMobsListener(this);
        // ? Doesn't work quite much and might be a performance eater
        // new EntityDamageByEntityListener(this);

        new PlayerJoinQuitListener(this);
        new PlayerInteractListener(this);
        new SilkSpawnerListener(this);

        // Commands
        new BankCommand(this);
        new SkillCommand(this);
        new InfoStickCommand(this);
        new ToggleUpgradeCommand(this);
        new MenuCommand(this);

        // Tasks
        this.magnetTask = new MagnetTask(this);
        magnetTask.runTaskTimer(this, 0L, 5L);

        this.keepDbAliveTask = new KeepDbAliveTask(this);
        keepDbAliveTask.runTaskTimer(this, 0L, 3_600L);

        this.dayCountTask = new DayCountTask(this);
        dayCountTask.runTaskTimer(this, 0L, 6_000L);

        this.getLogger().info("Plugin ON");
    }

    @Override
    public void onDisable() {
        try {
            this.databaseManager.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.getLogger().info("Plugin OFF");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
