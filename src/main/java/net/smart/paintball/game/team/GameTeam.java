package net.smart.paintball.game.team;

import lombok.Data;
import net.smart.paintball.game.translate.ColorTranslate;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;

@Data
public abstract class GameTeam {

    private final String name;
    private int points;
    private Location location;
    private final ChatColor chatColor;

    public GameTeam(String name, ChatColor chatColor) {
        this.name = name;
        this.chatColor = chatColor;
        this.points = 0;
        this.location = null;
    }

    public final Color getColor() {
        return ColorTranslate.translateToColor(chatColor);
    }

    public final String getNameColor() {
        return getChatColor() + name;
    }
}
