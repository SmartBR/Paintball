package net.smart.paintball.game.listener;

import net.smart.paintball.game.Game;
import net.smart.paintball.game.event.GameWinEvent;
import net.smart.paintball.game.team.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameWinListener implements Listener {

    @EventHandler
    public void onGameWin(GameWinEvent event) {
        Game game = event.getGame();
        GameTeam win = event.getWin();

        game.getPlayers().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setArmorContents(null);
            player.teleport(player.getWorld().getSpawnLocation());

            boolean isWinner = gamePlayer.getTeam().equals(win);
            String title = isWinner ? "§a§lVOCÊ GANHOU!" : "§c§lVOCÊ PERDEU!";
            String subtitle = isWinner ? "§fParabéns, seu time venceu!" : "§cSeu time perdeu =/";
            player.sendTitle(title, subtitle);

            game.broadcast("");
            game.broadcast("§e§lGANHADOR: " + win.getChatColor() + "Time " + win.getNameColor());
            game.broadcast("");
        });

        game.getPlayers().clear();
        game.getRedTeam().setPoints(0);
        game.getBlueTeam().setPoints(0);
    }
}
