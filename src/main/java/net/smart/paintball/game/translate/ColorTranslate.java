package net.smart.paintball.game.translate;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class ColorTranslate {

    public static Color translateToColor(ChatColor color) {
        switch (color) {
            case BLUE:
                return Color.BLUE;
            case RED:
                return Color.RED;
            default:
                return Color.GRAY;
        }
    }
}
