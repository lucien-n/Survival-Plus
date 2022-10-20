package me.scaffus.survivalplus.menus;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.skills.*;
import me.scaffus.survivalplus.objects.PlayerSkill;
import me.scaffus.survivalplus.objects.PlayerUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkillsMenu implements Listener {
    private SurvivalPlus plugin;
    private SurvivalData survivalData;
    private Helper helper;
    private String skillInventoryName = "§6§lSkills";

    private FarmingSkillMenu farmingSkillMenu;
    private MiningSkillMenu miningSkillMenu;
    private CombatSkillMenu combatSkillMenu;
    private ExplorerSkillMenu explorerSkillMenu;
    private DeathSkillMenu deathSkillMenu;
    private ChoppingSkillMenu choppingSkillMenu;
    private FlyingSkillMenu flyingSkillMenu;

    public SkillsMenu(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;

        this.farmingSkillMenu = new FarmingSkillMenu(plugin, this);
        this.miningSkillMenu = new MiningSkillMenu(plugin, this);
        this.combatSkillMenu = new CombatSkillMenu(plugin, this);
        this.explorerSkillMenu = new ExplorerSkillMenu(plugin, this);
        this.deathSkillMenu = new DeathSkillMenu(plugin, this);
        this.choppingSkillMenu = new ChoppingSkillMenu(plugin, this);
        this.flyingSkillMenu = new FlyingSkillMenu(plugin, this);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryName = event.getView().getTitle();
        if (inventoryName.equals(skillInventoryName)) {
            event.setCancelled(true);

            Player p = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            if (!survivalData.canPlayerClick(p.getUniqueId())) return;

            if (slot == 10) p.openInventory(farmingSkillMenu.createMenu(p));
            if (slot == 12) p.openInventory(miningSkillMenu.createMenu(p));
            if (slot == 14) p.openInventory(combatSkillMenu.createMenu(p));
            if (slot == 16) p.openInventory(explorerSkillMenu.createMenu(p));
            if (slot == 28) p.openInventory(deathSkillMenu.createMenu(p));
            if (slot == 30) p.openInventory(choppingSkillMenu.createMenu(p));
            if (slot == 34) p.openInventory(flyingSkillMenu.createMenu(p));

            survivalData.setPlayerLastClicked(p.getUniqueId());

            if (slot == event.getInventory().getSize() - 1) p.closeInventory();
        }


    }

    public Inventory createSkillMenu(Player p) {
        Inventory inventory = helper.createInventoryWithBackground(p, skillInventoryName, 54, false);

        PlayerSkill farmingSkill = survivalData.getSkill("farming");
        PlayerSkill miningSkill = survivalData.getSkill("mining");
        PlayerSkill combatSkill = survivalData.getSkill("combat");
        PlayerSkill explorerSkill = survivalData.getSkill("explorer");
        PlayerSkill deathSkill = survivalData.getSkill("death");
        PlayerSkill choppingSkill = survivalData.getSkill("chopping");
        PlayerSkill flyingSkill = survivalData.getSkill("flying");

        inventory.setItem(10, helper.getItem(
                new ItemStack(farmingSkill.displayMaterial), farmingSkill.displayName, getSkillLore(p, farmingSkill.id, "xp"), getSkillLore(p, farmingSkill.id, "level")));
        inventory.setItem(12, helper.getItem(
                new ItemStack(miningSkill.displayMaterial), miningSkill.displayName, getSkillLore(p, miningSkill.id, "xp"), getSkillLore(p, farmingSkill.id, "level")));
        inventory.setItem(14, helper.getItem(
                new ItemStack(combatSkill.displayMaterial), combatSkill.displayName, getSkillLore(p, combatSkill.id, "xp"), getSkillLore(p, farmingSkill.id, "level")));
        inventory.setItem(16, helper.getItem(
                new ItemStack(explorerSkill.displayMaterial), explorerSkill.displayName, getSkillLore(p, explorerSkill.id, "xp"), getSkillLore(p, farmingSkill.id, "level")));

        inventory.setItem(28, helper.getItem(
                new ItemStack(deathSkill.displayMaterial), deathSkill.displayName, getSkillLore(p, deathSkill.id, "xp"), getSkillLore(p, farmingSkill.id, "level")));
        inventory.setItem(30, helper.getItem(
                new ItemStack(choppingSkill.displayMaterial), choppingSkill.displayName, getSkillLore(p, choppingSkill.id, "xp"), getSkillLore(p, farmingSkill.id, "level")));
        inventory.setItem(34, helper.getItem(
                new ItemStack(flyingSkill.displayMaterial), flyingSkill.displayName, getSkillLore(p, flyingSkill.id, "xp"), getSkillLore(p, farmingSkill.id, "level")));

//        inventory.setItem(45, helper.getItem(new ItemStack(Material.REDSTONE_TORCH), "§6§lSkills", "§eActiver/Désactiver un de tes skill"));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + survivalData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }

    public String getSkillLore(Player p, String skill, String lore) {
        if (lore.equalsIgnoreCase("xp"))
            return "§eXp: §6" + helper.numberFormat.format(survivalData.getPlayerSkillPoints(p.getUniqueId(), skill)).replace(" ", " ");
        else if (lore.equalsIgnoreCase("level"))
            return "§eNiveau: §6" + survivalData.getPlayerSkillLevel(p.getUniqueId(), skill);
        return "§cError";
    }

    public Boolean buyUpgrade(Player p, PlayerUpgrade upgrade) {
        UUID uuid = p.getUniqueId();
        Integer playerUpgradeLevel = survivalData.getPlayerUpgrade(uuid, upgrade.id);
        Integer cost = (playerUpgradeLevel + 1) * upgrade.cost;
        if (survivalData.getPlayerTokens(uuid) < cost) {
            p.sendMessage(plugin.getConfig().getString("skills.not_enough_tokens"));
            return false;
        }
        if (playerUpgradeLevel >= upgrade.maxLevel) {
            p.sendMessage(plugin.getConfig().getString("skills.upgrade_maxed").replace("%upgrade%", upgrade.displayName));
            return false;
        }
        if (playerUpgradeLevel < 0) {
            p.sendMessage(plugin.getConfig().getString("skills.upgrade_already_bought").replace("%upgrade%", upgrade.displayName));
            return false;
        }

        survivalData.setPlayerUpgrade(uuid, upgrade.id, playerUpgradeLevel + 1);
        survivalData.incrementPlayerTokens(uuid, -cost);
        p.sendMessage(helper.upgradeBoughtMessage(survivalData.upgradeBought,
                "§6§n" + upgrade.displayName + "§6 niveau " + (playerUpgradeLevel + 1), cost));
        return true;
    }

    public ItemStack getUpgradeMenuItem(PlayerUpgrade upgrade, UUID uuid) {
        ItemStack item = new ItemStack(upgrade.displayMaterial);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', upgrade.displayName));

        List<String> lores = new ArrayList<>();
        for (String s : upgrade.lore) {
            lores.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        lores.add("");
        lores.add("§eNiveau: §6" + survivalData.getPlayerUpgrade(uuid, upgrade.id) + "/" + upgrade.maxLevel);
        lores.add(getUpgradePriceText(upgrade, survivalData.getPlayerUpgrade(uuid, upgrade.id)));

        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }

    public String getUpgradePriceText(PlayerUpgrade upgrade, Integer playerUpgradeLevel) {
        String priceText = "§ePrix: §6";
        String acquiredText = "Acquit";
        if (playerUpgradeLevel.equals(upgrade.maxLevel)) return priceText + acquiredText;
        return priceText + ((playerUpgradeLevel + 1) * upgrade.cost);
    }
}
