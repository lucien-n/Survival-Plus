package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.SkillsMenu;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ChoppingSkillMenu implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final Helper helper;
    private final SkillsConfig skillsConfig;
    private final SkillsMenu skillsMenu;
    private final String inventoryName = "§6§lBûcheronnage";
    private final List<Double> logvityRanges;

    private final PlayerUpgrade destripUpgrade;
    private final PlayerUpgrade logvityUpgrade;

    public ChoppingSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        this.skillsConfig = plugin.skillsConfig;
        this.skillsMenu = skillsMenu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        logvityRanges = (List) skillsConfig.get().get("chopping.logvity_ranges");
        destripUpgrade = survivalData.getUpgrade("destrip");
        logvityUpgrade = survivalData.getUpgrade("logvity");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (!survivalData.canPlayerClick(p.getUniqueId())) return;

        if (slot == 20) skillsMenu.buyUpgrade(p, destripUpgrade);
        if (slot == 24) skillsMenu.buyUpgrade(p, logvityUpgrade);

        survivalData.setPlayerLastClicked(p.getUniqueId());
        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, true);



        inventory.setItem(20, skillsMenu.getUpgradeMenuItem(destripUpgrade, uuid));
        inventory.setItem(24, skillsMenu.getUpgradeMenuItem(logvityUpgrade, uuid));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
