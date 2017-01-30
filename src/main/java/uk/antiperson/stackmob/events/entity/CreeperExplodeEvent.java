package uk.antiperson.stackmob.events.entity;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.plugins.WorldGuard;

/**
 * Created by nathat on 19/11/16.
 */
public class CreeperExplodeEvent implements Listener {

    private StackMob sm;
    private WorldGuard wg;
    public CreeperExplodeEvent(StackMob sm){
        this.sm = sm;
        wg = new WorldGuard(sm);
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent es){
        if(es.getEntity() instanceof Creeper){
            if(sm.amountMap.containsKey(es.getEntity().getUniqueId())){
                es.setCancelled(true);
                int power = sm.amountMap.get(es.getEntity().getUniqueId()) * 3;
                es.setYield(power);
            }
        }
    }
}
