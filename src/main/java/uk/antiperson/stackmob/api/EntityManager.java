package uk.antiperson.stackmob.api;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nathat on 06/11/16.
 */
public class EntityManager {

    private StackMob sm;
    public EntityManager(StackMob sm){
        this.sm = sm;
    }

    public StackedEntity getStackedEntity(Entity entity){
        return new StackedEntity(sm, entity);
    }

    public boolean isStackedEntity(Entity entity){
        if(sm.config.getFilecon().getBoolean("creature.update-metadata")) {
            return entity.hasMetadata("stackmob:stack-size");
        }else{
            return sm.amountMap.containsKey(entity.getUniqueId());
        }
    }

    public void addNewStack(Entity entity){
        sm.amountMap.put(entity.getUniqueId(), 1);
    }

    public void addNewStack(Entity entity, int amount){
        sm.amountMap.put(entity.getUniqueId(), amount);
    }

    public void removeStackedEntity(Entity entity){
        sm.amountMap.remove(entity.getUniqueId());
    }

    public void removeStackedEntity(StackedEntity stackedEntity){
        sm.amountMap.remove(stackedEntity.getEntity().getUniqueId());
    }

    public void killStackedEntity(StackedEntity stackedEntity){
        stackedEntity.getEntity().remove();
        sm.amountMap.remove(stackedEntity.getEntity().getUniqueId());
    }

    public List<StackedEntity> getAllStackedEntities(){
        List<StackedEntity> entities = new ArrayList<StackedEntity>();
        for(World w : Bukkit.getWorlds()){
            for(LivingEntity le : w.getLivingEntities()){
                if(sm.amountMap.containsKey(le.getUniqueId())){
                    entities.add(getStackedEntity(le));
                }
            }
        }
        return entities;
    }
}
