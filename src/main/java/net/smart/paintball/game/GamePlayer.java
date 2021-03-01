package net.smart.paintball.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.smart.paintball.game.team.GameTeam;
import org.bukkit.entity.Player;

@AllArgsConstructor @Data
public class GamePlayer {

    private Player player;
    private GameTeam team;
}
