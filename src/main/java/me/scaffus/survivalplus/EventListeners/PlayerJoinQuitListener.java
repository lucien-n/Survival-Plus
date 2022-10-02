package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinQuitListener implements Listener {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;
    private SurvivalData survivalData;

    public PlayerJoinQuitListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.survivalData = plugin.survivalData;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        data.createPlayer(p);
        survivalData.loadPlayerData(p);
        event.setJoinMessage("§6§l" + event.getPlayer().getDisplayName() + "§e a rejoint.");
    }
}
