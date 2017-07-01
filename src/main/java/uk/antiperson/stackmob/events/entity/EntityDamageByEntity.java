package uk.antiperson.stackmob.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.StackMob;

import java.util.SplittableRandom;

/**
 * Created by nathat on 28/01/17.
 */
public class EntityDamageByEntity implements Listener {

    private StackMob sm;
    private Configuration config;
    private SplittableRandom r = new SplittableRandom();
    public EntityDamageByEntity(StackMob sm){
        this.sm = sm;
        config = sm.config;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if(sm.amountMap.containsKey(e.getDamager().getUniqueId())){
            if(config.getFilecon().getBoolean("creature.all-damage.enabled")) {
                double max = config.getFilecon().getDouble("creature.all-damage.max");
                double min = config.getFilecon().getDouble("creature.all-damage.min");
                double newDamage = r.nextInt(sm.amountMap.get(e.getDamager().getUniqueId())) * (e.getDamage() * myRandom(min, max));
                e.setDamage(e.getDamage() + newDamage);
            }else if(config.getFilecon().getBoolean("creature.no-targeting")){
                e.setCancelled(true);
            }
        }
    }

    private double myRandom(double min, double max) {
        return (r.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
    }
}
