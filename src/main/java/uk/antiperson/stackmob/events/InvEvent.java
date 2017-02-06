package uk.antiperson.stackmob.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import uk.antiperson.stackmob.StackMob;

import java.util.UUID;

/**
 * Created by nathat on 03/02/17.
 */
public class InvEvent implements Listener {

    private StackMob sm;
    public InvEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e){
        if(e.getInventory().getName() != null && ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Stack Information:")){
            if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null){
                Entity ea = getEntity(sm.playerToMob.get(e.getWhoClicked().getUniqueId()));
                if(ea != null){
                    if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Remove from database.")){
                        sm.amountMap.remove(ea.getUniqueId());
                        closeInventory(e.getWhoClicked());
                        sm.playerToMob.remove(e.getWhoClicked().getUniqueId());
                    }else if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Remove from database and existence.")){
                        sm.amountMap.remove(ea.getUniqueId());
                        ea.remove();
                        closeInventory(e.getWhoClicked());
                        sm.playerToMob.remove(e.getWhoClicked().getUniqueId());
                    }
                    e.setCancelled(true);
                }else{
                    closeInventory(e.getWhoClicked());
                }
            }
        }
    }

    public Entity getEntity(UUID uuid){
        Entity ea = null;
        for(World world : Bukkit.getWorlds()){
            for(Entity e : world.getEntities()){
                if(e.getUniqueId().equals(uuid)){
                    ea = e;
                    break;
                }
            }
        }
        return ea;
    }

    public void closeInventory(final HumanEntity p){
        sm.getServer().getScheduler().runTask(sm, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        });
    }
}
