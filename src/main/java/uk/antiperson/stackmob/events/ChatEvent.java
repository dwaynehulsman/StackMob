package uk.antiperson.stackmob.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by nathat on 15/05/17.
 */
public class ChatEvent implements Listener {

    private StackMob sm;
    private String pluginTag = ChatColor.DARK_PURPLE + "[" + ChatColor.AQUA + "StackMob" + ChatColor.DARK_PURPLE + "] ";
    public ChatEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){
        if(sm.added.containsKey(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
            try{
                int i = Integer.parseInt(e.getMessage());
                if(i > 0){
                    sm.amountMap.put(sm.added.get(e.getPlayer().getUniqueId()), i);
                    sm.added.remove(e.getPlayer().getUniqueId());
                    e.getPlayer().sendMessage(pluginTag + ChatColor.GREEN + "Successfully added the entity to the database!");
                }else{
                    e.getPlayer().sendMessage(pluginTag + ChatColor.DARK_RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                            "Please enter a number from 1 to " + Integer.MAX_VALUE);
                }
            }catch (NumberFormatException npe){
                npe.printStackTrace();
                e.getPlayer().sendMessage(pluginTag + ChatColor.DARK_RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                        "Please enter a number from 1 to " + Integer.MAX_VALUE);
            }
        }
    }
}
