package uk.antiperson.stackmob.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.utils.EntityUtils;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by nathat on 10/10/16.
 */
public class PlayerInteractEntityEvent implements Listener {

    private StackMob sm;
    private Configuration config;
    public PlayerInteractEntityEvent(StackMob sm){
        this.sm = sm;
        config = sm.config;
    }

    @EventHandler
    public void onEntityInteract(org.bukkit.event.player.PlayerInteractEntityEvent e){
        Material m = itemStackVersionCorrect(e.getPlayer().getInventory()).getType();
        if(itemStackVersionCorrect(e.getPlayer().getInventory()) != null){
            if(m == Material.INK_SACK && config.getFilecon().getBoolean("creature.sheep.divideondye")) {
                if(e.getRightClicked().getType() == EntityType.SHEEP) {
                    if (sm.amountMap.containsKey(e.getRightClicked().getUniqueId())) {
                        if (sm.amountMap.get(e.getRightClicked().getUniqueId()) > 1) {
                            Sheep sh = (Sheep) e.getRightClicked();
                            sm.lastDyed.put(sh.getUniqueId(), sh.getColor());
                        }
                    }
                }
            }else if(m == Material.NAME_TAG && config.getFilecon().getBoolean("creature.divideonrename")){
                if(sm.amountMap.containsKey(e.getRightClicked().getUniqueId())){
                    if(sm.amountMap.get(e.getRightClicked().getUniqueId()) > 1){
                        EntityUtils eu = new EntityUtils(sm);
                        eu.createEntity(e.getRightClicked(), true, true);
                    }else{
                        sm.amountMap.remove(e.getRightClicked().getUniqueId());
                        e.getRightClicked().setCustomNameVisible(true);
                    }
                }
            }else if(correctFood(e.getRightClicked(), itemStackVersionCorrect(e.getPlayer().getInventory())) &&config.getFilecon().getBoolean("creature.breeding")){
                if(e.getRightClicked() instanceof Animals){
                    if(((Animals)e.getRightClicked()).canBreed()){
                        if(sm.amountMap.containsKey(e.getRightClicked().getUniqueId())){
                            if(sm.amountMap.get(e.getRightClicked().getUniqueId()) > 1){
                                EntityUtils eu = new EntityUtils(sm);
                                Entity ea = eu.createEntity(e.getRightClicked(), false, false);
                                sm.amountMap.put(ea.getUniqueId(), sm.amountMap.get(e.getRightClicked().getUniqueId()) - 1);
                                sm.amountMap.put(e.getRightClicked().getUniqueId(), 1);
                                sm.mobUuids.add(e.getRightClicked().getUniqueId());
                                sm.noStack.add(ea.getUniqueId());
                            }
                        }
                    }
                }
            }
        }
    }

    private ItemStack itemStackVersionCorrect(PlayerInventory inv){
        if(Bukkit.getVersion().contains("1.8") || sm.isLegacy()){
            return inv.getItemInHand();
        }else{
            return inv.getItemInMainHand();
        }
    }

    private boolean correctFood(Entity e, ItemStack is){
        if(e.getType() == EntityType.COW && is.getType() == Material.WHEAT){
            return true;
        }else if(e.getType() == EntityType.MUSHROOM_COW && is.getType() == Material.WHEAT){
            return true;
        }else if(e.getType() == EntityType.SHEEP && is.getType() == Material.WHEAT){
            return true;
        }else if(e.getType() == EntityType.WOLF && is.getType() == Material.BONE){
            return true;
        }else if(e.getType() == EntityType.CHICKEN && is.getType().toString().contains("SEEDS")){
            return true;
        }else if(e.getType() == EntityType.WOLF && (is.getType().toString().contains("RAW") || is.getType().toString().contains("COOKED")) && !is.getType().toString().contains("FISH")){
            return (((Wolf)e).isTamed());
        }else if(e.getType() == EntityType.OCELOT && (is.getType().toString().contains("RAW") || is.getType().toString().contains("COOKED")) && is.getType().toString().contains("FISH")){
            return (((Ocelot)e).isTamed());
        }else if(e.getType() == EntityType.HORSE && (is.getType() == Material.GOLDEN_APPLE || is.getType() == Material.GOLDEN_CARROT)){
            return true;
        }else if(e.getType() == EntityType.PIG && (is.getType() == Material.CARROT|| is.getType() == Material.POTATO)){
            return true;
        }else if(!Bukkit.getVersion().contains("1.7")){
            return (e.getType() == EntityType.RABBIT && (is.getType() == Material.CARROT || is.getType() == Material.GOLDEN_CARROT|| is.getType() == Material.YELLOW_FLOWER));
        }else{
            return false;
        }
    }
}
