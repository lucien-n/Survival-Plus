package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ChoppingListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillHelper skillHelper;
    private final SkillsConfig skillsConfig;
    private final Helper helper;

    private final Set logs;
    private final Map points;
    private List<String> destrippables;

    public ChoppingListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillHelper = plugin.skillHelper;
        this.skillsConfig = plugin.skillsConfig;
        this.helper = plugin.helper;

        logs = skillsConfig.get().getConfigurationSection("blocks.chopping").getKeys(false);
        points = skillsConfig.get().getConfigurationSection("blocks.chopping").getValues(false);
        destrippables = (List<String>) skillsConfig.get().get("chopping.destrippables");

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        if (!(logs.contains(block.getType().toString()))) return;
        ItemStack tools = p.getInventory().getItemInMainHand();
        UUID uuid = p.getUniqueId();

        Double pointsGained = (Double) points.getOrDefault(block.getType().toString(), 0.0);
        skillHelper.handleSkillGain(p, pointsGained, "chopping");

        // * LOGVITY
        Integer playerLogvityUpgradeLevel = survivalData.getPlayerUpgrade(uuid, "logvity");
        ItemStack tool = p.getInventory().getItemInMainHand();
        if (playerLogvityUpgradeLevel > 0) {
            for (int y = 1; y <= survivalData.getUpgrade("logvity").ranges.get(playerLogvityUpgradeLevel - 1); y++) {
                Block toChopp = block.getWorld().getBlockAt(block.getX(), block.getY() + y, block.getZ());
                if (toChopp.getType() != block.getType()) return;
                toChopp.breakNaturally(tool);
                helper.applyDamage(tool, -1);
                // 70% of gained points to avoid logvity being to op
                survivalData.incrementPlayerSkillPoints(uuid, "chopping", pointsGained * 0.7);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player p = event.getPlayer();

        if (logs.contains(block.getType().toString())) {
            Double pointsLost = -(Double) points.get(block.getType().toString());
            skillHelper.handleSkillGain(p, pointsLost, "chopping");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block block = event.getClickedBlock();
        UUID uuid = p.getUniqueId();

        if (survivalData.getPlayerUpgrade(uuid, "destrip") > 0 && event.getAction() == Action.RIGHT_CLICK_BLOCK && destrippables.contains(block.getType().toString())) {
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
    }
}
