package info.ahaha.levelsystem.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerGainEXPEvent extends PlayerEvent implements Cancellable {

    private int exp;
    private boolean cancel = false;
    private static HandlerList Handlers = new HandlerList();
    public PlayerGainEXPEvent(Player who,int exp) {
        super(who);
        this.exp = exp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
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
