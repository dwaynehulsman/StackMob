package uk.antiperson.stackmob.api.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.MergeType;
import uk.antiperson.stackmob.api.StackedEntity;

/**
 * Created by nathat on 06/11/16.
 */
public class EntityStackEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Entity le;
    private Entity la;
    private MergeType mt;
    private StackMob sm;
    public EntityStackEvent(Entity le, Entity la, StackMob sm, MergeType mt){
        this.le = le;
        this.la = la;
        this.sm = sm;
        this.mt = mt;
    }

    public StackedEntity getFirstEntity(){
        return sm.getAPI().getEntityManager().getStackedEntity(le);
    }

    public StackedEntity getOtherEntity(){
        return sm.getAPI().getEntityManager().getStackedEntity(la);
    }

    public int getCombinedAmount(){
        return getFirstEntity().getStackAmount() + getOtherEntity().getStackAmount();
    }

    public MergeType getMergeType(){
        return mt;
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
