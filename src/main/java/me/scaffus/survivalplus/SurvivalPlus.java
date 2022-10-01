package me.scaffus.survivalplus;

import me.scaffus.survivalplus.EventListeners.BlockBreakListener;
import me.scaffus.survivalplus.EventListeners.BlockPlaceListener;
import me.scaffus.survivalplus.EventListeners.DeathListener;
import me.scaffus.survivalplus.EventListeners.PlayerJoinQuitListener;
import me.scaffus.survivalplus.commands.BankCommand;
import me.scaffus.survivalplus.commands.SkillCommand;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import me.scaffus.survivalplus.sql.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class SurvivalPlus extends JavaPlugin {

    public static SurvivalPlus INSTANCE;
    private final DatabaseManager databaseManager = new DatabaseManager(this);
    public DatabaseGetterSetter data;
    public SkillsConfig skillsConfig = new SkillsConfig();
    public Helper helper = new Helper();
    public HashMap<UUID, Integer> playerSelectedAmount;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.data = new DatabaseGetterSetter(databaseManager.playerConnection.getConnection());
        saveDefaultConfig();

        skillsConfig.setup();
        skillsConfig.get().options().copyDefaults(true);
        skillsConfig.save();

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);

        new BankCommand(this);
        new SkillCommand(this);

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
