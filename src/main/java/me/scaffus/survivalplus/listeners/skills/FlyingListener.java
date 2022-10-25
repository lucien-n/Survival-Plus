package me.scaffus.survivalplus.listeners.skills;

import me.scaffus.survivalplus.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlyingListener implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillHelper skillHelper;
    private final SkillsConfig skillsConfig;
    private final Helper helper;
    private List<UUID> jumpers = new ArrayList<>();

    public FlyingListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillHelper = plugin.skillHelper;
        this.skillsConfig = plugin.skillsConfig;
        this.helper = plugin.helper;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (p.isGliding()) skillHelper.handleSkillGain(p, 1.0, "flying");

        if (jumpers.contains(uuid)) {
            Location belowPlayer = p.getLocation().subtract(0, 0.1, 0);
            Block block = belowPlayer.getBlock();
            if (block.isEmpty() || block.isLiquid())
                return;
            if (!block.getType().isSolid())
                return;
            jumpers.remove(uuid);
            p.setAllowFlight(true);
        }

        // Stolen from https://stackoverflow.com/a/35402318/14611858
        // If a player is not on the ground and has fallen more than two blocks
        if (!p.isOnGround() && p.getFallDistance() > 2) {
            Location to = event.getTo().clone().subtract(0, 0.15, 0); // Get the location they will be at next tick
            if (to.getBlock().getType() != Material.AIR) { // If that block is not air
                p.setAllowFlight(false); // Cancel their ability to fly so that they take regular fall damage
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (survivalData.getPlayerUpgrade(event.getPlayer().getUniqueId(), "double_jump") > 0)
            event.getPlayer().setAllowFlight(true);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        if (survivalData.getPlayerUpgrade(uuid, "double_jump") < 1 || jumpers.contains(uuid) || (p.getGameMode() == GameMode.CREATIVE) || (p.getGameMode() == GameMode.SPECTATOR) || p.isSwimming() || p.isFlying() || p.isRiptiding() || p.isGliding())
            return;

        event.setCancelled(true);
        Vector direction = p.getEyeLocation().getDirection();
        if (direction.getY() <= 0)
            direction.setY(0.6);
        p.setAllowFlight(false);
        p.setVelocity(direction);
        jumpers.add(uuid);
    }

//    @EventHandler
//    public void onFallDamage(EntityDamageEvent event) {
//        if (!(event.getEntity() instanceof Player)) return;
//        Player p = (Player) event.getEntity();
//
//        if (jumpers.contains(p.getUniqueId()) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
//            event.setCancelled(true);
//        }
//    }

    @EventHandler
    public void switchGameMode(PlayerGameModeChangeEvent event) {
        Player p = event.getPlayer();
        if (survivalData.getPlayerUpgrade(p.getUniqueId(), "double_jump") > 0) {
            p.setAllowFlight(true);
        } else
            p.setAllowFlight(event.getNewGameMode() == GameMode.CREATIVE || event.getNewGameMode() == GameMode.SPECTATOR);

        p.setFlying(false);
    }

//
//    private boolean isNonGroundMaterial(Material type) {
//        return type == Material.LADDER ||
//                type == Material.VINE ||
//                type == Material.TALL_GRASS ||
//                type == Material.WEEPING_VINES_PLANT ||
//                type == Material.TWISTING_VINES_PLANT ||
//                type == Material.GLOW_LICHEN ||
//                type == Material.BROWN_MUSHROOM ||
//                type == Material.RED_MUSHROOM ||
//                type == Material.CRIMSON_FUNGUS ||
//                type == Material.WARPED_FUNGUS ||
//                type == Material.SUGAR_CANE ||
//                type == Material.NETHER_SPROUTS ||
//                type == Material.CRIMSON_ROOTS ||
//                type == Material.WARPED_ROOTS ||
//                type == Material.SCULK_VEIN ||
//                type == Material.LARGE_FERN ||
//
//                type == Material.CORNFLOWER ||
//                type == Material.POPPY ||
//                type == Material.LILY_OF_THE_VALLEY ||
//                type == Material.OXEYE_DAISY ||
//                type == Material.DANDELION ||
//                type == Material.AZURE_BLUET ||
//                type == Material.WITHER_ROSE ||
//                type == Material.BLUE_ORCHID ||
//
//                type == Material.PEONY ||
//                type == Material.LILAC ||
//                type == Material.ROSE_BUSH ||
//                type == Material.SUNFLOWER ||
//
//                type == Material.TORCH ||
//                type == Material.END_CRYSTAL ||
//                type.toString().contains("WALL") ||
//                type.toString().contains("TULIP") ||
//                (type.toString().contains("DEAD") && type.toString().contains("CORAL")) ||
//                type.toString().contains("DRIPLEAF") ||
//                type.toString().contains("BANNER") ||
//                type.toString().contains("FENCE") || // Filters out all fences and gates
//                type.toString().contains("DOOR"); // Filters out doors and trapdoors
//    }
}
