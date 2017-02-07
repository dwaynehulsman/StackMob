package uk.antiperson.stackmob.plugins;

import net.elseland.xikage.MythicMobs.API.Exceptions.InvalidMobTypeException;
import net.elseland.xikage.MythicMobs.API.IMobsAPI;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by nathat on 22/10/16.
 */
public class MythicMobs {

    private StackMob sm;
    public MythicMobs(StackMob sm){
        this.sm = sm;
    }

    public Entity spawnMythicMob(Entity ea){
        ActiveMob am = getMythicMobs().getMythicMobInstance(ea);
        try{
            return getMythicMobs().spawnMythicMob(am.getType() ,ea.getLocation());
        }catch(InvalidMobTypeException esa){
            return ea.getWorld().spawnEntity(ea.getLocation(), ea.getType());
        }
    }

    public IMobsAPI getMythicMobs(){
        net.elseland.xikage.MythicMobs.MythicMobs mm = (net.elseland.xikage.MythicMobs.MythicMobs) sm.getServer().getPluginManager().getPlugin("MythicMobs");
        return mm.getAPI().getMobAPI();
    }

    public String getMythicMobsVersion(){
        return sm.getServer().getPluginManager().getPlugin("MythicMobs").getDescription().getVersion();
    }
}
