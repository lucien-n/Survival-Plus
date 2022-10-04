package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.tasks.PlaceBlockTask;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BlockBreakListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final Helper helper;
    private final List pointsForLevels;
    private final Set miningOres;
    private final Map miningOrePoints;
    private final List<String> miningTools;
    private final Set farmingCrops;
    private final Map farmingCropsPoints;
    private final List<String> farmingTools;
    private final List<String> farmingReplantableCrops;
    private final String skillsGainedXpMessage;
    private final String skillsPassedLevelMessage;
    private PlaceBlockTask placeBlockTask;

    private ItemStack farminghoeFortune1;
    private ItemStack farmingHoeFortune2;
    private ItemStack farminghoeFortune3;

    private final Set choppingLogs;
    private final Map choppingLogsPoints;
    private final Set choppingSaplings;
    private final Map choppingSaplingsPoints;
    private final List<Integer> choppingLogvityRange;

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

    public BlockBreakListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        this.helper = plugin.helper;

        pointsForLevels = (List) skillsConfig.get().get("points_for_level");

        miningOres = skillsConfig.get().getConfigurationSection("mining.blocks").getKeys(false);
        miningOrePoints = skillsConfig.get().getConfigurationSection("mining.blocks").getValues(false);
        miningTools = (List) skillsConfig.get().get("mining.tools");

        farmingCrops = skillsConfig.get().getConfigurationSection("farming.crops").getKeys(false);
        farmingCropsPoints = skillsConfig.get().getConfigurationSection("farming.crops").getValues(false);
        farmingTools = (List) skillsConfig.get().get("farming.tools");
        farmingReplantableCrops = (List) skillsConfig.get().get("farming.replantables");

        skillsGainedXpMessage = plugin.getConfig().getString("skills.gained");
        skillsPassedLevelMessage = plugin.getConfig().getString("skills.passed_level");

        farminghoeFortune1 = new ItemStack(Material.WOODEN_HOE);
        farmingHoeFortune2 = new ItemStack(Material.WOODEN_HOE);
        farminghoeFortune3 = new ItemStack(Material.WOODEN_HOE);

        ItemMeta hoe = farminghoeFortune1.getItemMeta();
        hoe.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, false);
        farminghoeFortune1.setItemMeta(hoe);
        hoe.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 2, false);
        farmingHoeFortune2.setItemMeta(hoe);
        hoe.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3, false);
        farminghoeFortune3.setItemMeta(hoe);

        choppingLogs = skillsConfig.get().getConfigurationSection("chopping.logs").getKeys(false);
        choppingLogsPoints = skillsConfig.get().getConfigurationSection("chopping.logs").getValues(false);
        choppingSaplings = skillsConfig.get().getConfigurationSection("chopping.saplings").getKeys(false);
        choppingSaplingsPoints = skillsConfig.get().getConfigurationSection("chopping.saplings").getValues(false);
        choppingLogvityRange = (List<Integer>) skillsConfig.get().get("chopping.logvity_ranges");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        Block block = event.getBlock();
        ItemStack itemInMainHand = p.getInventory().getItem(p.getInventory().getHeldItemSlot());

        // MINING
        if (miningOres.contains(block.getType().toString()) && miningTools.contains(p.getInventory().getItemInMainHand().getType().toString())) {
            // Points
            Double pointsGained = helper.round((Double) miningOrePoints.get(block.getType().toString()), 2);
            survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "mining", pointsGained);
            helper.sendActionBar(p, skillsGainedXpMessage.replace("%amount%", String.valueOf(pointsGained)).replace("%skill%", "minage"));

            // Levels
            handlePlayerSkillLevel(p, "mining");

            // Autosmelt
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

            // ? Vein Mine
            if (survivalData.getPlayerUpgrade(uuid, "vein_mine") > 0) {
                int range = 4;
                if (miningOres.contains(block.getType().toString())) {
//
                    // ! DIRECTION DEPENDENT - SUCKS
//                    for (int z = -range; z < range; z++) {
//                        for (int y = -range; y < range; y++) {
//                            for (int x = -range; x < range; x++) {
//                                Block toMine = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);
//                                if (toMine.getType() == block.getType()) {
//                                    toMine.breakNaturally(p.getInventory().getItemInMainHand());
//                                }
//                            }
//                        }
//                    }
                    // ! WAY TOO HARD TO SCALE - SUCKS
                    for (int[] pos : veinMinerArea) {
                        if (!miningTools.contains(p.getInventory().getItemInMainHand().getType().toString())) return;
                        Block veinMinedBlock = block.getWorld().getBlockAt(block.getX() + pos[0], block.getY() + pos[1], block.getZ() + pos[2]);
                        if (veinMinedBlock.getType() == block.getType()) {
                            veinMinedBlock.breakNaturally(p.getItemInUse());
                        }
                    }
                }
            }
            return;
        }

        // FARMING
        if (itemInMainHand != null && farmingTools.contains(itemInMainHand.getType().toString()) && farmingCrops.contains(block.getType().toString())) {
            Ageable ageable = (Ageable) block.getBlockData();
            if (ageable.getAge() != ageable.getMaximumAge()) return;

            // Points
            Double pointsGained = helper.round((Double) farmingCropsPoints.getOrDefault(block.getType().toString(), 0.0), 2);
            survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "chopping", pointsGained);
            helper.sendActionBar(p, skillsGainedXpMessage.replace("%amount%", String.valueOf(pointsGained)).replace("%skill%", "agriculture"));

            // Levels
            handlePlayerSkillLevel(p, "chopping");

            // Replant
            if (survivalData.getPlayerUpgrade(uuid, "replanter") > 0 && farmingReplantableCrops.contains(block.getType().toString())) {
                event.setCancelled(true);
                Material blockType = block.getType();

                // Apply fortune
                Integer playerReplanterFortuneLevel = survivalData.getPlayerUpgrade(uuid, "replanter_fortune");
                if (playerReplanterFortuneLevel > 0) {
                    switch (playerReplanterFortuneLevel) {
                        case 1:
                            block.breakNaturally(farminghoeFortune1);
                            break;
                        case 2:
                            block.breakNaturally(farmingHoeFortune2);
                            break;
                        case 3:
                            block.breakNaturally(farminghoeFortune3);
                            break;
                    }
                } else block.breakNaturally();

                // Replant
                placeBlockTask = new PlaceBlockTask(plugin, block.getLocation(), blockType);
                placeBlockTask.runTaskLater(plugin, 20L);
            }
            return;
        }

        // CHOPPING
        if (choppingLogs.contains(block.getType().toString())) {
            // Points
            Double pointsGained = helper.round((Double) choppingLogsPoints.getOrDefault(block.getType().toString(), 0.0), 2);
            survivalData.incrementPlayerSkillPoints(uuid, "chopping", pointsGained);
            helper.sendActionBar(p, skillsGainedXpMessage.replace("%amount%", String.valueOf(pointsGained)).replace("%skill%", "bûcheronnage"));

            // Levels
            handlePlayerSkillLevel(p, "chopping");

            // ? LOGVITY
            Integer playerLogvityUpgradeLevel = survivalData.getPlayerUpgrade(uuid, "logvity");
            if (playerLogvityUpgradeLevel > 0) {
                for (int y = 1; y <= choppingLogvityRange.get(playerLogvityUpgradeLevel - 1); y++) {
                    Block toChopp = block.getWorld().getBlockAt(block.getX(), block.getY() + y, block.getZ());
                    if (toChopp.getType() != block.getType()) return;
                    toChopp.breakNaturally(p.getInventory().getItemInMainHand());
                    helper.applyDamage(p.getInventory().getItemInMainHand(), -1);
                }
            }
        }
    }

    public void handlePlayerSkillLevel(Player p, String skill) {
        int playerSkillLevel = survivalData.getPlayerSkillLevel(p.getUniqueId(), skill);
        Double playerSkillPoints = survivalData.getPlayerSkillPoints(p.getUniqueId(), skill);
        for (int i = 0; i <= pointsForLevels.size(); i++) {
            if (playerSkillLevel == pointsForLevels.size()) return;
            if (playerSkillLevel == i && playerSkillPoints >= (int) pointsForLevels.get(i)) {
                survivalData.incrementPlayerSkillLevel(p.getUniqueId(), skill, 1);
                survivalData.incrementPlayerTokens(p.getUniqueId(), 1);
                p.sendMessage(skillsPassedLevelMessage.replace("%level%", String.valueOf(i + 1)));
            }
        }
    }
}