package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.SkillsMenu;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class ExplorerSkillMenu implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final Helper helper;
    private final SkillsMenu skillsMenu;
    private final String inventoryName = "§6§lCourse";

    private static PlayerUpgrade speedUpgrade;
    private static PlayerUpgrade breathUpgrade;
    private static PlayerUpgrade dolphinUpgrade;

    public ExplorerSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        this.skillsMenu = skillsMenu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        speedUpgrade = survivalData.getUpgrade("speed");
        breathUpgrade = survivalData.getUpgrade("breath");
        dolphinUpgrade = survivalData.getUpgrade("dolphin");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (!survivalData.canPlayerClick(p.getUniqueId())) return;

        if (slot == 11) skillsMenu.buyUpgrade(p, speedUpgrade);
        if (slot == 15) skillsMenu.buyUpgrade(p, breathUpgrade);
        if (slot == 31) skillsMenu.buyUpgrade(p, dolphinUpgrade);

        survivalData.setPlayerLastClicked(p.getUniqueId());
        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, true);

        inventory.setItem(11, skillsMenu.getUpgradeMenuItem(speedUpgrade, uuid));
        inventory.setItem(15, skillsMenu.getUpgradeMenuItem(breathUpgrade, uuid));
        inventory.setItem(31, skillsMenu.getUpgradeMenuItem(dolphinUpgrade, uuid));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
