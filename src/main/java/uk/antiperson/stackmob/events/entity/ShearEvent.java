package uk.antiperson.stackmob.events.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.utils.EntityUtils;
import uk.antiperson.stackmob.StackMob;

import java.util.SplittableRandom;

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
                EntityUtils eu = new EntityUtils(sm);
                LivingEntity ea = (LivingEntity) e.getEntity();
                if(ea instanceof Sheep && config.getFilecon().getBoolean("creature.sheep.shearall")){
                    Wool dye = new Wool();
                    dye.setColor(((Sheep) ea).getColor());
                    ItemStack is = dye.toItemStack();
                    multiplyDrops(is, (int) generateRandom(sm.amountMap.get(ea.getUniqueId())), ea.getLocation());
                    ItemStack isa = e.getPlayer().getItemInHand();
                    isa.setDurability((short)(isa.getDurability() + sm.amountMap.get(ea.getUniqueId())));
                    e.getPlayer().setItemInHand(isa);
                }else if (ea instanceof Sheep && config.getFilecon().getBoolean("creature.sheep.divideonshear")) {
                    LivingEntity le = (LivingEntity) eu.createEntity(ea,false,true);
                    ((Sheep)le).setSheared(false);
                    sm.amountMap.put(le.getUniqueId(), sm.amountMap.get(ea.getUniqueId()) - 1);
                    sm.amountMap.put(ea.getUniqueId(), 1);
                    sm.noStack.add(le.getUniqueId());
                } else if (ea instanceof MushroomCow && config.getFilecon().getBoolean("creature.mushroom-cow.shear-all") && sm.amountMap.get(ea.getUniqueId()) > 1){
                    multiplyDrops(new ItemStack(Material.RED_MUSHROOM), (sm.amountMap.get(ea.getUniqueId()) - 1) * 5, ea.getLocation());
                    ItemStack isa = e.getPlayer().getItemInHand();
                    isa.setDurability((short)(isa.getDurability() + sm.amountMap.get(ea.getUniqueId())));
                    e.getPlayer().setItemInHand(isa);
                    Entity es = ea.getWorld().spawnEntity(ea.getLocation(), EntityType.COW);
                    sm.noStack.add(es.getUniqueId());
                    sm.amountMap.put(es.getUniqueId(), sm.amountMap.get(ea.getUniqueId()) - 1);
                    sm.amountMap.remove(ea.getUniqueId());
                } else if (ea instanceof MushroomCow && config.getFilecon().getBoolean("creature.mushroom-cow.divide-on-shear")){
                    LivingEntity le = (LivingEntity) eu.createEntity(ea, false, true);
                    sm.amountMap.put(le.getUniqueId(), (sm.amountMap.get(ea.getUniqueId()) - 1));
                    sm.noStack.add(le.getUniqueId());
                }
            }
        }
    }

    public float generateRandom(int amount){
        SplittableRandom rand = new SplittableRandom();
        double a = rand.nextDouble(2.5) + rand.nextDouble();
        double b = a * amount;
        return Math.round(b);
    }


    public void multiplyDrops(ItemStack item, double itemAmount, Location location){
        double inStacks = itemAmount / 64;
        double stacksOnly = Math.floor(inStacks);
        double leftOvers = (inStacks - stacksOnly) * 64;
        for(int i = 1; i <= stacksOnly; i++){
            ItemStack isa = new ItemStack(item.getType(), 64, item.getDurability());
            location.getWorld().dropItemNaturally(location, isa);
        }
        sm.getLogger().info(leftOvers + " " + inStacks + " " + stacksOnly);
        ItemStack isa = new ItemStack(item.getType(), (int) leftOvers, item.getDurability());
        location.getWorld().dropItemNaturally(location, isa);
    }
}
