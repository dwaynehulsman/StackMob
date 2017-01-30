package uk.antiperson.stackmob.events.entity;

import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import uk.antiperson.stackmob.StackMob;


/**
 * Created by nathat on 29/10/16.
 */
public class ChickenLayEggEvent implements Listener {

    private StackMob sm;
    public ChickenLayEggEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void chickenLayEgg(ItemSpawnEvent e){
        if(e.getEntity().getItemStack().getType() == Material.EGG){
            for(Entity en : e.getEntity().getNearbyEntities(0, 0.25, 0)){
                if(en instanceof Chicken){
                    if(sm.amountMap.containsKey(en.getUniqueId())){
                        e.getEntity().getItemStack().setAmount(sm.amountMap.get(en.getUniqueId()));
                    }
                }
            }
        }
    }

}
