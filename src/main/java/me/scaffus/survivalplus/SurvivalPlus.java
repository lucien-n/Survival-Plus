package me.scaffus.survivalplus;

import me.scaffus.survivalplus.commands.ToggleUpgradeCommand;
import me.scaffus.survivalplus.listeners.*;
import me.scaffus.survivalplus.commands.BankCommand;
import me.scaffus.survivalplus.commands.InfoStickCommand;
import me.scaffus.survivalplus.commands.SkillCommand;
import me.scaffus.survivalplus.listeners.skills.*;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import me.scaffus.survivalplus.sql.DatabaseManager;
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

        getServer().getPluginManager().registerEvents(new FarmingListener(this), this);
        getServer().getPluginManager().registerEvents(new MiningListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new ExplorerListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new ChoppingListener(this), this);
        getServer().getPluginManager().registerEvents(new FlyingListener(this), this);

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new SilkSpawnerListener(this), this);

        new BankCommand(this);
        new SkillCommand(this);
        new InfoStickCommand(this);
        new ToggleUpgradeCommand(this);

        this.magnetTask = new MagnetTask(this);
        magnetTask.runTaskTimer(this, 0L, 5L);
        this.keepDbAliveTask = new KeepDbAliveTask(this);
        keepDbAliveTask.runTaskTimer(this, 0L, 3600L);

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
