package info.ahaha.levelsystem.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLevelUpEvent extends PlayerEvent implements Cancellable {

    private int defaultLevel;
    private int afterLevel;
    private boolean cancel = false;
    private static HandlerList Handlers = new HandlerList();

    public PlayerLevelUpEvent(Player who, int defaultLevel, int afterLevel) {
        super(who);
        this.defaultLevel = defaultLevel;
        this.afterLevel = afterLevel;
    }

    public int getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(int defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public int getAfterLevel() {
        return afterLevel;
    }

    public void setAfterLevel(int afterLevel) {
        this.afterLevel = afterLevel;
    }

    public HandlerList getHandlers() {
        return Handlers;
    }

    public static HandlerList getHandlerList() {
        return Handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}