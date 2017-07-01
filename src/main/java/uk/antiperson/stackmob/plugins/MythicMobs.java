package uk.antiperson.stackmob.plugins;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
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

    public Entity spawnMythicMob(Entity ea) {
        ActiveMob am = getMythicMobs().getMythicMobInstance(ea);
        return getMythicMobs().spawnMob(am.getType().getInternalName(), ea.getLocation()).getLivingEntity();
    }

    public MobManager getMythicMobs(){
        io.lumine.xikage.mythicmobs.MythicMobs mm = (io.lumine.xikage.mythicmobs.MythicMobs) sm.getServer().getPluginManager().getPlugin("MythicMobs");
        return mm.getMobManager();
    }

    public String getMythicMobsVersion(){
        return sm.getServer().getPluginManager().getPlugin("MythicMobs").getDescription().getVersion();
    }
}
