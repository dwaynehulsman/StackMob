package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by nathat on 17/10/16.
 */
public class EntityTargetPlayerEvent implements Listener {

    private StackMob sm;
    public EntityTargetPlayerEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent ea){
        if(ea.getTarget() instanceof Player){
            if(sm.amountMap.containsKey(ea.getEntity().getUniqueId())){
                ea.setCancelled(true);
            }
        }
    }
}
