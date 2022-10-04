package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SkillsConfig;
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

import java.util.List;
import java.util.UUID;

public class ChoppingSkillMenu implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final Helper helper;
    private final SkillsConfig skillsConfig;
    private final SkillsMenu skillsMenu;
    private final String inventoryName = "§6§lArcher";
    private final Integer destripCost;
    private final Integer logvityCost;
    private final List<Double> logvityRanges;

    public ChoppingSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        this.skillsConfig = plugin.skillsConfig;
        this.skillsMenu = skillsMenu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        logvityRanges = (List) skillsConfig.get().get("chopping.logvity_ranges");
        destripCost = (Integer) skillsConfig.get().get("upgrades.destrip.price");
        logvityCost = (Integer) skillsConfig.get().get("upgrades.logvity.price");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == 11) skillsMenu.buyUpgrade(p, survivalData.getUpgrade("destrip"));
        if (slot == 15) skillsMenu.buyUpgrade(p, survivalData.getUpgrade("logvity"));

        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, backgroundItem, true);

        inventory.setItem(11, helper.getItem(new ItemStack(Material.STRIPPED_OAK_LOG), "§6§lRéécorceur", "§eCliques droit sur une bûche écorcée", "§epour magiquement lui remetrre son écorce", "", survivalData.getPlayerUpgrade(uuid, "destrip") > 0 ? "§ePrix: §6Acquit" : "§ePrix: §6" + destripCost));

        Integer playerLogvityUpgradeLevel = survivalData.getPlayerUpgrade(uuid, "logvity");
        inventory.setItem(15, helper.getItem(new ItemStack(Material.BEACON), "§6§lTronvité", "§eLe tronc est \"soumis\" à la \"gravité\"", "§eCasser une bûche casse les autres bûches", "§eau dessus. Augmente à chaque niveaux", "", "§eNiveau: §6" + playerLogvityUpgradeLevel, playerLogvityUpgradeLevel > 0 ? "§eColonne: §6" + logvityRanges.get(playerLogvityUpgradeLevel - 1) : "§eCollone: §61", playerLogvityUpgradeLevel
                == 0 ? "§ePrix: §6" + (playerLogvityUpgradeLevel + 1) * logvityCost : playerLogvityUpgradeLevel
                == 1 ? "§ePrix: §6" + (playerLogvityUpgradeLevel + 1) * logvityCost : playerLogvityUpgradeLevel
                == 2 ? "§ePrix: §6" + (playerLogvityUpgradeLevel + 1) * logvityCost : "§ePrix: §6Acquit"));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
