package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.SkillsMenu;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class MiningSkillMenu implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final Helper helper;
    private final SkillsMenu skillsMenu;
    private final String inventoryName = "§6§lMinage";
    private final Integer autoSmeltCost;
    private final Integer veinMineCost;
    private final Integer magnetCost;
    private final Integer magnetCostFactor;
    private final List<Double> magnetRanges;

    public MiningSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        this.skillsConfig = plugin.skillsConfig;
        this.skillsMenu = skillsMenu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        magnetRanges = (List<Double>) skillsConfig.get().get("mining.magnet_ranges");
        autoSmeltCost = (Integer) skillsConfig.get().get("upgrades.auto_smelt.cost");
        veinMineCost = (Integer) skillsConfig.get().get("upgrades.vein_mine.cost");
        magnetCost = (Integer) skillsConfig.get().get("upgrades.magnet.cost");
        magnetCostFactor = (Integer) skillsConfig.get().get("upgrades.magnet.factor");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        UUID uuid = p.getUniqueId();
        int slot = event.getSlot();

        Integer playerAutoSmeltLevel = survivalData.getPlayerUpgrade(uuid, "auto_smelt");
        Integer playerVeinMineLevel = survivalData.getPlayerUpgrade(uuid, "vein_mine");

        if (slot == 11 && survivalData.getPlayerTokens(uuid) >= autoSmeltCost && playerAutoSmeltLevel < 1) {
            if (playerVeinMineLevel > 0) survivalData.setPlayerUpgrade(uuid, "vein_mine", 0);
            skillsMenu.buyUpgrade(p, survivalData.getUpgrade("auto_smelt"));
        } else if (slot == 15) {
            if (playerAutoSmeltLevel > 0) survivalData.setPlayerUpgrade(uuid, "auto_smelt", 0);
            skillsMenu.buyUpgrade(p, survivalData.getUpgrade("vein_mine"));
        } else if (slot == 31) skillsMenu.buyUpgrade(p, survivalData.getUpgrade("magnet"));

        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, true);

        PlayerUpgrade autoSmelt = survivalData.getUpgrade("auto_smelt");
        PlayerUpgrade veinMiner = survivalData.getUpgrade("vein_mine");
        PlayerUpgrade magnet = survivalData.getUpgrade("magnet");

        inventory.setItem(11, skillsMenu.getUpgradeMenuItem(autoSmelt, uuid));
        inventory.setItem(15, skillsMenu.getUpgradeMenuItem(veinMiner, uuid));
        inventory.setItem(31, skillsMenu.getUpgradeMenuItem(magnet, uuid));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
