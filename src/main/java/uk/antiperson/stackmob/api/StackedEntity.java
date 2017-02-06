package uk.antiperson.stackmob.api;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by nathat on 06/11/16.
 */
public class StackedEntity {

    private Entity e;
    private StackMob sm;
    public StackedEntity(StackMob sm, Entity e){
        this.e = e;
        this.sm = sm;
    }

    public int getStackAmount(){
        return sm.amountMap.get(e.getUniqueId());
    }

    public void setStackAmount(int stackAmount){
        sm.amountMap.put(e.getUniqueId(), stackAmount);
    }

    public void remove(){
        sm.amountMap.remove(e.getUniqueId());
    }

    public void kill(){
        e.remove();
        sm.amountMap.remove(e.getUniqueId());
    }

    public Entity getEntity(){
        return e;
    }

    public boolean isFertile(){
        return sm.fertile.contains(e.getUniqueId());
    }
}
