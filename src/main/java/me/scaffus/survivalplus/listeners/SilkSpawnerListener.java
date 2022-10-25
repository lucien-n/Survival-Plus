package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SilkSpawnerListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final Integer levelRequired;
    private final Random random;

    public SilkSpawnerListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        levelRequired = skillsConfig.get().getInt("misc.silk_spawner.level_required");
        random = new Random();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        Block block = event.getBlock();
        Integer playerMiningLevel = survivalData.getPlayerSkillLevel(uuid, "mining");
        boolean playerHasRequiredLevel = playerMiningLevel > levelRequired;

        if (block.getType() != Material.SPAWNER) return;
        event.setCancelled(true);

        if (p.isSneaking() && !playerHasRequiredLevel) {
            block.setType(Material.AIR);
            return;
        }

        if (!playerHasRequiredLevel) {
            p.sendMessage("§eIl faut que tu sois §6niveau §n" + levelRequired + "§e pour récupérer le spawner.");
            p.sendMessage("§ePour §6supprimer§e le spawner, accroupi toi.");
            return;
        }

        if (!p.isSneaking() && playerHasRequiredLevel) {
            if (random.nextBoolean()) {
                CreatureSpawner cs = (CreatureSpawner) block.getState();
                ItemStack spawner = new ItemStack(block.getType());
                ItemMeta meta = spawner.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add("§e§o" + cs.getSpawnedType());
                meta.setLore(lore);
                meta.setDisplayName("§6§l§oSpawner");
                spawner.setItemMeta(meta);

                block.getWorld().dropItem(block.getLocation(), spawner);
                block.setType(Material.AIR);
                p.playSound(p.getLocation(), "entity.wandering_trader.yes", 1, 1);
            } else {
                block.setType(Material.REINFORCED_DEEPSLATE);
                p.playSound(p.getLocation(), "entity.wandering_trader.no", 1, 1);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Block blockPlaced = event.getBlockPlaced();
        ItemStack itemInHand = event.getItemInHand();

        if (blockPlaced.getType().equals(Material.SPAWNER)) {
            CreatureSpawner creatureSpawner = (CreatureSpawner) blockPlaced.getState();
            try {
                creatureSpawner.setSpawnedType(EntityType.valueOf(itemInHand.getItemMeta().getLore().get(0).replace("§e§o", "")));
            } catch (Exception e) {
                creatureSpawner.update();
            }
        }
    }
}
