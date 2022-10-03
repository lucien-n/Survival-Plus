package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Vector;

public class PlayerInteractListener implements Listener {
    private SurvivalPlus plugin;
    private SurvivalData survivalData;
    private SkillsConfig skillsConfig;
    private List<String> hoes;
    private List<String> tillables;
    private List<Object> toTillVectors;

    public PlayerInteractListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        hoes = (List<String>) skillsConfig.get().get("farming.hoes");
        tillables = (List<String>) skillsConfig.get().get("farming.tillables");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (survivalData.playerHasUpgradeWideTill.get(p.getUniqueId()) > 0) {
            if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
            if (!(tillables.contains(event.getClickedBlock().getType().toString()))) return;
            Location blockLoc = event.getClickedBlock().getLocation();

            blockLoc.add(0, 0, -1).getBlock().setType(Material.FARMLAND);
            blockLoc.add(1, 0, -1).getBlock().setType(Material.FARMLAND);
            blockLoc.add(1, 0, 0).getBlock().setType(Material.FARMLAND);

            blockLoc.add(1, 0, 1).getBlock().setType(Material.FARMLAND);
            blockLoc.add(0, 0, -1).getBlock().setType(Material.FARMLAND);
            blockLoc.add(-1, 0, 1).getBlock().setType(Material.FARMLAND);

            blockLoc.add(-1, 0, 0).getBlock().setType(Material.FARMLAND);
            blockLoc.add(-1, 0, -1).getBlock().setType(Material.FARMLAND);
        }
    }
}
