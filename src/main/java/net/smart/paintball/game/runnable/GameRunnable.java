package net.smart.paintball.game.runnable;

import lombok.AllArgsConstructor;
import net.smart.paintball.game.Game;
import net.smart.paintball.game.event.GameStartEvent;
import net.smart.paintball.game.event.GameWinEvent;
import net.smart.paintball.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class GameRunnable extends BukkitRunnable {

    private final Game game;

    @Override
    public void run() {
        switch (game.getState()) {
            case WAITING:
                if (game.getPlayers().size() < 2) {
                    if (game.getTime() < 15) {
                        game.setTime(15);
                        game.playSound(Sound.BLOCK_STONE_BUTTON_CLICK_ON);
                        game.broadcast("§cO temporizador foi resetado por falta de jogadores.");
                    }
                    return;
                }

                if (game.getTime() > 10 && (game.getPlayers().size() == game.getMaxPlayers() || game.getPlayers().size() == (game.getPlayers().size() / 2))) {
                    game.setTime(10);
                }

                if (game.getTime() == 0) {
                    game.setState(GameState.INGAME);
                    Bukkit.getPluginManager().callEvent(new GameStartEvent(game));
                    return;
                }

                if (game.getTime() == 30 || game.getTime() == 50 || game.getTime() == 15 || game.getTime() == 10 || game.getTime() == 5 || (game.getTime() <= 3 && game.getTime() != 0)) {
                    game.playSound(Sound.BLOCK_STONE_BUTTON_CLICK_ON);
                    game.broadcast("§eIniciando partida em §f" + game.getTime() + " " + (game.getTime() > 1 ? "segundos" : "segundo") + ".");
                }
                game.setTime(game.getTime() - 1);
                break;
            case INGAME:
                int pointsToWin = 3;
                if (game.getBlueTeam().getPoints() >= pointsToWin || game.getRedTeam().getPoints() >= pointsToWin
                        || game.getTime() >= (TimeUnit.MINUTES.toSeconds(15))
                        || game.getPlayers(game.getBlueTeam()).size() == 0
                        || game.getPlayers(game.getRedTeam()).size() == 0) {
                    Bukkit.getPluginManager().callEvent(
                            new GameWinEvent(game, game.getBlueTeam().getPoints() >= pointsToWin ? game.getBlueTeam() : game.getRedTeam())
                    );
                    return;
                }
                game.setTime(game.getTime() + 1);
                break;
        }
    }
}
