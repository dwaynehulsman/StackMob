package uk.antiperson.stackmob.api.events;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.api.StackedEntity;

import java.util.List;

/**
 * Created by natha on 07/02/2017.
 */
public class StackDeathEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private List<ItemStack> drops;
    private boolean killAll;
    private StackedEntity se;
    private boolean killStep;
    private int step;
    private HumanEntity he;
    private int xp;
    private StackedEntity newEntity;
    public StackDeathEvent(StackedEntity se, boolean killAll, boolean killStep, int step, HumanEntity killer, int xp, List<ItemStack> drops, StackedEntity newEntity){
        this.se = se;
        this.killAll = killAll;
        this.killStep = killStep;
        this.step = step;
        this.he = killer;
        this.xp = xp;
        this.drops = drops;
        this.newEntity = newEntity;
    }

    public StackedEntity getStackedEntity(){
        return se;
    }

    public StackedEntity getNewEntity(){
        return newEntity;
    }

    public boolean isKillAll(){
        return killAll;
    }

    public boolean isKillStep(){
        return killStep;
    }

    public int getKillStep(){
        return step;
    }

    public HumanEntity getKiller(){
        return he;
    }

    public int getXp(){
        return xp;
    }

    public List<ItemStack> getDrops(){
        return drops;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
