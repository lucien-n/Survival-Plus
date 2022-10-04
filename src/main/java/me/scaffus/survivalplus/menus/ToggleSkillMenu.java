package me.scaffus.survivalplus.menus;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ToggleSkillMenu implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final Helper helper;
    private final SkillsMenu skillsMenu;
    private final String inventoryName = "§6§lSkill ON/OFF";

    public ToggleSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        this.skillsMenu = skillsMenu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        UUID uuid = p.getUniqueId();
        int slot = event.getSlot();


        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.AIR), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 27, backgroundItem, true);

        HashMap<String, Integer> playerUpgrades = survivalData.playerUpgrades.get(uuid);

        int i = 0;
        for (String upgrade : playerUpgrades.keySet()) {
            ItemStack item = new ItemStack(Material.AIR);
            switch (upgrade) {
                // ? Mining
                case "magnet":
                    item.setType(Material.ENDER_EYE);
                    break;
                case "auto_smelt":
                    item.setType(Material.FURNACE);
                    break;
                case "vein_mine":
                    item.setType(Material.TNT);
                    break;
                // ? Farming
                case "wide_till":
                    item.setType(Material.DIAMOND_HOE);
                    break;
                case "replanter":
                    item.setType(Material.PISTON);
                    break;
                case "replanter_fortune":
                    item.setType(Material.STICKY_PISTON);
                    break;
            }
            inventory.setItem(i, item);
            i++;
        }

        return inventory;
    }
}
