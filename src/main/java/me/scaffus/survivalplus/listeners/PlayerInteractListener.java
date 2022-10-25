package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerInteractListener implements Listener {
    private SurvivalPlus plugin;

    public PlayerInteractListener(SurvivalPlus plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        Block block = event.getClickedBlock();

        // ? INFO STICK
        if (p.getInventory().getItemInOffHand().getItemMeta() == null) return;
        ItemStack itemInOffHand = p.getInventory().getItemInOffHand();
        if ((itemInOffHand.getItemMeta().getDisplayName().equalsIgnoreCase("§6§lInfoStick"))) {
            Location loc = event.getClickedBlock().getLocation();
            p.sendMessage("§e#-----| §6§l" + event.getClickedBlock().getType() + " §e|-----#");
            p.sendMessage("§e - Loc: §6" + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
            p.sendMessage("§e - State: §6" + event.getClickedBlock().getState().getBlockData());
        }
    }
}
