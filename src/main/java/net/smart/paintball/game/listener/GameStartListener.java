package net.smart.paintball.game.listener;

import net.smart.paintball.game.Game;
import net.smart.paintball.game.GamePlayer;
import net.smart.paintball.game.event.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStartListener implements Listener {

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Game game = event.getGame();

        for (int i = 0; i < game.getPlayers().size(); i++) {
            GamePlayer gamePlayer = game.getPlayers().get(i);
            gamePlayer.setTeam(game.getPlayers().size() % 2 == 0 ?
                    ((i + 1) <= (game.getPlayers().size() / 2) ? game.getBlueTeam() : game.getRedTeam()) :
                    game.getBlueTeam()
            );

            game.respawn(gamePlayer.getPlayer().getName());
            gamePlayer.getPlayer().sendMessage("§6Você é do time " + gamePlayer.getTeam().getNameColor() + "§6.");
        }

        game.getPlayers().forEach(gamePlayer -> {
            game.setItems(gamePlayer.getPlayer().getName());
            game.respawn(gamePlayer.getPlayer().getName());
        });

        game.broadcast("§aA partida iniciou!");
    }
}
