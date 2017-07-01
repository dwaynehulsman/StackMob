package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.utils.EntityUtils;

/**
 * Created by nathat on 10/10/16.
 */
public class DyeEvent implements Listener {

    private StackMob sm;
    public DyeEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onSheepDye(SheepDyeWoolEvent e){
        if(sm.amountMap.containsKey(e.getEntity().getUniqueId())){
            if(sm.amountMap.get(e.getEntity().getUniqueId()) > 1){
                EntityUtils eu = new EntityUtils(sm);
                Entity ne = eu.createEntity(e.getEntity(), true, true);
                sm.amountMap.put(e.getEntity().getUniqueId(), 1);
            }
        }
    }
}
