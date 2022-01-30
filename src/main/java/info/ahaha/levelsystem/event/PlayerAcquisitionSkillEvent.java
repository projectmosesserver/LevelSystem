package info.ahaha.levelsystem.event;

import info.ahaha.levelsystem.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerAcquisitionSkillEvent extends PlayerEvent implements Cancellable {
    private boolean cancel = false;
    private static HandlerList Handlers = new HandlerList();
    private Skill skill;
    public PlayerAcquisitionSkillEvent(Player who , Skill skill) {
        super(who);
        this.skill = skill;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
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
