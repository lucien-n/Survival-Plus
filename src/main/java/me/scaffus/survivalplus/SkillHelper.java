package me.scaffus.survivalplus;

import me.scaffus.survivalplus.tasks.HidePlayerSkillBarTask;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class SkillHelper {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final SkillsConfig skillsConfig;
    private final String skillsGainedXpMessage;
    private final String skillsPassedLevelMessage;
    private final List pointsForLevels;

    public SkillHelper(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;

        pointsForLevels = (List) skillsConfig.get().get("points_for_level");
        skillsGainedXpMessage = plugin.getConfig().getString("skills.gained");
        skillsPassedLevelMessage = plugin.getConfig().getString("skills.passed_level");
    }

    public void handlePlayerSkillLevel(Player p, String skill) {
        int playerSkillLevel = survivalData.getPlayerSkillLevel(p.getUniqueId(), skill);
        Double playerSkillPoints = survivalData.getPlayerSkillPoints(p.getUniqueId(), skill);
        for (int i = 0; i <= pointsForLevels.size(); i++) {
            if (playerSkillLevel == pointsForLevels.size()) return;
            if (playerSkillLevel == i && playerSkillPoints >= (int) pointsForLevels.get(i)) {
                survivalData.incrementPlayerSkillLevel(p.getUniqueId(), skill, 1);
                survivalData.incrementPlayerTokens(p.getUniqueId(), 1);
                p.sendMessage(skillsPassedLevelMessage.replace("%level%", String.valueOf(i + 1)));
            }
        }
    }

    public void handleSkillGain(Player p, Double pointsGained, String skill) {
        UUID uuid = p.getUniqueId();
        Integer playerSkillLevel = survivalData.getPlayerSkillLevel(uuid, skill);
        Double playerSkillPoints = survivalData.getPlayerSkillPoints(uuid, skill);
        Double totalPlayerSkillPoints = playerSkillPoints + pointsGained;

        Integer pointsForNextLevel = (Integer) pointsForLevels.get(playerSkillLevel + 1);
        BossBar bar = Bukkit.createBossBar(
                plugin.getConfig().getString("skills.gained")
                        .replace("%skill%", skill)
                        .replace("%amount%", String.valueOf(totalPlayerSkillPoints))
                        .replace("%amount_for_level%", String.valueOf(pointsForNextLevel)),
                BarColor.GREEN, BarStyle.SOLID);
        bar.setProgress(totalPlayerSkillPoints / pointsForNextLevel);
        bar.addPlayer(p);
        bar.setVisible(true);
        survivalData.setPlayerSkillBar(uuid, bar);

        HidePlayerSkillBarTask hide = new HidePlayerSkillBarTask(survivalData.getPlayerSkillBar(uuid));
        hide.runTaskLater(plugin, 60L);

        survivalData.incrementPlayerSkillPoints(p.getUniqueId(), "mining", pointsGained);
        handlePlayerSkillLevel(p, skill);
    }
}
