package uk.antiperson.stackmob.utils;

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
    private StackMob sm;
    public CreatureData(StackMob sm){
        file = new File(sm.getDataFolder(), "mobdata.yml");
        filecon = YamlConfiguration.loadConfiguration(file);
        this.sm = sm;
    }

    public void makeStore(){
        filecon.options().header("There is nothing that needs to be changed in this file!\nThis file is only used for the storage of creatures in a world!");
        for(UUID uuid : sm.amountMap.keySet()){
            filecon.set(uuid.toString(), sm.amountMap.get(uuid));
        }
        try{
            filecon.save(file);
            sm.getLogger().log(Level.INFO, "Saved all creature amount data!");
        }catch(IOException e){
            sm.getLogger().log(Level.SEVERE, "There was a problem while saving the creature data!");
        }
    }

    public void loadStore(){
        for(String s : filecon.getKeys(false)){
            sm.amountMap.put(UUID.fromString(s), filecon.getInt(s));
        }
        file.delete();
    }
}
