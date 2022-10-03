package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.PlayersData;
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

import java.util.UUID;

public class FarmingSkillMenu implements Listener {
    private SurvivalPlus plugin;
    private PlayersData pData;
    private Helper helper;
    private SkillsMenu skillsMenu;
    private String inventoryName = "§6§lAgriculture";
    private Integer replanterPrice = 1;
    private Integer wideTillePrice = 2;


    public FarmingSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.pData = plugin.pData;
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

        Integer playerReplanterLevel = pData.getPlayerUpgrade(uuid, "replanter");
        Integer playerReplanterFortuneLevel = pData.getPlayerUpgrade(uuid, "replanter_fortune");
        Integer playerWideTillLevel = pData.getPlayerUpgrade(uuid, "wide_till");

        if (slot == 11 && pData.getPlayerTokens(p.getUniqueId()) >= replanterPrice && playerReplanterLevel < 1) {
            pData.setPlayerUpgrade(uuid, "replanter", playerReplanterLevel + 1);
            pData.incrementPlayerTokens(p.getUniqueId(), -replanterPrice);
            p.sendMessage(helper.upgradeBoughtMessage(pData.upgradeBought,
                    "replanteur niveau " + playerReplanterLevel + 1, replanterPrice));
        } else if (slot == 15 && pData.getPlayerTokens(p.getUniqueId()) >= (playerReplanterFortuneLevel + 1) * 2 && playerReplanterFortuneLevel < 3) {
            pData.setPlayerUpgrade(uuid, "replanter_fortune", playerReplanterFortuneLevel + 1);
            pData.incrementPlayerTokens(p.getUniqueId(), -(playerReplanterFortuneLevel + 1) * 2);
            p.sendMessage(helper.upgradeBoughtMessage(pData.upgradeBought,
                    "fortune du replanteur niveau " + (playerReplanterFortuneLevel + 1), (playerReplanterFortuneLevel + 1) * 2));
        } else if (slot == 31 && pData.getPlayerTokens(p.getUniqueId()) >= wideTillePrice && pData.getPlayerUpgrade(uuid, "wide_till") < 1) {
            pData.setPlayerUpgrade(uuid, "wide_till", playerWideTillLevel + 1);
            pData.incrementPlayerTokens(p.getUniqueId(), -replanterPrice);
            p.sendMessage(helper.upgradeBoughtMessage(pData.upgradeBought,
                    "large bêche niveau " + playerWideTillLevel + 1, wideTillePrice));
        }

        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }


    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, backgroundItem, true);

        // FORTUNE PRICE IS 2 TIMES THE DESIRED FORTUNE LEVEL
        Integer playerReplanterFortuneLevel = pData.getPlayerUpgrade(uuid, "replanter_fortune");
        ItemStack replanterFortuneItem = helper.getItem(new ItemStack(Material.PISTON), "§6§lFortune du Replanteur",
                playerReplanterFortuneLevel == 0 ? "§ePrix: §6" + (playerReplanterFortuneLevel + 1) * 2 :
                        playerReplanterFortuneLevel == 1 ? "§ePrix: §6" + (playerReplanterFortuneLevel + 1) * 2 :
                                playerReplanterFortuneLevel == 2 ? "§ePrix: §6" + (playerReplanterFortuneLevel + 1) * 2 :
                                        "§ePrix: §6Acquit", "§eRequiert: §6Replanteur");
        ItemMeta replanterFortuneItemMeta = replanterFortuneItem.getItemMeta();
        replanterFortuneItemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS,
                playerReplanterFortuneLevel == 3 ? 3 :
                        playerReplanterFortuneLevel + 1, false);
        replanterFortuneItem.setItemMeta(replanterFortuneItemMeta);

        inventory.setItem(11, helper.getItem(new ItemStack(Material.PISTON), "§6§lReplanteur",
                pData.getPlayerUpgrade(uuid, "replanter")
                        > 0 ? "§ePrix: §6Acquit" : "§ePrix: §6" + replanterPrice));
        inventory.setItem(15, replanterFortuneItem);
        inventory.setItem(31, helper.getItem(new ItemStack(Material.DIAMOND_HOE), "§6§lLarge Bêche",
                pData.getPlayerUpgrade(uuid, "wide_till")
                        > 0 ? "§ePrix: §6Acquit" : "§ePrix: §6" + wideTillePrice, "§eBêche une zone §63x3"));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + pData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}