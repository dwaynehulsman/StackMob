package uk.antiperson.stackmob.events.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.StackMob;

import java.util.Random;

/**
 * Created by nathat on 28/01/17.
 */
public class EntityDamage implements Listener {

    private StackMob sm;
    private Configuration config;
    private Random r = new Random();
    public EntityDamage(StackMob sm){
        this.sm = sm;
        config = sm.config;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if(sm.amountMap.containsKey(e.getDamager().getUniqueId())){
            double max = config.getFilecon().getDouble("creature.all-damage.max");
            double min = config.getFilecon().getDouble("creature.all-damage.min");
            double newDamage = r.nextInt(sm.amountMap.get(e.getDamager().getUniqueId())) * (e.getDamage() * myRandom(min, max));
            e.setDamage(e.getDamage() + newDamage);
        }
    }

    private double myRandom(double min, double max) {
        return (r.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
    }
}
