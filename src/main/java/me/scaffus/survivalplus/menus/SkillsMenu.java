package me.scaffus.survivalplus.menus;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.PlayersData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.skills.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkillsMenu implements Listener {
    private SurvivalPlus plugin;
    private PlayersData playersData;
    private Helper helper;
    private String skillInventoryName = "§6§lSkills";

    private FarmingSkillMenu farmingSkillMenu;
    private MiningSkillMenu miningSkillMenu;
    private CombatSkillMenu combatSkillMenu;
    private RunningSkillMenu runningSkillMenu;
    private DeathSkillMenu deathSkillMenu;
    private ArcherySkillMenu archerySkillMenu;
    private SwimmingSkillMenu swimmingSkillMenu;
    private FlyingSkillMenu flyingSkillMenu;

    public SkillsMenu(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.playersData = plugin.playersData;
        this.helper = plugin.helper;

        this.farmingSkillMenu = new FarmingSkillMenu(plugin, this);
        this.miningSkillMenu = new MiningSkillMenu(plugin, this);
        this.combatSkillMenu = new CombatSkillMenu(plugin, this);
        this.runningSkillMenu = new RunningSkillMenu(plugin, this);
        this.deathSkillMenu = new DeathSkillMenu(plugin, this);
        this.archerySkillMenu = new ArcherySkillMenu(plugin, this);
        this.swimmingSkillMenu = new SwimmingSkillMenu(plugin, this);
        this.flyingSkillMenu = new FlyingSkillMenu(plugin, this);

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
            if (slot == 30) p.openInventory(archerySkillMenu.createMenu(p));
            if (slot == 32) p.openInventory(swimmingSkillMenu.createMenu(p));
            if (slot == 34) p.openInventory(flyingSkillMenu.createMenu(p));

            if (slot == event.getInventory().getSize() - 1) p.closeInventory();
        }
    }

    public Inventory createSkillMenu(Player p) {
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, skillInventoryName, 54, backgroundItem, false);

        inventory.setItem(10, helper.getItem(
                new ItemStack(Material.GOLDEN_HOE), "§6§lAgriculture", "§eXp: §6" + playersData.getPlayerSkillPoints(p.getUniqueId(), "farming"),
                "§eNiveau: §6" + playersData.getPlayerSkillLevel(p.getUniqueId(), "farming")));
        inventory.setItem(12, helper.getItem(
                new ItemStack(Material.DIAMOND_PICKAXE), "§6§lMinage", "§eXp: §6" + playersData.getPlayerSkillPoints(p.getUniqueId(), "mining"),
                "§eNiveau: §6" + playersData.getPlayerSkillLevel(p.getUniqueId(), "mining")));
        inventory.setItem(14, helper.getItem(
                new ItemStack(Material.NETHERITE_SWORD), "§6§lCombat", "§eXp: §6" + playersData.getPlayerSkillPoints(p.getUniqueId(), "combat"),
                "§eNiveau: §6" + playersData.getPlayerSkillLevel(p.getUniqueId(), "combat")));
        inventory.setItem(16, helper.getItem(
                new ItemStack(Material.LEATHER_BOOTS), "§6§lCourse", "§eXp: §6" + playersData.getPlayerSkillPoints(p.getUniqueId(), "running"),
                "§eNiveau: §6" + playersData.getPlayerSkillLevel(p.getUniqueId(), "running")));

        inventory.setItem(28, helper.getItem(
                new ItemStack(Material.SKELETON_SKULL), "§6§lMort", "§eXp: §6" + playersData.getPlayerSkillPoints(p.getUniqueId(), "death"),
                "§eNiveau: §6" + playersData.getPlayerSkillLevel(p.getUniqueId(), "death")));
        inventory.setItem(30, helper.getItem(
                new ItemStack(Material.BOW), "§6§lArcher", "§eXp: §6" + playersData.getPlayerSkillPoints(p.getUniqueId(), "archery"),
                "§eNiveau: §6" + playersData.getPlayerSkillLevel(p.getUniqueId(), "archery")));
        inventory.setItem(32, helper.getItem(
                new ItemStack(Material.CONDUIT), "§6§lNage", "§eXp: §6" + playersData.getPlayerSkillPoints(p.getUniqueId(), "swimming"),
                "§eNiveau: §6" + playersData.getPlayerSkillLevel(p.getUniqueId(), "swimming")));
        inventory.setItem(34, helper.getItem(
                new ItemStack(Material.ELYTRA), "§6§lVol", "§eXp: §6" + playersData.getPlayerSkillPoints(p.getUniqueId(), "flying"),
                "§eNiveau: §6" + playersData.getPlayerSkillLevel(p.getUniqueId(), "flying")));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + playersData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
