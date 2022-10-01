package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinQuitListener implements Listener {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;

    public PlayerJoinQuitListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        data.createPlayer(event.getPlayer());
        event.setJoinMessage("§6§l" + event.getPlayer().getDisplayName() + "§e a rejoint.");
    }
}
