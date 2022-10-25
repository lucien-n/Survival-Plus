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

public class FlyingSkillMenu implements Listener {
    private SurvivalPlus plugin;
    private SurvivalData survivalData;
    private Helper helper;
    private SkillsMenu skillsMenu;
    private String inventoryName = "§6§lVol";
    private static PlayerUpgrade leapUpgrade;
    private static PlayerUpgrade doubleJumpUpgrade;

    public FlyingSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        this.skillsMenu = skillsMenu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
//        leapUpgrade = survivalData.getUpgrade("leap");
        doubleJumpUpgrade = survivalData.getUpgrade("double_jump");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (!survivalData.canPlayerClick(p.getUniqueId())) return;

//        if (slot == 11) skillsMenu.buyUpgrade(p, leapUpgrade);
        if (slot == 22) {
            skillsMenu.buyUpgrade(p, doubleJumpUpgrade);
            p.setAllowFlight(true);
        }

        survivalData.setPlayerLastClicked(p.getUniqueId());
        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, true);

//        inventory.setItem(11, skillsMenu.getUpgradeMenuItem(leapUpgrade, uuid));
        inventory.setItem(22, skillsMenu.getUpgradeMenuItem(doubleJumpUpgrade, uuid));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
