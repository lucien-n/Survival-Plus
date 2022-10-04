package me.scaffus.survivalplus.menus;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.skills.*;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SkillsMenu implements Listener {
    private SurvivalPlus plugin;
    private SurvivalData survivalData;
    private Helper helper;
    private String skillInventoryName = "§6§lSkills";

    private FarmingSkillMenu farmingSkillMenu;
    private MiningSkillMenu miningSkillMenu;
    private CombatSkillMenu combatSkillMenu;
    private RunningSkillMenu runningSkillMenu;
    private DeathSkillMenu deathSkillMenu;
    private ChoppingSkillMenu choppingSkillMenu;
    private SwimmingSkillMenu swimmingSkillMenu;
    private FlyingSkillMenu flyingSkillMenu;
//    private ToggleSkillMenu toggleSkillMenu;

    public SkillsMenu(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;

        this.farmingSkillMenu = new FarmingSkillMenu(plugin, this);
        this.miningSkillMenu = new MiningSkillMenu(plugin, this);
        this.combatSkillMenu = new CombatSkillMenu(plugin, this);
        this.runningSkillMenu = new RunningSkillMenu(plugin, this);
        this.deathSkillMenu = new DeathSkillMenu(plugin, this);
        this.choppingSkillMenu = new ChoppingSkillMenu(plugin, this);
        this.swimmingSkillMenu = new SwimmingSkillMenu(plugin, this);
        this.flyingSkillMenu = new FlyingSkillMenu(plugin, this);

//        this.toggleSkillMenu = new ToggleSkillMenu(plugin, this);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryName = event.getView().getTitle();
        if (inventoryName.equals(skillInventoryName)) {
            event.setCancelled(true);

            Player p = (Player) event.getWhoClicked();
            int slot = event.getSlot();

            if (slot == 10) p.openInventory(farmingSkillMenu.createMenu(p));
            if (slot == 12) p.openInventory(miningSkillMenu.createMenu(p));
            if (slot == 14) p.openInventory(combatSkillMenu.createMenu(p));
            if (slot == 16) p.openInventory(runningSkillMenu.createMenu(p));
            if (slot == 28) p.openInventory(deathSkillMenu.createMenu(p));
            if (slot == 30) p.openInventory(choppingSkillMenu.createMenu(p));
            if (slot == 32) p.openInventory(swimmingSkillMenu.createMenu(p));
            if (slot == 34) p.openInventory(flyingSkillMenu.createMenu(p));

//            if (slot == 45) p.openInventory(toggleSkillMenu.createMenu(p));

            if (slot == event.getInventory().getSize() - 1) p.closeInventory();
        }
    }

    public Inventory createSkillMenu(Player p) {
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, skillInventoryName, 54, backgroundItem, false);

        inventory.setItem(10, helper.getItem(
                new ItemStack(Material.GOLDEN_HOE), "§6§lAgriculture", "§eXp: §6" +
                        helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), "farming")).replace(" ", " "),
                "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), "farming")));
        inventory.setItem(12, helper.getItem(
                new ItemStack(Material.DIAMOND_PICKAXE), "§6§lMinage", "§eXp: §6" +
                        helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), "mining")).replace(" ", " "),
                        "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), "mining")));
        inventory.setItem(14, helper.getItem(
                new ItemStack(Material.NETHERITE_SWORD), "§6§lCombat", "§eXp: §6" +
                        helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), "combat")).replace(" ", " "),
                        "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), "combat")));
        inventory.setItem(16, helper.getItem(
                new ItemStack(Material.LEATHER_BOOTS), "§6§lCourse", "§eXp: §6" +
                        helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), "running")).replace(" ", " "),
                        "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), "running")));

        inventory.setItem(28, helper.getItem(
                new ItemStack(Material.SKELETON_SKULL), "§6§lMort", "§eXp: §6" +
                        helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), "death")).replace(" ", " "),
                "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), "death")));
        inventory.setItem(30, helper.getItem(
                new ItemStack(Material.STONE_AXE), "§6§lBûcheronnage", "§eXp: §6" +
                        helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), "chopping")).replace(" ", " "),
                "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), "chopping")));
        inventory.setItem(32, helper.getItem(
                new ItemStack(Material.CONDUIT), "§6§lNage", "§eXp: §6" +
                        helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), "swimming")).replace(" ", " "),
                "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), "swimming")));
        inventory.setItem(34, helper.getItem(
                new ItemStack(Material.ELYTRA), "§6§lVol", "§eXp: §6" +
                        helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), "flying")).replace(" ", " "),
                "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), "flying")));

//        inventory.setItem(45, helper.getItem(new ItemStack(Material.REDSTONE_TORCH), "§6§lSkills", "§eActiver/Désactiver un de tes skill"));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }

    public Boolean buyUpgrade(Player p, PlayerUpgrade upgrade) {
        UUID uuid = p.getUniqueId();
        Integer playerUpgradeLevel = survivalData.getPlayerUpgrade(uuid, upgrade.name);
        Integer cost = (playerUpgradeLevel + 1) * upgrade.cost;
        if (survivalData.getPlayerTokens(uuid) < cost) return false;
        if (playerUpgradeLevel >= upgrade.maxLevel) return false;

        survivalData.setPlayerUpgrade(uuid, upgrade.name, playerUpgradeLevel + 1);
        survivalData.incrementPlayerTokens(uuid, -cost);
        p.sendMessage(helper.upgradeBoughtMessage(survivalData.upgradeBought,
                "§6§n" + upgrade.displayName + "§6 niveau " + (playerUpgradeLevel + 1), cost));
        return true;
    }

    public String getUpgradePrice(Player p, PlayerUpgrade upgrade) {
        UUID uuid = p.getUniqueId();
        Integer playerUpgradelevel = survivalData.getPlayerUpgrade(uuid, upgrade.name);
        String priceText = "§ePrix: §6";
        String acquiredText = "Acquit";
        if (playerUpgradelevel.equals(upgrade.maxLevel)) return priceText + acquiredText;
        return priceText + ((playerUpgradelevel + 1) * upgrade.cost);
    }
}
