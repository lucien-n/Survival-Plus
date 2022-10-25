package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.SkillHelper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MiningListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillHelper skillHelper;
    private final SkillsConfig skillsConfig;

    private final Set ores;
    private final Map points;
    private final List<Material> tools;
    private final int[][] veinMinerArea = {
            {-1, -1, -1}, {0, -1, -1}, {1, -1, -1},
            {-1, -1, 0}, {0, -1, 0}, {1, -1, 0},
            {-1, -1, 1}, {0, -1, 1}, {1, -1, 1},

            {-1, 0, -1}, {0, 0, -1}, {1, 0, -1},
            {-1, 0, 0}, {1, 0, 0},
            {-1, 0, 1}, {0, 0, 1}, {1, 0, 1},

            {-1, 1, -1}, {0, 1, -1}, {1, 1, -1},
            {-1, 1, 0}, {0, 1, 0}, {1, 1, 0},
            {-1, 1, 1}, {0, 1, 1}, {1, 1, 1},
    };

    public MiningListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillHelper = plugin.skillHelper;
        this.skillsConfig = plugin.skillsConfig;

        ores = skillsConfig.get().getConfigurationSection("blocks.mining").getKeys(false);
        points = skillsConfig.get().getConfigurationSection("blocks.mining").getValues(false);
        tools = (List<Material>) skillsConfig.get().get("tools.mining");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        if (!(ores.contains(block.getType().toString()))) return;
        ItemStack tool = p.getInventory().getItemInMainHand();
        if (!(tools.contains(tool.getType().toString()))) return;
        UUID uuid = p.getUniqueId();

        Double pointsGained = (Double) points.get(block.getType().toString());
        skillHelper.handleSkillGain(p, pointsGained, "mining");

        // * Autosmelt
        if (survivalData.getPlayerUpgrade(uuid, "auto_smelt") > 0) {
            Material resultItem = Material.AIR;
            switch (block.getType()) {
                case IRON_ORE:
                    resultItem = Material.IRON_INGOT;
                    break;
                case GOLD_ORE:
                    resultItem = Material.GOLD_INGOT;
                    break;
                case COPPER_ORE:
                    resultItem = Material.COPPER_INGOT;
                    break;
                case ANCIENT_DEBRIS:
                    resultItem = Material.NETHERITE_SCRAP;
                    break;
            }
            if (resultItem != Material.AIR) event.setDropItems(false);
            p.getWorld().dropItem(block.getLocation(), new ItemStack(resultItem));
            return;
        }

        // * Vein Miner
        if (survivalData.getPlayerUpgrade(uuid, "vein_mine") > 0) {
            if (ores.contains(block.getType().toString())) {
                for (int[] pos : veinMinerArea) {
                    if (!tools.contains(p.getInventory().getItemInMainHand().getType().toString())) return;
                    Block veinMinedBlock = block.getWorld().getBlockAt(block.getX() + pos[0], block.getY() + pos[1], block.getZ() + pos[2]);
                    if (veinMinedBlock.getType() == block.getType()) {
                        veinMinedBlock.breakNaturally(p.getItemInUse());
                    }
                    survivalData.incrementPlayerSkillPoints(uuid, "mining", pointsGained * 0.7);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player p = event.getPlayer();

        if (ores.contains(block.getType().toString())) {
            Double pointsLost = -(Double) points.get(block.getType().toString());
            skillHelper.handleSkillGain(p, pointsLost, "mining");
        }
    }
}
