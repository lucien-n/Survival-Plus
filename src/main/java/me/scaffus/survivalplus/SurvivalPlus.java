package me.scaffus.survivalplus;

import me.scaffus.survivalplus.listeners.*;
import me.scaffus.survivalplus.commands.BankCommand;
import me.scaffus.survivalplus.commands.InfoStickCommand;
import me.scaffus.survivalplus.commands.SkillCommand;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import me.scaffus.survivalplus.sql.DatabaseManager;
import me.scaffus.survivalplus.tasks.MagnetTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

public final class SurvivalPlus extends JavaPlugin {

    public static SurvivalPlus INSTANCE;
    private final DatabaseManager databaseManager = new DatabaseManager(this);
    public DatabaseGetterSetter data;
    public SkillsConfig skillsConfig = new SkillsConfig();
    public Helper helper = new Helper();
    public SurvivalData survivalData;
    private MagnetTask magnetTask;
    @Override
    public void onEnable() {
        INSTANCE = this;
        this.data = new DatabaseGetterSetter(databaseManager.playerConnection.getConnection());

        saveDefaultConfig();
        skillsConfig.setup();
        skillsConfig.get().options().copyDefaults(true);
        skillsConfig.save();

        this.survivalData = new SurvivalData(this);

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        new BankCommand(this);
        new SkillCommand(this);
        new InfoStickCommand(this);

        this.magnetTask = new MagnetTask(this);
        magnetTask.runTaskTimer(this, 0L, 5L);

        Logger.getLogger("Minecraft").info("[SURV+] Plugin ON");
    }

    @Override
    public void onDisable() {
        try {
            this.databaseManager.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Logger.getLogger("Minecraft").info("[SURV+] Plugin OFF");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
