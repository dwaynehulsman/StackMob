package uk.antiperson.stackmob.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

/**
 * Created by nathat on 03/10/16.
 */
public class Updater{

    private StackMob m;
    public Updater(StackMob m){
        this.m = m;
    }

    public void checkUpdate(final boolean isPlayer, final Player p){
        m.getServer().getScheduler().runTaskAsynchronously(m, new Runnable() {
            @Override
            public void run() {
                String pluginTag = ChatColor.DARK_PURPLE + "[" + ChatColor.AQUA + "StackMob" + ChatColor.DARK_PURPLE + "] ";
                try {
                    if(!isPlayer){
                        m.getLogger().log(Level.INFO, "Currently checking SpigotMC for plugin updates...");
                    }
                    HttpURLConnection connect = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
                    connect.setDoOutput(true);
                    connect.setRequestMethod("POST");
                    connect.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=29999").getBytes("UTF-8"));
                    String version = new BufferedReader(new InputStreamReader(connect.getInputStream())).readLine();
                    if (!m.getDescription().getVersion().equals(version.replace("v", ""))) {
                        if(isPlayer) {
                            p.sendMessage(pluginTag + ChatColor.GREEN + "You are currently on v" + m.getDescription().getVersion() + " and a new version, " + version + " is available.");
                            p.sendMessage(pluginTag + ChatColor.GREEN + "Make sure to run '/sm download' to get the latest version.");
                        }else{
                            m.getLogger().log(Level.INFO, "You are currently on v" + m.getDescription().getVersion() + ", and a new version, " + version + " is available!");
                            m.getLogger().log(Level.INFO, "Make sure to run '/sm download' to get the latest version.");
                        }
                    }else{
                        if(isPlayer) {
                            p.sendMessage(pluginTag + ChatColor.GREEN + "You are currently on the latest version of this plugin (v" + m.getDescription().getVersion() + ")");
                        }else{
                            m.getLogger().log(Level.INFO, "No new updates for StackMob have been found!");
                        }
                    }

                } catch (Exception e) {
                    if(isPlayer) {
                        p.sendMessage(pluginTag + ChatColor.RED + "Unable to contact spigot, is your server offline?");
                    }else{
                        m.getLogger().log(Level.SEVERE, "Unable to contact spigot, is your server offline?");
                    }
                }
            }
        });
    }

    // &spiget__ua=StackMob-Updater
    public void downloadUpdate() throws Exception{
        FileUtils.copyURLToFile(new URL("https://api.spiget.org/v2/resources/29999/download"),
                new File(m.getDataFolder().toString().replace("StackMob", "") + File.separator + "update", "StackMob.jar"));
    }
}
