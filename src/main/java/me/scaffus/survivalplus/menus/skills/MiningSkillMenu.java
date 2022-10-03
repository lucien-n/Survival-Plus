package me.scaffus.survivalplus.menus.skills;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.PlayersData;
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

import java.util.UUID;

public class MiningSkillMenu implements Listener {
    private SurvivalPlus plugin;
    private PlayersData pData;
    private Helper helper;
    private SkillsMenu skillsMenu;
    private String inventoryName = "§6§lMinage";
    private Integer autoSmeltPrice = 2;
    private Integer veinMinePrice = 3;
    private Integer oreMagnetPrice = 2;

    public MiningSkillMenu(SurvivalPlus plugin, SkillsMenu skillsMenu) {
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

        Integer playerAutoSmeltLevel = pData.getPlayerUpgrade(uuid, "auto_smelt");
        Integer playerVeinMineLevel = pData.getPlayerUpgrade(uuid, "vein_mine");
        Integer playerOreMagnetLevel = pData.getPlayerUpgrade(uuid, "ore_magnet");

        if (slot == 11 && pData.getPlayerTokens(uuid) >= autoSmeltPrice && playerAutoSmeltLevel < 1) {
            pData.setPlayerUpgrade(uuid, "auto_smelt", playerAutoSmeltLevel + 1);
            pData.incrementPlayerTokens(uuid, -autoSmeltPrice);
            p.sendMessage(helper.upgradeBoughtMessage(pData.upgradeBought,
                    "cuisson des minerais niveau " + (playerAutoSmeltLevel + 1), autoSmeltPrice));
        } else if (slot == 15 && pData.getPlayerTokens(uuid) >= veinMinePrice && playerVeinMineLevel < 1) {
            pData.setPlayerUpgrade(uuid, "vein_mine", playerVeinMineLevel + 1);
            pData.incrementPlayerTokens(uuid, veinMinePrice);
            p.sendMessage(helper.upgradeBoughtMessage(pData.upgradeBought,
                    "mineur de veines niveau " + (playerVeinMineLevel + 1), veinMinePrice));
        } else if (slot == 31 && pData.getPlayerTokens(uuid) >= oreMagnetPrice && playerOreMagnetLevel < 1) {
            pData.setPlayerUpgrade(uuid, "ore_magnet", playerOreMagnetLevel + 1);
            pData.incrementPlayerTokens(uuid, oreMagnetPrice);
            p.sendMessage(helper.upgradeBoughtMessage(pData.upgradeBought,
                    "aimant à minerais niveau " + (playerOreMagnetLevel + 1), oreMagnetPrice));
        }

        if (slot == event.getInventory().getSize() - 9) p.openInventory(skillsMenu.createSkillMenu(p));
        if (slot == event.getInventory().getSize() - 1) p.closeInventory();
    }

    public Inventory createMenu(Player p) {
        UUID uuid = p.getUniqueId();
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, inventoryName, 54, backgroundItem, true);

        inventory.setItem(11, helper.getItem(new ItemStack(Material.FURNACE), "§6§lCuisson Auto", "§eCuit ton minerais quand tu le casses",
                pData.getPlayerUpgrade(uuid, "auto_smelt")
                        > 0 ? "§ePrix: §6Acquit" : "§ePrix: §6" + autoSmeltPrice));

        inventory.setItem(15, helper.getItem(new ItemStack(Material.TNT), "§6§lMineur De Veines", "§eCuit ton minerais quand tu le casses",
                pData.getPlayerUpgrade(uuid, "vein_mine")
                        > 0 ? "§ePrix: §6Acquit" : "§ePrix: §6" + veinMinePrice));

        inventory.setItem(31, helper.getItem(new ItemStack(Material.ENDER_EYE), "§6§lAimant à minerais", "§ele minerais arrives directement dans ton inventaire",
                pData.getPlayerUpgrade(uuid, "ore_magnet")
                        > 0 ? "§ePrix: §6Acquit" : "§ePrix: §6" + oreMagnetPrice,
                "§eRequiert: §6Cuissont Auto",
                "§cNe fonctionne qu'avec §6Cuissont Auto"));

        inventory.setItem(49, helper.getHead(p, "§eJetons: §6" + pData.getPlayerTokens(p.getUniqueId())));

        return inventory;
    }
}
