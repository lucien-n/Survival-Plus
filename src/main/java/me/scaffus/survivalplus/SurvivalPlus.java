package me.scaffus.survivalplus;

import me.scaffus.survivalplus.commands.ToggleUpgradeCommand;
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

    private final DatabaseManager databaseManager = new DatabaseManager(this);
    public DatabaseGetterSetter data;
    public SkillsConfig skillsConfig = new SkillsConfig();
    public Helper helper = new Helper(this);
    public SurvivalData survivalData;
    private MagnetTask magnetTask;
    @Override
    public void onEnable() {
        this.data = new DatabaseGetterSetter(databaseManager.playerConnection.getConnection(), this);

        saveDefaultConfig();
        skillsConfig.setup();
        skillsConfig.get().options().copyDefaults(true);
        skillsConfig.save();

        survivalData = new SurvivalData(this);

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);

        new BankCommand(this);
        new SkillCommand(this);
        new InfoStickCommand(this);
        new ToggleUpgradeCommand(this);

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
