package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.SkillsMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FarmingSkillMenu implements Listener {
    private SurvivalPlus plugin;
    private SurvivalData survivalData;
    private Helper helper;
    private SkillsMenu skillsMenu;
    private String inventoryName = "§6§lAgriculture";
    private int replanterPrice = 1;
    private int replanterFortunePrice = 2;

    public FarmingSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
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
        int slot = event.getSlot();

        if (slot == 10) {
            if (helper.buyUpgrade(p, "replanter", 1)) helper.sendUpgradeBoughtMessage(p, "replanteur", 1);
            else helper.sendNotEnoughTokensMessage(p);
        } else if (slot == 12) {
            if (helper.buyUpgrade(p, "replanter_fortune", 1))
                helper.sendUpgradeBoughtMessage(p, "fortune du replanteur", 1);
            else helper.sendNotEnoughTokensMessage(p);
        }

        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }


    public Inventory createMenu(Player p) {
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, backgroundItem, true);

        ItemStack replanterFortuneItem = helper.getItem(new ItemStack(Material.PISTON), "§6§lFortune du Replanteur", "§ePrix: §6" + replanterFortunePrice);
        ItemMeta replanterFortuneItemMeta = replanterFortuneItem.getItemMeta();
        replanterFortuneItemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, false);
        replanterFortuneItem.setItemMeta(replanterFortuneItemMeta);

        inventory.setItem(11, helper.getItem(new ItemStack(Material.PISTON), "§6§lReplanteur", "§ePrix: §6" + replanterPrice));
        inventory.setItem(15, replanterFortuneItem);

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
