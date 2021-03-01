package net.smart.paintball.game.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.smart.paintball.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @Data
public class GameStartEvent extends Event {

    @Getter private static final HandlerList handlerList = new HandlerList();

    private final Game game;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
