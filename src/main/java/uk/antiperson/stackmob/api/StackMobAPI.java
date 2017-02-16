package uk.antiperson.stackmob.api;

import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by nathat on 06/11/16.
 */
public class StackMobAPI {

    private StackMob sm;
    public StackMobAPI(StackMob sm){
        this.sm = sm;
    }

    public EntityManager getEntityManager(){
        return new EntityManager(sm);
    }

    public Configuration getConfig(){
        return sm.config;
    }

    public Plugin getPlugin(){
        return sm;
    }
}
