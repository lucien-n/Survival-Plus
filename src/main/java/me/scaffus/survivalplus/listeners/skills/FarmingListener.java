package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.SkillHelper;
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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FarmingListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillHelper skillHelper;
    private final SkillsConfig skillsConfig;

    private final Set crops;
    private final Map points;
    private final List<Material> tools;
    private final List<Material> replantables;

    private PlaceBlockTask placeBlockTask;
    private ItemStack farminghoeFortune1;
    private ItemStack farmingHoeFortune2;
    private ItemStack farminghoeFortune3;
    private List<Material> tillables;

    public FarmingListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillHelper = plugin.skillHelper;
        this.skillsConfig = plugin.skillsConfig;

        crops = skillsConfig.get().getConfigurationSection("blocks.farming").getKeys(false);
        points = skillsConfig.get().getConfigurationSection("blocks.farming").getValues(false);
        tools = (List<Material>) skillsConfig.get().get("tools.farming");
        tillables = (List<Material>) skillsConfig.get().get("farming.tillables");
        replantables = (List<Material>) skillsConfig.get().get("farming.replantables");

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
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        if (!(crops.contains(block.getType()))) return;
        ItemStack tool = p.getInventory().getItemInMainHand();
        if (!(tools.contains(tool.getType()))) return;
        UUID uuid = p.getUniqueId();

        Ageable ageable = (Ageable) block.getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge()) return;

        Double pointsGained = (Double) points.getOrDefault(block.getType().toString(), 0.0);
        skillHelper.handleSkillGain(p, pointsGained, "farming");

        // * Replant
        if (survivalData.getPlayerUpgrade(uuid, "replanter") > 0 && replantables.contains(block.getType().toString())) {
            event.setCancelled(true);
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
            placeBlockTask = new PlaceBlockTask(plugin, block.getLocation(), block.getType());
            placeBlockTask.runTaskLater(plugin, 20L);
        }
        return;
    }
}
