package uk.antiperson.stackmob.events.entity;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import uk.antiperson.stackmob.StackMob;

import java.util.SplittableRandom;

/**
 * Created by nathat on 11/03/17.
 */
public class EntityDamage implements Listener {

    private StackMob sm;
    private SplittableRandom rand = new SplittableRandom();
    public EntityDamage(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(sm.config.getFilecon().getStringList("creature.damage-multiply.reasons").contains(e.getCause().toString())){
            if(sm.amountMap.containsKey(e.getEntity().getUniqueId())){
                if(sm.amountMap.get(e.getEntity().getUniqueId()) > 1){
                    double newDamage = (e.getDamage() * sm.amountMap.get(e.getEntity().getUniqueId())) * (0.8 + rand.nextDouble(0.2));
                    e.getEntity().setMetadata("stackmob-damage", new FixedMetadataValue(sm, newDamage));
                    e.setDamage(newDamage);
                }
            }
        }
    }
}
