package net.smart.paintball.game.listener;

import lombok.AllArgsConstructor;
import net.smart.paintball.game.GameManager;
import net.smart.paintball.game.event.GamePlayerRespawnEvent;
import net.smart.paintball.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@AllArgsConstructor
public class GameSnowballListener implements Listener {

    private final GameManager gameManager;

    @EventHandler(ignoreCancelled = true)
    public void onSnowball(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Snowball) ||
                !event.getEntityType().equals(EntityType.PLAYER)) return;

        Snowball snowball = (Snowball) event.getDamager();
        Player shooter = (Player) snowball.getShooter();
        Player victim = (Player) event.getEntity();

        gameManager.getPlayerGame(shooter.getName()).ifPresent(game -> {
            if (game.getState().equals(GameState.INGAME) && !game.equalsTeam(shooter.getName(), victim.getName())) {
                Bukkit.getPluginManager().callEvent(
                        new GamePlayerRespawnEvent(game, shooter, victim)
                );
            }
        });
    }
}
