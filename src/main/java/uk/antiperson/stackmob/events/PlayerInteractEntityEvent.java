package uk.antiperson.stackmob.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.utils.EntityUtils;
import uk.antiperson.stackmob.StackMob;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nathat on 10/10/16.
 */
public class PlayerInteractEntityEvent implements Listener {

    private StackMob sm;
    private Configuration config;
    private String pluginTag = ChatColor.DARK_PURPLE + "[" + ChatColor.AQUA + "StackMob" + ChatColor.DARK_PURPLE + "] ";
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
            }else if(m == Material.STICK && itemStackVersionCorrect(e.getPlayer().getInventory()).getItemMeta() != null){
                if(ChatColor.stripColor(itemStackVersionCorrect(e.getPlayer().getInventory()).getItemMeta().getDisplayName()).equalsIgnoreCase("The wand of stacked monsters.")){
                    if(e.getPlayer().hasPermission("stackmob.admin") || e.getPlayer().hasPermission("stackmob.*")){
                        if(sm.amountMap.containsKey(e.getRightClicked().getUniqueId())){
                            Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Stack information:");

                            // Monster information
                            ItemStack is1 = new ItemStack(Material.BOOK, 1);
                            ItemMeta im = is1.getItemMeta();
                            im.setDisplayName(ChatColor.AQUA + "Monster information");
                            ArrayList<String> lore = new ArrayList<String>();
                            lore.add(ChatColor.GOLD + "Entity Type: " + ChatColor.YELLOW + e.getRightClicked().getType().toString());
                            lore.add(ChatColor.GOLD + "UUID: " + ChatColor.YELLOW + e.getRightClicked().getUniqueId());
                            lore.add(" ");
                            lore.add(ChatColor.GOLD + "Stack size: " + ChatColor.YELLOW + sm.amountMap.get(e.getRightClicked().getUniqueId()));
                            im.setLore(lore);
                            is1.setItemMeta(im);
                            // Remove from StackMob database.
                            ItemStack is2 = new ItemStack(Material.BARRIER, 1);
                            ItemMeta im2 = is2.getItemMeta();
                            im2.setDisplayName(ChatColor.RED + "Remove from database.");
                            is2.setItemMeta(im2);
                            // Kill and remove
                            ItemStack is3 = new ItemStack(Material.DIAMOND_SWORD, 1);
                            ItemMeta im3 = is3.getItemMeta();
                            im3.setDisplayName(ChatColor.RED + "Remove from database and existence.");
                            is3.setItemMeta(im3);

                            inv.setItem(0, is1);
                            inv.setItem(8, is2);
                            inv.setItem(7, is3);
                            e.getPlayer().openInventory(inv);
                            sm.playerToMob.put(e.getPlayer().getUniqueId(), e.getRightClicked().getUniqueId());
                        }else{
                            e.getPlayer().sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "Not an entity in the StackMob database!");
                        }
                    }else{
                        e.getPlayer().sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have permission to do this!");
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
