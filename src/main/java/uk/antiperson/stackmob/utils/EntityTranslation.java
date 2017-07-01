package uk.antiperson.stackmob.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import uk.antiperson.stackmob.StackMob;

import java.io.File;
import java.io.IOException;

/**
 * Created by nathat on 16/04/17.
 */
public class EntityTranslation {

    private StackMob sm;
    private File file;
    private FileConfiguration filecon;
    private boolean updated = false;
    public EntityTranslation(StackMob sm){
        this.sm = sm;
        file = new File(sm.getDataFolder(), "entity-translation.yml");
        filecon = YamlConfiguration.loadConfiguration(file);
    }

    public void generateTranslation(){
        for(EntityType et : EntityType.values()){
            filecon.set(et.toString(), et.toString());
        }
        save();
    }

    public void updateTranslations(){
        for(EntityType et : EntityType.values()){
            if(!filecon.getKeys(false).contains(et.toString())){
                filecon.set(et.toString(), et.toString());
                updated = true;
            }
        }
        if(updated){
            sm.getLogger().info("Updating your entity translations file to the latest version...");
            save();
            sm.getLogger().info("Updated your entity translations file!");
        }
    }

    public String getTranslationName(Entity e){
        return filecon.getString(e.getType().toString());
    }

    public File getFile(){
        return file;
    }

    public void save(){
        try{
            filecon.save(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
