package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;

    public InventoryListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // If current inventory is a menu
        if (event.getView().getTitle().contains("§")) {
            Player p = (Player) event.getWhoClicked();
            if (survivalData.canPlayerClick(p.getUniqueId())) survivalData.setPlayerLastClicked(p.getUniqueId());
        }
    }
}


