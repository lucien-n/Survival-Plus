package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.PlayersData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.SkillsMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RunningSkillMenu implements Listener {
    private SurvivalPlus plugin;
    private PlayersData playersData;
    private Helper helper;
    private SkillsMenu skillsMenu;
    private String inventoryName = "§6§lCourse";

    public RunningSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.playersData = plugin.playersData;
        this.helper = plugin.helper;
        this.skillsMenu = skillsMenu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, backgroundItem, true);

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + playersData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
