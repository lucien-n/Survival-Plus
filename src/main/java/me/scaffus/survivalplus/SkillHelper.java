package me.scaffus.survivalplus;

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
    private final List<Integer> pointsForLevels;

    public SkillHelper(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.skillsConfig = plugin.skillsConfig;

        pointsForLevels = (List<Integer>) skillsConfig.get().get("points_for_level");
        skillsGainedXpMessage = plugin.getConfig().getString("skills.gained");
        skillsPassedLevelMessage = plugin.getConfig().getString("skills.passed_level");
    }

    public void handlePlayerSkillLevel(Player p, String skill, Integer skillLevel, Double skillPoints, Integer pointsForNextLevel) {
        UUID uuid = p.getUniqueId();
        if (skillLevel >= pointsForLevels.size()) return;
        if (skillPoints >= pointsForNextLevel) {
            survivalData.incrementPlayerSkillLevel(uuid, skill, 1);
            survivalData.incrementPlayerTokens(uuid, 1);
            p.sendMessage(skillsPassedLevelMessage.replace("%level%", String.valueOf(skillLevel + 1)).replace("%skill%", skill));
        }
    }

    public void handleSkillGain(Player p, Double pointsGained, String skill) {
        UUID uuid = p.getUniqueId();
        Integer playerSkillLevel = survivalData.getPlayerSkillLevel(uuid, skill);
        Double playerSkillPoints = survivalData.getPlayerSkillPoints(uuid, skill);
        Integer pointsForNextLevel = pointsForLevels.get(playerSkillLevel);
        Double totalPlayerSkillPoints = playerSkillPoints + pointsGained;

        survivalData.incrementPlayerSkillPoints(uuid, skill, pointsGained);
        handlePlayerSkillLevel(p, skill, playerSkillLevel, totalPlayerSkillPoints, pointsForNextLevel);

        // Running skill spams the bar
        if (skill.equalsIgnoreCase("running")) return;

        // Display skill xp bar
        BossBar playerSkillBar = survivalData.getPlayerSkillBar(uuid);
        playerSkillBar.setTitle(plugin.getConfig().getString("skills.gained")
                .replace("%skill%", survivalData.getSkill(skill).displayName)
                .replace("%amount%", String.valueOf(totalPlayerSkillPoints))
                .replace("%amount_for_level%", String.valueOf(pointsForNextLevel)));

        int factor = pointsForLevels.get(playerSkillLevel - (playerSkillLevel == 0 ? 0 : 1));
        double progress = (totalPlayerSkillPoints - (playerSkillLevel == 0 ? 0 : factor)) / (pointsForNextLevel - (playerSkillLevel == 0 ? 0 : factor));

        playerSkillBar.setProgress(totalPlayerSkillPoints + pointsGained >= pointsForNextLevel ? 1.0 : progress);
        playerSkillBar.addPlayer(p);
        playerSkillBar.setVisible(true);

//        HidePlayerSkillBarTask hide = new HidePlayerSkillBarTask(survivalData.getPlayerSkillBar(uuid));
//        hide.runTaskLater(plugin, 140L);
    }
}
