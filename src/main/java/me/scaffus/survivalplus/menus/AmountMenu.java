package me.scaffus.survivalplus.menus;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AmountMenu implements Listener {
    private SurvivalPlus plugin;
    private Helper helper;
    private String inventoryName = "§6§lMontant";
    private Material material;

    public AmountMenu(SurvivalPlus plugin, Material material) {
        this.plugin = plugin;
        this.helper = plugin.helper;
        this.material = material;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(inventoryName)) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        int amount = 0;

        if (slot == 10) amount += 1;
        if (slot == 11) amount += 5;
        if (slot == 12) amount += 10;
        if (slot == 13) amount += 32;
        if (slot == 14) amount += 64;
        if (slot == 15) amount = helper.getAmountOfItemInventory(p.getInventory(), material);
        if (slot == )
    }

    public Inventory createMenu(Player p) {
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 36, backgroundItem);

        inventory.setItem(4, helper.getItem(new ItemStack(Material.GOLD_BLOCK), "§6§lMontant", "§e0"));

        inventory.setItem(10, helper.getItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§a+1"));
        inventory.setItem(11, helper.getItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§a+5"));
        inventory.setItem(12, helper.getItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§a+10"));
        inventory.setItem(13, helper.getItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§a+32"));
        inventory.setItem(14, helper.getItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§a+64"));
        inventory.setItem(15, helper.getItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§aTout"));

        return inventory;
    }
}
