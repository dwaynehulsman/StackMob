package uk.antiperson.stackmob;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import uk.antiperson.stackmob.api.StackMobAPI;
import uk.antiperson.stackmob.events.*;
import uk.antiperson.stackmob.events.entity.*;
import uk.antiperson.stackmob.plugins.WorldGuard;
import uk.antiperson.stackmob.tasks.CheckEntites;
import uk.antiperson.stackmob.tasks.MetadataUpdater;
import uk.antiperson.stackmob.tasks.TagUpdater;
import uk.antiperson.stackmob.utils.CreatureData;
import uk.antiperson.stackmob.utils.Updater;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by nathat on 02/10/16.
 */
public class StackMob extends JavaPlugin {

    // Prevent from getting merged on the CheckEntities task.
    public HashSet<UUID> mobUuids = new HashSet<UUID>();
    // Map of the colour that is dyed in a stack of sheep
    public HashMap<UUID, DyeColor> lastDyed = new HashMap<UUID, DyeColor>();
    // Contains monster amounts
    public HashMap<UUID, Integer> amountMap = new HashMap<UUID, Integer>();
    // Prevent from stacking on first spawn
    public HashSet<UUID> noStack = new HashSet<UUID>();
    // Wand of stacked mobs map, player UUID to monster UUID
    public HashMap<UUID, UUID> playerToMob = new HashMap<UUID, UUID>();
    public Configuration config;

    public boolean firstTime = false;

    @Override
    public void onEnable(){
        long time = new Date().getTime();
        config = new Configuration(this);
        Updater up = new Updater(this);
        getLogger().log(Level.INFO, "StackMob v" + getDescription().getVersion() + " by antiPerson");
        getLogger().log(Level.INFO, "Find out more at " + getDescription().getWebsite());
        getLogger().log(Level.INFO, "Please leave a review on this plugin - it helps a lot.");
        if(!config.getFile().exists()){
            firstTime = true;
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.isOp()){
                    firstTime = false;
                    p.sendMessage(ChatColor.YELLOW + "Hello there, " + ChatColor.BLUE + p.getName() + ChatColor.YELLOW + ". Thank you for downloading StackMob v" + getDescription().getVersion() + " by antiPerson. If you need help please make a post in the discussion thread and if you need to find something else you should find it on the plugin page. Also, if this plugin has benefited your server, please make sure to leave a review! (Link can be found by typing /sm about)");
                }
            }
            getLogger().log(Level.INFO, "Configuration file not found, making one for you...");
            config.makeConfig();
            getLogger().log(Level.INFO, "Configuration file made at " + config.getFile().getAbsolutePath());
        }else{
            config.updateConfig();
        }
        config = new Configuration(this);
        getLogger().log(Level.INFO, "Loading stack amounts from storage...");
        new CreatureData(this).loadStore();
        getLogger().log(Level.INFO, "Loaded stack amounts from storage!");
        getLogger().log(Level.INFO, "Registering commands and events...");
        if(config.getFilecon().getBoolean("creature.move.merge")) {
            new CheckEntites(this).runTaskTimer(this, 0, config.getFilecon().getLong("creature.move.mergeinterval"));
        }
        if(config.getFilecon().getBoolean("creature.tag.visible")) {
            new TagUpdater(this).runTaskTimer(this, 0, config.getFilecon().getLong("creature.tag.updateinterval"));
        }
        if(config.getFilecon().getBoolean("creature.update-metadata")){
            new MetadataUpdater(this).runTaskTimer(this, 0, 1);
        }
        getServer().getPluginManager().registerEvents(new SpawnEvent(this), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(this), this);
        if(config.getFilecon().getBoolean("creature.sheep.divideondye")) {
            getServer().getPluginManager().registerEvents(new DyeEvent(this), this);
        }
        getServer().getPluginManager().registerEvents(new PlayerInteractEntityEvent(this), this);
        if(config.getFilecon().getBoolean("creature.no-targeting")) {
            getServer().getPluginManager().registerEvents(new EntityTargetPlayerEvent(this), this);
        }
        if(config.getFilecon().getBoolean("creature.multiplyeggs")){
            getServer().getPluginManager().registerEvents(new ChickenLayEggEvent(this), this);
        }
        if(config.getFilecon().getBoolean("creature.multiplyexplosion")){
            getServer().getPluginManager().registerEvents(new CreeperExplodeEvent(this), this);
        }
        if(config.getFilecon().getBoolean("creature.kill-all.enabled") && config.getFilecon().getBoolean("creature.multiplyslimes")){
            getServer().getPluginManager().registerEvents(new SlimeDivide(this), this);
        }
        if(config.getFilecon().getBoolean("creature.all-damage.enabled")){
            getServer().getPluginManager().registerEvents(new EntityDamage(this), this);
        }
        getServer().getPluginManager().registerEvents(new ShearEvent(this), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new InvEvent(this), this);
        getCommand("sm").setExecutor(new Commands(this));
        getLogger().log(Level.INFO, "Registered all commands and events!");
        if(getServer().getVersion().contains("1.8")){
            getLogger().log(Level.INFO, "It appears that you are running a Spigot 1.8 Minecraft server.");
            getLogger().log(Level.WARNING, "This plugin will work, but if a  Minecraft 1.8 client joins your server, the tag won't always be visible regardless of the setting in the config. Minecraft 1.9 and newer clients won't be effected.");
        }
        if(!isLegacy()){
            getLogger().log(Level.INFO, "It appears that you are running a Spigot 1.7 (or older) Minecraft server.");
            getLogger().log(Level.WARNING, "Old versions are not offically supported!");
        }
        Plugin wgp = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if((wgp != null) && (wgp.isEnabled())  && config.getFilecon().getBoolean("worldguard.enabled")){
            WorldGuard wg = new WorldGuard(this);
            getLogger().log(Level.INFO, "WorldGuard v" + wg.getWorldGuard().getDescription().getVersion() + " has been detected!");
        }
        up.checkUpdate(false, null);
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Something went wrong while enabling metrics!");
        }
        getLogger().log(Level.INFO, "Loaded everything in " + (new Date().getTime() - time) + "ms");
    }

    @Override
    public void onDisable(){
        getLogger().log(Level.INFO, "Saving all creature amount data...");
        new CreatureData(this).makeStore();
        getServer().getScheduler().cancelTasks(this);
    }

    public StackMobAPI getAPI(){
        return new StackMobAPI(this);
    }

    public boolean isLegacy(){
        return !Bukkit.getServer().getVersion().contains("1.7") && !Bukkit.getServer().getVersion().contains("1.6") && !Bukkit.getServer().getVersion().contains("1.5");
    }
}
