package uk.antiperson.stackmob.events;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import uk.antiperson.stackmob.StackMob;

import java.util.SplittableRandom;

/**
 * Created by nathat on 05/03/17.
 */
public class ChunkUnload implements Listener {

    private StackMob sm;
    public ChunkUnload(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e){
        for(Entity en : e.getChunk().getEntities()){
            if(!(en instanceof Creature)){
                continue;
            }
            if(en instanceof Monster && sm.amountMap.get(en.getUniqueId()) == 1){
                continue;
            }
            en.setCustomName(sm.amountMap.get(en.getUniqueId()) + "");
            sm.amountMap.remove(en.getUniqueId());
        }
    }
}
