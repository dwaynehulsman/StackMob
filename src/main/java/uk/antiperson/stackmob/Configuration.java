package uk.antiperson.stackmob;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;

/**
 * Created by nathat on 02/10/16.
 */
public class Configuration {

    private File file;
    private FileConfiguration filecon;
    private StackMob m;
    public Configuration(StackMob m){
        this.m = m;
        file = new File(m.getDataFolder(), "config.yml");
        filecon = YamlConfiguration.loadConfiguration(file);
    }

    public void makeConfig(){
        filecon.options().header("Thank you for downloading StackMob by antiPerson!\nPlease leave a review if this plugin has benefited your server!\n\nIf you need help post a reply to https://www.spigotmc.org/threads/stackmob.184178/");
        filecon.set("plugin.loginupdatechecker", true);
        filecon.set("creature.tag.visiblenothovered", true);
        filecon.set("creature.tag.text", "&b%amount%x &6%entity%");
        filecon.set("creature.tag.betterentitynames", true);
        filecon.set("creature.tag.visible", true);
        filecon.set("creature.tag.updateinterval", 10);
        filecon.set("creature.tag.removeat.enabled", true);
        filecon.set("creature.tag.removeat.amount", 1);
        filecon.set("creature.radius.x", 10);
        filecon.set("creature.radius.y", 10);
        filecon.set("creature.radius.z", 10);
        filecon.set("creature.tamed.check", true);
        filecon.set("creature.tamed.removetag", true);
        filecon.set("creature.leashed.check", true);
        filecon.set("creature.move.merge", true);
        filecon.set("creature.move.mergeinterval", 100);
        filecon.set("creature.stack.max", 20);
        filecon.set("creature.stack.max-in-chunk", -1);
        filecon.set("creature.stack.delay", 1);
        filecon.set("creature.sheep.stacksamecolor", true);
        filecon.set("creature.sheep.stacksheared", true);
        filecon.set("creature.sheep.divideondye", true);
        filecon.set("creature.sheep.divideonshear", true);
        filecon.set("creature.sheep.shearall", false);
        filecon.set("creature.slime.stacksamesize", true);
        filecon.set("creature.horses.stacksamecolor", true);
        if(!Bukkit.getVersion().contains("1.11")){
            filecon.set("creature.skeleton.stacksametype", true);
            filecon.set("creature.zombie.stacksamevillager", true);
            filecon.set("creature.horses.stacksametype", true);
        }else{
            filecon.set("creature.stacksamellama", true);
        }
        filecon.set("creature.villager.stacksameprofession", true);
        filecon.set("creature.separateages", true);
        filecon.set("creature.disablemobai", false);
        filecon.set("creature.kill-step.enabled", false);
        filecon.set("creature.kill-step.min", 1);
        filecon.set("creature.kill-step.max", 10);
        filecon.set("creature.kill-all.enabled", false);
        filecon.set("creature.kill-all.drops.multiply", true);
        filecon.set("creature.kill-all.drops.blacklist-enabled", true);
        filecon.set("creature.kill-all.drops.blacklist", Collections.singletonList("IRON_CHESTPLATE"));
        filecon.set("creature.kill-all.drops.zombie-only-multiply-rottenflash", true);
        filecon.set("creature.kill-all.drops.ignore-armor", true);
        filecon.set("creature.kill-all.drops.chances-enabled", true);
        filecon.set("creature.kill-all.drops.chance.IRON_CHESTPLATE", 0.5);
        filecon.set("creature.kill-all.drops.chance.CARROT", 0.5);
        filecon.set("creature.all-damage.enabled", true);
        filecon.set("creature.all-damage.min", 0.2);
        filecon.set("creature.all-damage.max", 0.4);
        filecon.set("creature.no-targeting", false);
        filecon.set("creature.multiplyeggs", true);
        filecon.set("creature.divideonrename", true);
        filecon.set("creature.breeding", true);
        filecon.set("creature.multiplyexplosion", true);
        filecon.set("creature.keepfireondeath", true);
        filecon.set("creature.stackbackwards", false);
        filecon.set("creature.multiplyslimes", true);
        filecon.set("creature.blacklist.enabled", false);
        filecon.set("creature.blacklist.list", Arrays.asList("COW", "SHEEP", "PIG"));
        filecon.set("creature.whitelist.enabled", false);
        filecon.set("creature.whitelist.list", Arrays.asList("COW", "SHEEP", "PIG"));
        filecon.set("creature.worlds.enabled", false);
        filecon.set("creature.worlds.list", Collections.singletonList("world"));
        filecon.set("creature.spawnreasons.enabled", false);
        filecon.set("creature.spawnreasons.list", Collections.singletonList("SPAWNER"));
        filecon.set("creature.special.pig.maxstack", 25);
        filecon.set("creature.special.pig.displaytag", "&b%amount%&6 of the amazing %entity%");
        filecon.set("creature.special.pig.stackbackwards", false);
        filecon.set("creature.update-metadata", true);
        filecon.set("worldguard.enabled", false);
        filecon.set("worldguard.regions", Collections.singletonList("__global__"));
        filecon.set("worldguard.blacklist.enabled", false);
        filecon.set("worldguard.blacklist.regions", Collections.singletonList("__global__"));
        filecon.set("mythicmobs.enabled", false);
        filecon.set("mcmmo.disable-xp", false);
        filecon.set("mcmmo.use-whitelist", false);
        filecon.set("mcmmo.whitelist", Collections.singletonList("CREEPER"));
        try{
            filecon.save(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public File getFile(){
        return file;
    }

    public FileConfiguration getFilecon(){
        return filecon;
    }

    public void updateConfig(){
        boolean updated = false;
        if(!filecon.isBoolean("plugin.loginupdatechecker")){
            updated = true;
            filecon.set("plugin.loginupdatechecker", true);
        }
        if(!filecon.isString("creature.tag.text")){
            updated = true;
            filecon.set("creature.tag.visiblenothovered", filecon.getBoolean("creature.tagvisiblenothovered"));
            filecon.set("creature.tag.text", "&b%amount% &6%entity%");
            filecon.set("creature.tamed.check", true);
            filecon.set("creature.tamed.removetag", true);
            filecon.set("creature.leashed.check", true);
        }
        if(!filecon.isBoolean("creature.move.merge")){
            updated = true;
            filecon.set("creature.move.merge", true);
            filecon.set("creature.move.mergeinterval", 100);
            filecon.set("creature.stack.max", 20);
        }
        if(!filecon.isBoolean("worldguard.enabled")){
            updated = true;
            filecon.set("creature.sheep.stacksamecolor", filecon.getBoolean("creature.sheepsamecolor"));
            filecon.set("creature.sheep.divideondye", true);
            filecon.set("worldguard.enabled", true);
            filecon.set("worldguard.regions", Collections.singletonList("__global__"));
        }
        if(!filecon.isBoolean("creature.whitelist.enabled")){
            updated = true;
            filecon.set("creature.whitelist.enabled", false);
            filecon.set("creature.whitelist.list", Arrays.asList("COW", "SHEEP", "PIG"));
        }
        if(!filecon.isBoolean("creature.sheep.stacksheared")){
            updated = true;
            filecon.set("creature.separateages", true);
            filecon.set("creature.disablemobai", false);
            filecon.set("creature.sheep.divideonshear", true);
            filecon.set("creature.sheep.stacksheared", true);
            filecon.set("creature.slime.stacksamesize", true);
            filecon.set("creature.tag.visible", true);
            filecon.set("creature.tag.removeat.enabled", true);
            filecon.set("creature.tag.removeat.amount", 1);
            filecon.set("mythicmobs.enabled", false);
            filecon.set("creature.tag.updateinterval", 10);
            filecon.set("creature.killall", false);
        }
        if(!filecon.isBoolean("creature.sheep.shearall")){
            updated = true;
            filecon.set("creature.worlds.enabled", true);
            filecon.set("creature.worlds.list", Collections.singletonList("world"));
            filecon.set("creature.sheep.shearall", false);
            filecon.set("creature.tag.text", filecon.getString("creature.tag.text").replace("%amount%", "%amount%x"));
            filecon.set("creature.spawnreasons.enabled", false);
            filecon.set("creature.spawnreasons.list", Collections.singletonList("SPAWNER"));
            filecon.set("worldguard.blacklist.enabled", false);
            filecon.set("worldguard.blacklist.regions", Collections.singletonList("__global__"));
        }
        if(!filecon.isBoolean("creature.horses.stacksametype")){
                updated = true;
            filecon.set("creature.horses.stacksametype", true);
            filecon.set("creature.horses.stacksamecolor", true);
            filecon.set("creature.villager.stacksameprofession", true);
            filecon.set("creature.zombie.stacksamevillager", true);
            filecon.set("creature.divideonrename", true);
            filecon.set("creature.tag.betterentitynames", true);
        }
        if(!filecon.isBoolean("creature.multiplyeggs")){
            updated = true;
            filecon.set("creature.multiplyeggs", true);
        }
        if(!filecon.isBoolean("creature.skeleton.stacksametype")){
            updated = true;
            filecon.set("creature.skeleton.stacksametype", true);
        }
        if(!filecon.isBoolean("creature.breeding")){
            updated = true;
            filecon.set("creature.breeding", true);
            filecon.set("creature.keepfireondeath", true);
            filecon.set("creature.special.pig.maxstack", 25);
            filecon.set("creature.special.pig.displaytag", "&b%amount%&6 of the amazing %entity%");
        }
        if(!filecon.isBoolean("creature.stackbackwards")){
            updated = true;
            filecon.set("creature.stackbackwards", false);
        }
        if(!filecon.isBoolean("creature.stacksamellama") && Bukkit.getVersion().contains("1.11")){
            updated = true;
            filecon.set("creature.stacksamellama", true);
        }
        if(!filecon.isBoolean("creature.multiplyexplosion")){
            updated = true;
            filecon.set("creature.multiplyexplosion", true);
        }
        if(!filecon.isBoolean("creature.multiplyslimes")){
            updated = true;
            filecon.set("creature.multiplyslimes", true);
        }
        if(!filecon.isInt("creature.stack.delay")){
            updated = true;
            filecon.set("creature.stack.delay", 1);
        }
        if(!filecon.isConfigurationSection("mcmmo")){
            updated = true;
            filecon.set("mcmmo.disable-xp", false);
            filecon.set("mcmmo.use-whitelist", false);
            filecon.set("mcmmo.whitelist", Collections.singletonList("CREEPER"));
        }
        if(!filecon.isBoolean("creature.no-targeting")){
            updated = true;
            filecon.set("creature.kill-step.enabled", false);
            filecon.set("creature.kill-step.min", 1);
            filecon.set("creature.kill-step.max", 10);
            filecon.set("creature.kill-all.enabled", filecon.getBoolean("creature.killall"));
            filecon.set("creature.kill-all.drops.multiply", true);
            filecon.set("creature.kill-all.drops.chances-enabled", true);
            filecon.set("creature.kill-all.drops.multiply", true);
            filecon.set("creature.kill-all.drops.blacklist-enabled", true);
            filecon.set("creature.kill-all.drops.blacklist", Collections.singletonList("IRON_CHESTPLATE"));
            filecon.set("creature.kill-all.drops.ignore-armor", true);
            filecon.set("creature.kill-all.drops.chance.IRON_CHESTPLATE", 0.5);
            filecon.set("creature.kill-all.drops.chance.CARROT", 0.5);
            filecon.set("creature.all-damage.enabled", true);
            filecon.set("creature.all-damage.min", 0.2);
            filecon.set("creature.all-damage.max", 0.4);
            filecon.set("creature.no-targeting", false);
        }
        if(!filecon.isBoolean("creature.update-metadata")){
            updated = true;
            filecon.set("creature.update-metadata", true);
            filecon.set("creature.kill-all.drops.zombie-only-multiply-rottenflash", true);
        }
        if(updated){
            m.getLogger().log(Level.INFO, "Updating your configuration file to the latest version...");
            try {
                filecon.save(file);
                m.getLogger().log(Level.INFO, "Updated your configuration file!");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void reloadConfig(){
        file = new File(m.getDataFolder(), "config.yml");
        filecon = YamlConfiguration.loadConfiguration(file);
    }
}
