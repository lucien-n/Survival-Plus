package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.PlayersData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.UUID;

public class PlayerInteractListener implements Listener {
    private SurvivalPlus plugin;
    private PlayersData pData;
    private SkillsConfig skillsConfig;
    private List<String> hoes;
    private List<String> tillables;
    private List<Object> toTillVectors;

    public PlayerInteractListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.pData = plugin.playersData;
        this.skillsConfig = plugin.skillsConfig;
        hoes = (List<String>) skillsConfig.get().get("farming.hoes");
        tillables = (List<String>) skillsConfig.get().get("farming.tillables");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        // ? WIDE TILL
        if (pData.getPlayerUpgrade(uuid, "wide_till") > 0 && event.getAction() == Action.RIGHT_CLICK_BLOCK
                && hoes.contains(p.getInventory().getItemInMainHand().getType().toString()) && tillables.contains(event.getClickedBlock().getType().toString())) {
            Location blockLoc = event.getClickedBlock().getLocation();

            // * 8 1 2
            // * 7 0 3
            // * 6 5 4

            // 0 => Original block
            // 1
            blockLoc.add(1, 0, 0);
            if (tillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 2
            blockLoc.add(0, 0, 1);
            if (tillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 3
            blockLoc.add(-1, 0, 0);
            if (tillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 4
            blockLoc.add(-1, 0, 0);
            if (tillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 5
            blockLoc.add(0, 0, -1);
            if (tillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 6
            blockLoc.add(0, 0, -1);
            if (tillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 7
            blockLoc.add(1, 0, 0);
            if (tillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 8
            blockLoc.add(1, 0, 0);
            if (tillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
        }
        assert p.getInventory().getItemInMainHand().getItemMeta() != null;
        if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6§lInfoStick")) {
            Location loc = event.getClickedBlock().getLocation();
            p.sendMessage("§e#-----| §6§l" + event.getClickedBlock().getType() + " §e|-----#");
            p.sendMessage("§e - Loc: §6" + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
            p.sendMessage("§e - State: §6" + event.getClickedBlock().getState().getBlockData());
        }
    }
}
