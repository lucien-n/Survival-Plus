package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.SkillsMenu;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
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
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, backgroundItem, true);

        PlayerUpgrade replanter = survivalData.getUpgrade("replanter");
        PlayerUpgrade replanterFortune = survivalData.getUpgrade("replanter_fortune");
        PlayerUpgrade wideTill = survivalData.getUpgrade("wide_till");

        inventory.setItem(11, helper.getItem(new ItemStack(replanter.displayItem), replanter.displayName, "§eReplante pour toi après que tu aies récolté",
                "",
                survivalData.getPlayerUpgrade(uuid, replanter.name)
                        > 0 ? "§ePrix: §6Acquit" : "§ePrix: §6" + replanter.cost));

        Integer playerReplanterFortuneLevel = survivalData.getPlayerUpgrade(uuid, replanterFortune.name);
        inventory.setItem(15, helper.getItem(new ItemStack(replanterFortune.displayItem), replanterFortune.displayName, "§eFortune fonctionne sur les plantes sans", "§eavoir à enchanté ton outil",
                "",
                skillsMenu.getUpgradePrice(p, survivalData.getUpgrade(replanterFortune.name)), "§eRequiert: §6Replanteur"));

        inventory.setItem(31, helper.getItem(new ItemStack(wideTill.displayItem), wideTill.displayName, "§eBêche une zone §63x3",
                "",
                survivalData.getPlayerUpgrade(uuid, wideTill.name)
                        > 0 ? "§ePrix: §6Acquit" : "§ePrix: §6" + wideTill.cost));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}