package uk.antiperson.stackmob.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.antiperson.stackmob.StackMob;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by nathat on 21/10/16.
 */
public class CreatureData {

    private FileConfiguration filecon;
    private File file;
    private FileConfiguration filecon2;
    private File file2;
    private StackMob sm;
    public CreatureData(StackMob sm){
        file = new File(sm.getDataFolder(), "mobdata.yml");
        filecon = YamlConfiguration.loadConfiguration(file);
        file2 = new File(sm.getDataFolder(), "unloaded-mobdata.yml");
        filecon2 = YamlConfiguration.loadConfiguration(file2);
        this.sm = sm;
    }

    public void makeStore(){
        filecon.options().header("There is nothing that needs to be changed in this file!\nThis file is only used for the storage of creatures in a world!");
        for(UUID uuid : sm.amountMap.keySet()){
            filecon.set(uuid.toString(), sm.amountMap.get(uuid));
        }
        filecon2.options().header("There is nothing that needs to be changed in this file!\nThis file is only used for the storage of creatures in a world!");
        int counter = 0;
        for(Location loc : sm.locMap.keySet()){
            counter++;
            filecon2.set(counter + ".x", loc.getX());
            filecon2.set(counter + ".y", loc.getY());
            filecon2.set(counter + ".z", loc.getZ());
            filecon2.set(counter + ".world", loc.getWorld().getName());
            filecon2.set(counter + ".amount", sm.locMap.get(loc));
        }
        try{
            filecon.save(file);
            filecon2.save(file2);
            sm.getLogger().log(Level.INFO, "Saved all creature amount data!");
        }catch(IOException e){
            sm.getLogger().log(Level.SEVERE, "There was a problem while saving the creature data!");
        }
    }

    public void loadStore(){
        for(String s : filecon.getKeys(false)){
            sm.amountMap.put(UUID.fromString(s), filecon.getInt(s));
        }
        for(String s : filecon2.getKeys(false)){
            Double x = filecon2.getDouble(s + ".x");
            Double y = filecon2.getDouble(s + ".y");
            Double z = filecon2.getDouble(s + ".z");
            World world = Bukkit.getWorld(filecon2.getString(s + ".world"));
            sm.locMap.put(new Location(world, x, y, z), filecon2.getInt(s + ".amount"));
        }
        file.delete();
        file2.delete();
    }
}
