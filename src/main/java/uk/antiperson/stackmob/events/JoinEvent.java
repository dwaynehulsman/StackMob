package uk.antiperson.stackmob.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.utils.Updater;

/**
 * Created by nathat on 18/10/16.
 */
public class JoinEvent implements Listener {

    private StackMob m;
    private Updater up;
    public JoinEvent(StackMob sm){
        m = sm;
        up = new Updater(sm);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();
        if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
            final Configuration config = new Configuration(m);
                BukkitScheduler scheduler = m.getServer().getScheduler();
                scheduler.runTaskLaterAsynchronously(m, new Runnable() {
                    @Override
                    public void run() {
                        if(config.getFilecon().getBoolean("plugin.loginupdatechecker")) {
                            up.checkUpdate(true, p);
                        }
                        if(m.firstTime){
                            p.sendMessage(ChatColor.YELLOW + "Hello there, " + ChatColor.BLUE + p.getName() + ChatColor.YELLOW + ". Thank you for downloading StackMob v" + m.getDescription().getVersion() + " by antiPerson. If you need help please make a post in the discussion thread and if you need to find something else you should find it on the plugin page. Also, if this plugin has benefited your server, please make sure to leave a review! (Link can be found by typing /sm about)");
                            m.firstTime = false;
                        }
                    }
                }, 20L);
        }

    }
}
