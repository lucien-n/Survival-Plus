package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {
    private final SurvivalPlus plugin;
    private final DatabaseGetterSetter data;
    private final SurvivalData survivalData;
    public PlayerJoinQuitListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.survivalData = plugin.survivalData;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        data.createPlayer(p);
        survivalData.loadPlayerData(p);
        event.setJoinMessage("§6§l" + p.getDisplayName() + "§e a rejoint.");
        p.sendMessage("§6§n/menu§e est la pour toi ❤");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        survivalData.savePlayerData(p);
        event.setQuitMessage("§6§l" + p.getDisplayName() + "§e a quitté.");
        p.setAllowFlight(false);
        Bukkit.getServer().reload();
    }
}
