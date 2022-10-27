package me.scaffus.survivalplus.listeners;

import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomMobsListener implements Listener {
    private final SurvivalPlus plugin;

    private final List<String> mobs = new ArrayList<>();
    private final Random random;
    private final ItemStack ironGolemSpawnEgg = new ItemStack(Material.POLAR_BEAR_SPAWN_EGG);

    public CustomMobsListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);

        mobs.add("Creeper");
        mobs.add("Skeleton");
        mobs.add("Zombie");
        mobs.add("Spider");

        random = new Random();

        ItemMeta meta = ironGolemSpawnEgg.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("IRON_GOLEM");
        meta.setLore(lore);
        meta.setDisplayName("§fOeuf de Golem de Fer");
        ironGolemSpawnEgg.setItemMeta(meta);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();

        if (!mobs.contains(entity.getName())) return;

        long dayPassed = plugin.dayCountTask.dayPassed;

        // No custom mobs under 20days
        if (dayPassed < 20) return;

        modifyMob(entity);
    }

    private Integer r() {
        return random.nextInt(9);
    }

    private LivingEntity modifyMob(LivingEntity entity) {
        long dayPassed = plugin.dayCountTask.dayPassed;
        String name = entity.getName();
        EntityEquipment equipment = entity.getEquipment();
        boolean isGronk = false;

        /* Zombies bewteen day 20 & 50
         * 30% wooden_sword
         * 50% leather_boots
         * 20% leather_chestplate
         * */
        if (dayPassed > 20 && name.equalsIgnoreCase("Zombie")) {
            if (r() > 6) equipment.setItemInMainHand(new ItemStack(Material.WOODEN_SWORD));
            if (r() > 4) equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            if (r() < 1) equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
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
        if (dayPassed >= 50 && name.equalsIgnoreCase("Zombie") || name.equalsIgnoreCase("Skeleton")) {
            if (r() > 6) equipment.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            if (r() > 8) equipment.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
            if (r() < 1) equipment.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));

            if (name.equalsIgnoreCase("Zombie")) {
                if (r() > 7) equipment.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
                if (r() < 4) equipment.setItemInMainHand(new ItemStack(Material.WOODEN_SWORD));
            } else if (name.equalsIgnoreCase("Skeleton")) {
                if (r() > 7) equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            }
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.3);

            // ? Gronk
            if (random.nextInt(70) == 0 && name.equalsIgnoreCase("Zombie")) {
//            if (name.equalsIgnoreCase("Zombie")) {
                isGronk = true;
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 2.5);

                entity.setCustomName("§c§lGronk | §n" + entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + "§c❤");
                entity.setCustomNameVisible(true);

                equipment.setHelmet(new ItemStack(Material.IRON_HELMET));
                equipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                equipment.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                equipment.setBoots(new ItemStack(Material.GOLDEN_BOOTS));

                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                ItemMeta meta = sword.getItemMeta();
                meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
                if (r() > 7) meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
                sword.setItemMeta(meta);

                equipment.setItemInMainHand(sword);
                entity.setGlowing(true);
            }
        }

        if (dayPassed >= 150) {
            if (isGronk) {
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 2.5);
                entity.setCustomName("§c§lGronk | §n" + entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + "§c❤");
            }

            if (entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null)
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.2);
            if (entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() * 1.2);
            if (entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 1.2);
        }

        if (dayPassed >= 400) {
            if (isGronk) {
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 2.5);

                entity.setCustomName("§c§lGronk | §n" + entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + "§c❤");
            }

            if (entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null)
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.2);
            if (entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() * 1.2);
            if (entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 1.2);
        }

        if (dayPassed >= 750) {
            if (isGronk) {
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 2.5);

                entity.setCustomName("§c§lGronk | §n" + entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + "§c❤");
            }

            if (entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null)
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.2);
            if (entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() * 1.2);
            if (entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 1.2);
        }
        return entity;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
//         && event.getEntity().getLastDamageCause().getEntity() != null && event.getEntity().getLastDamageCause().getEntity() instanceof Player
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains("§c§lGronk")) {
            long dayPassed = plugin.dayCountTask.dayPassed;
            if (dayPassed >= 50) {
                event.getDrops().add(new ItemStack(Material.GOLD_BLOCK));
            }
            if (dayPassed >= 150) {
                event.getDrops().add(new ItemStack(Material.ANCIENT_DEBRIS));
                if (r() > 6) event.getDrops().add(new ItemStack(Material.ANCIENT_DEBRIS, 2));
                if (r() > 6) event.getDrops().add(new ItemStack(Material.TOTEM_OF_UNDYING));
                if (r() > 8) event.getDrops().add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
                if (r() > 8) event.getDrops().add(new ItemStack(Material.CREEPER_SPAWN_EGG));
            }
            if (dayPassed >= 400) {
                if (r() > 6) event.getDrops().add(new ItemStack(Material.REINFORCED_DEEPSLATE));
                if (r() > 8) event.getDrops().add(new ItemStack(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG, 2));
            }
            if (dayPassed >= 750) {
                if (r() > 8) event.getDrops().add(ironGolemSpawnEgg);
            }
        }
    }
}
