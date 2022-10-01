package me.scaffus.survivalplus.menus;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkillMenu implements Listener {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;
    private Helper helper;
    private String skillInventoryName = "§6§lSkills";

    public SkillMenu(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.helper = plugin.helper;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryName = event.getView().getTitle();
        if (inventoryName.equals(skillInventoryName)) {
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();
            int slot = event.getSlot();

            if (slot == event.getInventory().getSize() - 1) p.closeInventory();
        }
    }

    public Inventory createSkillMenu(Player p) {
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, skillInventoryName, 54, backgroundItem, false);

        inventory.setItem(10, helper.getItem(
                new ItemStack(Material.GOLDEN_HOE), "§6§lAgriculture", "§eXp: " + data.getPlayerSkillLevel(p.getUniqueId(), "farming")));
        inventory.setItem(12, helper.getItem(
                new ItemStack(Material.DIAMOND_PICKAXE), "§6§lMinage", "§eXp: " + data.getPlayerSkillLevel(p.getUniqueId(), "mining")));
        inventory.setItem(14, helper.getItem(
                new ItemStack(Material.NETHERITE_SWORD), "§6§lCombat", "§eXp: " + data.getPlayerSkillLevel(p.getUniqueId(), "combat")));
        inventory.setItem(16, helper.getItem(
                new ItemStack(Material.LEATHER_BOOTS), "§6§lCourse", "§eXp: " + data.getPlayerSkillLevel(p.getUniqueId(), "running")));

        inventory.setItem(28, helper.getItem(
                new ItemStack(Material.SKELETON_SKULL), "§6§lMort", "§eXp: " + data.getPlayerSkillLevel(p.getUniqueId(), "death")));
        inventory.setItem(30, helper.getItem(
                new ItemStack(Material.BOW), "§6§lArcher", "§eXp: " + data.getPlayerSkillLevel(p.getUniqueId(), "archery")));
        inventory.setItem(32, helper.getItem(
                new ItemStack(Material.CONDUIT), "§6§lNage", "§eXp: " + data.getPlayerSkillLevel(p.getUniqueId(), "swimming")));
        inventory.setItem(34, helper.getItem(
                new ItemStack(Material.ELYTRA), "§6§lVol", "§eXp: " + data.getPlayerSkillLevel(p.getUniqueId(), "flying")));

        return inventory;
    }
}
