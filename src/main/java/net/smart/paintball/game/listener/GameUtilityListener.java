package net.smart.paintball.game.listener;

import lombok.AllArgsConstructor;
import net.smart.paintball.Paintball;
import net.smart.paintball.game.GameManager;
import net.smart.paintball.game.GamePlayer;
import net.smart.paintball.game.exception.PaintballException;
import net.smart.paintball.game.state.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

@AllArgsConstructor
public class GameUtilityListener implements Listener {

    private final GameManager gameManager;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER))
            return;

        Player victim = (Player) event.getEntity();
        gameManager.getPlayerGame(victim.getName()).ifPresent(game -> {
            if (game.getState() != GameState.INGAME) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.FALL ||
                    event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                    event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK);
        });
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER) || !event.getDamager().getType().equals(EntityType.PLAYER))
            return;

        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        gameManager.getPlayerGame(victim.getName()).ifPresent(game -> {
            if (game.getState() != GameState.INGAME) {
                event.setCancelled(true);
                return;
            }
            if (game.equalsTeam(victim.getName(), damager.getName())) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(event.toWeatherState());
    }
}
