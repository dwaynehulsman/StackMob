package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.utils.EntityUtils;
import uk.antiperson.stackmob.StackMob;

import java.util.Random;

/**
 * Created by nathat on 17/10/16.
 */
public class ShearEvent implements Listener {

    private Configuration config;
    private StackMob sm;
    public ShearEvent(StackMob sm){
        config = sm.config;
        this.sm = sm;
    }


    @EventHandler
    public void onShear(PlayerShearEntityEvent e){
        if(sm.amountMap.containsKey(e.getEntity().getUniqueId())) {
            if (sm.amountMap.get(e.getEntity().getUniqueId()) > 1) {
                LivingEntity ea = (LivingEntity) e.getEntity();
                if(ea instanceof Sheep && config.getFilecon().getBoolean("creature.sheep.shearall")){
                    Wool dye = new Wool();
                    dye.setColor(((Sheep) ea).getColor());
                    ItemStack is = dye.toItemStack((int) generateRandom(sm.amountMap.get(ea.getUniqueId())));
                    ea.getWorld().dropItemNaturally(ea.getLocation(), is);
                }else if (ea instanceof Sheep && config.getFilecon().getBoolean("creature.sheep.divideonshear")) {
                    LivingEntity le = (LivingEntity) ea.getWorld().spawnEntity(ea.getLocation(), ea.getType());
                    ((Sheep) le).setColor(((Sheep) ea).getColor());
                    sm.amountMap.put(le.getUniqueId(), (sm.amountMap.get(ea.getUniqueId()) - 1));
                    sm.amountMap.put(ea.getUniqueId(), 1);
                } else if (ea instanceof MushroomCow){
                    EntityUtils eu = new EntityUtils(sm);
                    LivingEntity le = (LivingEntity) eu.createEntity(ea, false, true);
                    sm.amountMap.put(le.getUniqueId(), (sm.amountMap.get(ea.getUniqueId()) - 1));
                }
            }
        }
    }

    public float generateRandom(int amount){
        Random rand = new Random();
        double a = rand.nextInt(3) + rand.nextDouble();
        double b = a * amount;
        return Math.round(b);
    }
}
