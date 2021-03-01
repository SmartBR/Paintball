package net.smart.paintball.game.listener;

import net.smart.paintball.game.Game;
import net.smart.paintball.game.GamePlayer;
import net.smart.paintball.game.event.GamePlayerRespawnEvent;
import net.smart.paintball.game.exception.PaintballException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GamePlayerRespawnListener implements Listener {

    @EventHandler
    public void onGamePlayerRespawn(GamePlayerRespawnEvent event) {
        Game game = event.getGame();
        Player killer = event.getKiller();
        Player victim = event.getVictim();

        GamePlayer killerGamePlayer = game.getPlayer(killer.getName())
                .orElseThrow(() -> new PaintballException("killer game player is null"));

        GamePlayer victimGamePlayer = game.getPlayer(killer.getName())
                .orElseThrow(() -> new PaintballException("victim game player is null"));

        victim.getInventory().clear();
        victim.getInventory().setArmorContents(null);
        game.respawn(victim.getName());
        game.setItems(victim.getName());

        victim.setNoDamageTicks(3 * 20);
        victim.sendMessage("§eVocê morreu para o jogador " + killerGamePlayer.getTeam().getChatColor() + killer.getName() + "§e.");

        killerGamePlayer.getTeam().setPoints(killerGamePlayer.getTeam().getPoints() + 1);
        killer.sendMessage("§aVocê matou o jogador " + victimGamePlayer.getTeam().getChatColor() + victim.getName() + "§a. §3(" + killerGamePlayer.getTeam().getPoints() + " pontos)");
    }
}
