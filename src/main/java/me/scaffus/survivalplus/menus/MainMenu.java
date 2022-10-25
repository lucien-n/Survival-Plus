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

import java.util.UUID;

public class MainMenu implements Listener {
    private final SurvivalPlus plugin;
    private final Helper helper;
    private final SurvivalData survivalData;

    private final String inventoryName = "§6§lMenu";
    private final SkillsMenu skillsMenu;
    private final BankMenu bankMenu;

    public MainMenu(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.helper = plugin.helper;
        this.survivalData = plugin.survivalData;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        skillsMenu = new SkillsMenu(plugin);
        bankMenu = new BankMenu(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (!survivalData.canPlayerClick(p.getUniqueId())) return;

        if (slot == 11) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == 15) p.openInventory(bankMenu.createBankMenu(p));

        survivalData.setPlayerLastClicked(p.getUniqueId());
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 27, false);

        inventory.setItem(11, helper.getItem(new ItemStack(Material.DIAMOND_BLOCK), "§6§lBanque"));
        inventory.setItem(15, helper.getItem(new ItemStack(Material.NETHERITE_HOE), "§6§lSkills"));

        return inventory;
    }
}
