package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.PlayersData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;
    private PlayersData playersData;

    public PlayerJoinQuitListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.playersData = plugin.pData;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        data.createPlayer(p);
        playersData.loadPlayerData(p);
        event.setJoinMessage("§6§l" + p.getDisplayName() + "§e a rejoint.");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        playersData.savePlayerData(p);
        event.setQuitMessage("§6§l" + p.getDisplayName() + "§e a quitté.");
    }
}
