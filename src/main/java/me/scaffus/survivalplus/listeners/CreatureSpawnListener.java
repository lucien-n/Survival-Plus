package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatureSpawnListener implements Listener {
    private final SurvivalPlus plugin;

    private final List<String> mobs = new ArrayList<>();
    private final Random random;

    public CreatureSpawnListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);

        mobs.add("Creeper");
        mobs.add("Skeleton");
        mobs.add("Zombie");
        mobs.add("Spider");

        random = new Random();
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();

        if (!mobs.contains(entity.getName())) return;

        long dayPassed = plugin.dayCountTask.dayPassed;

        // No custom mobs under 20days
        if (dayPassed > 20) {
            String name = entity.getName();
            EntityEquipment equipment = entity.getEquipment();
            /* Zombies bewteen day 20 & 50
             * 30% wooden_sword
             * 50% leather_boots
             * 20% leather_chestplate
             * */
            if (dayPassed < 50 && name.equalsIgnoreCase("Zombie")) {
                if (random.nextInt(10) > 6) {
                    equipment.setItemInMainHand(new ItemStack(Material.WOODEN_SWORD));
                }
                if (random.nextInt(10) > 4) {
                    equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                }
                if (random.nextInt(10) < 1) {
                    equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                }
            }

            /* Zombies & Skeletons between day 50 & 150
             * 30% chainmail_leggings
             * 90% leather boots
             * 10% chainmail_chestplate
             * 10% golden_helmet
             * zombie: 20% iron_sword
             * zombie: 40% enchanted wooden_sword
             * skeleton: 20% iron_chestplate
             * */
            if (dayPassed > 50 && dayPassed < 150 && name.equalsIgnoreCase("Zombie") || name.equalsIgnoreCase("Skeleton")) {
                int r = random.nextInt(10);
                if (r > 6) equipment.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                if (r > 8) equipment.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
                if (r < 1) equipment.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));

                if (name.equalsIgnoreCase("Zombie")) {
                    if (r > 7) equipment.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
                    if (r < 4) equipment.setItemInMainHand(new ItemStack(Material.WOODEN_SWORD));
                } else if (name.equalsIgnoreCase("Skeleton")) {
                    if (r > 7) equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                }
            }
        }
    }
}
