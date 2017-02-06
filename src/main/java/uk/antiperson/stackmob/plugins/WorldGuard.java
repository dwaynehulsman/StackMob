package uk.antiperson.stackmob.plugins;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.utils.States;

import java.util.List;

/**
 * Created by nathat on 28/10/16.
 */
public class WorldGuard {

    private StackMob sm;
    private boolean wgEnabled;
    private boolean wgBlackEnabled;
    private List<String> wgRegions;
    private List<String> wgBlackRegions;
    public WorldGuard(StackMob sm){
        this.sm = sm;
        Configuration config = sm.config;
        wgEnabled = config.getFilecon().getBoolean("worldguard.enabled");
        wgBlackEnabled = config.getFilecon().getBoolean("worldguard.blacklist.enabled");
        wgBlackRegions = config.getFilecon().getStringList("worldguard.blacklist.regions");
        wgRegions = config.getFilecon().getStringList("worldguard.regions");
    }

    public WorldGuardPlugin getWorldGuard(){
        return (WorldGuardPlugin) sm.getServer().getPluginManager().getPlugin("WorldGuard");
    }

    public boolean checkEntitiyInRegion(Entity ea){
        if (wgBlackEnabled || wgEnabled) {
            ApplicableRegionSet set2 = getWorldGuard().getRegionManager(ea.getWorld()).getApplicableRegions(ea.getLocation());
            if (wgEnabled) {
                States sta2 = States.ONE;
                for (ProtectedRegion s2 : set2) {
                    for (Object s : wgRegions) {
                        if (s2.getId().equals(s)) {
                            sta2 = States.TWO;
                            break;
                        }
                    }
                }
                if (sta2 == States.ONE) {
                    return true;
                }
            }
            if (wgBlackEnabled) {
                States sta2 = States.THREE;
                for (ProtectedRegion s2 : set2) {
                    for (Object s : wgBlackRegions) {
                        if (s2.getId().equals(s)) {
                            sta2 = States.ONE;
                            break;
                        }
                    }
                }
                if (sta2 == States.ONE) {
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }

    public boolean checkEntitesInRegion(Entity i, Entity ea){
        if(wgBlackEnabled || wgEnabled){
            ApplicableRegionSet set1 = getWorldGuard().getRegionManager(i.getWorld()).getApplicableRegions(i.getLocation());
            ApplicableRegionSet set2 = getWorldGuard().getRegionManager(ea.getWorld()).getApplicableRegions(ea.getLocation());
            if(wgEnabled){
                States sta2 = States.ONE;
                for (ProtectedRegion s1 : set1) {
                    for (ProtectedRegion s2 : set2) {
                        if (s1.getId().equals(s2.getId())) {
                            for (Object s : wgRegions) {
                                if (s1.getId().equals(s)) {
                                    sta2 = States.TWO;
                                    break;
                                }
                            }
                        }
                    }
                }
                if(sta2 == States.ONE){
                    return true;
                }
            }
            if(wgBlackEnabled){
                States sta2 = States.THREE;
                for (ProtectedRegion s1 : set1) {
                    for (ProtectedRegion s2 : set2) {
                        if (s1.getId().equals(s2.getId())) {
                            for (Object s : wgBlackRegions) {
                                if (s1.getId().equals(s)) {
                                    sta2 = States.ONE;
                                    break;
                                }
                            }
                        }
                    }
                }
                if(sta2 == States.ONE){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }
}
