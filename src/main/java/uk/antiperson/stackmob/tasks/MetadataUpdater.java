package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by natha on 13/02/2017.
 */
public class MetadataUpdater extends BukkitRunnable {

    private StackMob sm;
    public MetadataUpdater(StackMob sm){
        this.sm = sm;
    }

    @Override
    public void run(){
        for(World world : Bukkit.getWorlds()){
            for(LivingEntity le : world.getLivingEntities()){
                if(sm.amountMap.containsKey(le.getUniqueId())){
                    le.setMetadata("stackmob:stack-size", new FixedMetadataValue(sm, sm.amountMap.get(le.getUniqueId())));
                }
            }
        }
    }
}
