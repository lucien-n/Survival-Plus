package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.SkillsMenu;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class DeathSkillMenu implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final Helper helper;
    private final SkillsConfig skillsConfig;
    private final SkillsMenu skillsMenu;
    private final String inventoryName = "§6§lMort";

    public DeathSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        this.skillsConfig = plugin.skillsConfig;
        this.skillsMenu = skillsMenu;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(inventoryName))) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == 11) {
            if (skillsMenu.buyUpgrade(p, survivalData.getUpgrade("cat_life"))) {
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 2.0);
                p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 10));
            }
        } else if (slot == 15) skillsMenu.buyUpgrade(p, survivalData.getUpgrade("limited_immortality"));

        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, true);

        PlayerUpgrade catLife = survivalData.getUpgrade("cat_life");
        PlayerUpgrade limitedImmortality = survivalData.getUpgrade("limited_immortality");

        Integer playerCatLifeUpgradeLevel = survivalData.getPlayerUpgrade(uuid, catLife.name);
        inventory.setItem(11, helper.getItem(new ItemStack(catLife.displayItem), catLife.displayName, "§eChaque niveau t'offres un coeur",
                "",
                "§eNiveau: §6" + playerCatLifeUpgradeLevel,
                skillsMenu.getUpgradePrice(p, survivalData.getUpgrade(catLife.name))));

        inventory.setItem(15, helper.getItem(new ItemStack(limitedImmortality.displayItem), limitedImmortality.displayName, "§eUn ange gardien te sauvera à ta prochaine mort",
                "", skillsMenu.getUpgradePrice(p, survivalData.getUpgrade(limitedImmortality.name))));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
