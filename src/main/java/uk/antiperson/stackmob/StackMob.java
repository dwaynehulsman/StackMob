package uk.antiperson.stackmob;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import uk.antiperson.stackmob.api.StackMobAPI;
import uk.antiperson.stackmob.events.*;
import uk.antiperson.stackmob.events.entity.*;
import uk.antiperson.stackmob.plugins.WorldGuard;
import uk.antiperson.stackmob.tasks.CheckEntites;
import uk.antiperson.stackmob.tasks.MetadataUpdater;
import uk.antiperson.stackmob.tasks.TagUpdater;
import uk.antiperson.stackmob.utils.CreatureData;
import uk.antiperson.stackmob.utils.EntityTranslation;
import uk.antiperson.stackmob.utils.Updater;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by nathat on 02/10/16.
 */
public class StackMob extends JavaPlugin {

    // Prevent from getting merged on the CheckEntities task.
    public HashSet<UUID> mobUuids = new HashSet<UUID>();
    // Contains monster amounts (currently loaded in world)
    public HashMap<UUID, Integer> amountMap = new HashMap<UUID, Integer>();
    // Contains monster amounts (currently unloaded)
    public HashMap<Location, Integer> locMap = new HashMap<Location, Integer>();
    // Prevent from stacking on first spawn
    public HashSet<UUID> noStack = new HashSet<UUID>();
    // Wand of stacked mobs map, player UUID to monster UUID
    public HashMap<UUID, UUID> playerToMob = new HashMap<UUID, UUID>();
    // Prevent from stacking at all until removed.
    public HashSet<UUID> noStackingAtAll = new HashSet<UUID>();
    // Currently in love entities
    public HashSet<UUID> love = new HashSet<UUID>();
    // If the entity need to added to database (player, monster)
    public HashMap<UUID, UUID> added = new HashMap<UUID, UUID>();

    public Configuration config;
    public EntityTranslation et;

    public boolean firstTime = false;
    public boolean debuggerMode = false;

    @Override
    public void onEnable(){
        long time = new Date().getTime();
        config = new Configuration(this);
        et = new EntityTranslation(this);
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
        if(!config.getFile().exists()){
            getLogger().log(Level.INFO, "Entity translations file not found, making one for you...");
            et.generateTranslation();
            getLogger().log(Level.INFO, "Entity translations file made at " + et.getFile().getAbsolutePath());
        }else{
            et.updateTranslations();
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
        getServer().getPluginManager().registerEvents(new InteractEntityEvent(this), this);
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
        if(config.getFilecon().getBoolean("creature.damage-multiply.enabled")){
            getServer().getPluginManager().registerEvents(new EntityDamage(this), this);
        }
        getServer().getPluginManager().registerEvents(new EntityDamageByEntity(this), this);
        getServer().getPluginManager().registerEvents(new ShearEvent(this), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new InvEvent(this), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
        if(!config.getFilecon().getBoolean("plugin.use-old-data-format") && !isLegacy()){
            getServer().getPluginManager().registerEvents(new ChunkLoad(this), this);
            getServer().getPluginManager().registerEvents(new ChunkUnload(this), this);
        }
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
        Metrics metrics = new Metrics(this);
        getLogger().log(Level.INFO, "Loaded everything in " + (new Date().getTime() - time) + "ms");
    }

    @Override
    public void onDisable(){
        getLogger().log(Level.INFO, "Saving all creature amount data...");
        if(config.getFilecon().getBoolean("plugin.disable-data-saving")){
            for(World world : Bukkit.getWorlds()){
                for(Entity e : world.getLivingEntities()){
                    if(amountMap.get(e.getUniqueId()) != null){
                        e.remove();
                        amountMap.remove(e.getUniqueId());
                    }
                }
            }
        }else{
            new CreatureData(this).makeStore();
        }
        getServer().getScheduler().cancelTasks(this);
    }

    public StackMobAPI getAPI(){
        return new StackMobAPI(this);
    }

    // Is actually the inverse of that, for some reason.
    public boolean isLegacy(){
        return !Bukkit.getServer().getVersion().contains("1.7") && !Bukkit.getServer().getVersion().contains("1.6") && !Bukkit.getServer().getVersion().contains("1.5");
    }
}
