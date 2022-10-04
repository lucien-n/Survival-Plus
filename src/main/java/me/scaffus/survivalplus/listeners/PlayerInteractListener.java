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
    private SurvivalData survivalData;
    private SkillsConfig skillsConfig;
    private List<String> farmingHoes;
    private List<String> farmingTillables;

    private List<String> choppingDestrippables;
    private Set choppingLogs;
    public PlayerInteractListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        farmingHoes = (List<String>) skillsConfig.get().get("farming.tools");
        farmingTillables = (List<String>) skillsConfig.get().get("farming.tillables");

        choppingDestrippables = (List<String>) skillsConfig.get().get("chopping.destrippables");
        choppingLogs = skillsConfig.get().getConfigurationSection("chopping.logs").getKeys(false);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        Block block = event.getClickedBlock();

        // ? WIDE TILL
        if (survivalData.getPlayerUpgrade(uuid, "wide_till") > 0 && event.getAction() == Action.RIGHT_CLICK_BLOCK
                && farmingHoes.contains(p.getInventory().getItemInMainHand().getType().toString()) && farmingTillables.contains(event.getClickedBlock().getType().toString())) {
            Location blockLoc = event.getClickedBlock().getLocation();

            // * 8 1 2
            // * 7 0 3
            // * 6 5 4

            // 0 => Original block
            // 1
            blockLoc.add(1, 0, 0);
            if (farmingTillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 2
            blockLoc.add(0, 0, 1);
            if (farmingTillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 3
            blockLoc.add(-1, 0, 0);
            if (farmingTillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 4
            blockLoc.add(-1, 0, 0);
            if (farmingTillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 5
            blockLoc.add(0, 0, -1);
            if (farmingTillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 6
            blockLoc.add(0, 0, -1);
            if (farmingTillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 7
            blockLoc.add(1, 0, 0);
            if (farmingTillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
            // 8
            blockLoc.add(1, 0, 0);
            if (farmingTillables.contains(blockLoc.getBlock().getType().toString()))
                blockLoc.getBlock().setType(Material.FARMLAND);
        }

        // ? DESTRIP
        if (survivalData.getPlayerUpgrade(uuid, "destrip") > 0 && event.getAction() == Action.RIGHT_CLICK_BLOCK && choppingDestrippables.contains(block.getType().toString())) {
            Material type = Material.AIR;
            switch (block.getType()) {
                case STRIPPED_OAK_LOG:
                    type = Material.OAK_LOG;
                    break;
                case STRIPPED_DARK_OAK_LOG:
                    type = Material.DARK_OAK_LOG;
                    break;
                case STRIPPED_SPRUCE_LOG:
                    type = Material.SPRUCE_LOG;
                    break;
                case STRIPPED_ACACIA_LOG:
                    type = Material.ACACIA_LOG;
                    break;
                case STRIPPED_JUNGLE_LOG:
                    type = Material.JUNGLE_LOG;
                    break;
                case STRIPPED_BIRCH_LOG:
                    type = Material.BIRCH_LOG;
                    break;
                case STRIPPED_MANGROVE_LOG:
                    type = Material.MANGROVE_LOG;
                    break;
            }
            block.setType(type);
            p.playSound(p.getLocation(), Sound.ITEM_AXE_STRIP, 1.0F, 1.0F);
        }

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
