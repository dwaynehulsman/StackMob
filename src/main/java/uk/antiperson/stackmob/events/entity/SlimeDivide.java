package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;
import uk.antiperson.stackmob.utils.EntityUtils;
import uk.antiperson.stackmob.StackMob;

import java.util.SplittableRandom;

/**
 * Created by nathat on 26/11/16.
 */
public class SlimeDivide implements Listener {

    private StackMob sm;
    public SlimeDivide(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onSlimeDivide(SlimeSplitEvent e){
        if(sm.amountMap.containsKey(e.getEntity().getUniqueId()) && sm.amountMap.get(e.getEntity().getUniqueId()) > 1){
            SplittableRandom rand = new SplittableRandom();
            int slimeCount = (int) Math.round((2 + rand.nextInt(1) + rand.nextDouble() + rand.nextDouble()) * sm.amountMap.get(e.getEntity().getUniqueId())) - e.getCount();
            EntityUtils eu = new EntityUtils(sm);
            Slime s = (Slime) eu.createEntity(e.getEntity(), false, true);
            s.setSize(Math.round(e.getEntity().getSize() / 2));
            sm.noStack.add(s.getUniqueId());
            sm.amountMap.put(s.getUniqueId(), slimeCount);
        }
    }
}
