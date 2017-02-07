package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import uk.antiperson.stackmob.StackMob;

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
                Entity ea = e.getEntity();
                Entity en = ea.getWorld().spawnEntity(ea.getLocation(), ea.getType());
                Sheep sh = (Sheep) en;
                sh.setColor(sm.lastDyed.get(ea.getUniqueId()));
                if(((Ageable)ea).isAdult()){
                    ((Sheep) en).setAdult();
                }else{
                    sh.setBaby();
                }
                sm.lastDyed.remove(ea.getUniqueId());
                sm.amountMap.put(en.getUniqueId(), (sm.amountMap.get(ea.getUniqueId()) - 1));
                sm.amountMap.put(ea.getUniqueId(), 1);
            }
        }
    }
}
