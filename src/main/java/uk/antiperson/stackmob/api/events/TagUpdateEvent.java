package uk.antiperson.stackmob.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import uk.antiperson.stackmob.api.StackedEntity;

/**
 * Created by nathat on 08/11/16.
 */
public class TagUpdateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private StackedEntity entity;
    private String updatedTag;
    private String unParsedName;
    private boolean visible;

    public TagUpdateEvent(StackedEntity e, String newName, String fromConfig, boolean visibleOnHover) {
        entity = e;
        updatedTag = newName;
        unParsedName = fromConfig;
        visible = visibleOnHover;
    }

    public StackedEntity getStackedEntity(){
        return entity;
    }

    public String getUpdatedTag(){
        return updatedTag;
    }

    public void setUpdatedTag(String newTag){
        updatedTag = newTag;
    }

    public boolean isVisibleOnHover(){
        return visible;
    }

    public void setVisibleOnHover(boolean value){
        visible = value;
    }

    public String getUnParsedName(){
        return unParsedName;
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
