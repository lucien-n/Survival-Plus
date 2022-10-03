package me.scaffus.survivalplus.EventListeners;

import me.scaffus.survivalplus.SkillsConfig;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerItemDamageListener implements Listener {
    private SurvivalPlus plugin;
    private SurvivalData survivalData;
    private SkillsConfig skillsConfig;
    private List<String> hoes;

    public PlayerItemDamageListener(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;
        hoes = (List<String>) skillsConfig.get().getConfigurationSection("farming.hoes");
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (survivalData.playerHasUpgradeWideTill.get(event.getPlayer().getUniqueId()) > 0) {
            Player p = event.getPlayer();
            ItemStack tool = event.getItem();
            if (!hoes.contains(tool.getType().toString())) return;
            Block block = p.getTargetBlock(null, 3);
            if (!(block.getType() == Material.FARMLAND)) return;
            block.getLocation().add(0, 1, 0).getBlock().setType(Material.GOLD_BLOCK);
        }
    }
}
