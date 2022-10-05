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

import java.util.UUID;

public class FarmingSkillMenu implements Listener {
    private final SurvivalData survivalData;
    private final Helper helper;
    private final SkillsMenu skillsMenu;
    private final String inventoryName = "§6§lAgriculture";

    public FarmingSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        SkillsConfig skillsConfig = plugin.skillsConfig;
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

        if (slot == 11) skillsMenu.buyUpgrade(p, survivalData.getUpgrade("replanter"));
        else if (slot == 15) skillsMenu.buyUpgrade(p, survivalData.getUpgrade("replanter_fortune"));
        else if (slot == 31) skillsMenu.buyUpgrade(p, survivalData.getUpgrade("wide_till"));

        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }


    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, true);

        PlayerUpgrade replanter = survivalData.getUpgrade("replanter");
        PlayerUpgrade replanterFortune = survivalData.getUpgrade("replanter_fortune");
        PlayerUpgrade wideTill = survivalData.getUpgrade("wide_till");

        inventory.setItem(11, skillsMenu.getUpgradeMenuItem(replanter, uuid));

        inventory.setItem(15, skillsMenu.getUpgradeMenuItem(replanterFortune, uuid));

        inventory.setItem(31, skillsMenu.getUpgradeMenuItem(wideTill, uuid));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}