package uk.antiperson.stackmob.events;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.StackMob;

/**
 * Created by nathat on 05/03/17.
 */
public class ChunkLoad implements Listener {

    private StackMob sm;
    public ChunkLoad(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e){
        for(Entity en : e.getChunk().getEntities()){
            if(!(en instanceof Creature)){
                continue;
            }

            if(Integer.valueOf(en.getCustomName()) != null){
                sm.amountMap.put(en.getUniqueId(), Integer.valueOf(en.getCustomName()));
                en.setCustomName(null);
            }



            //Legacy
            Location loc = new Location(en.getLocation().getWorld(), en.getLocation().getX(), en.getLocation().getY(), en.getLocation().getZ());
            if(sm.locMap.containsKey(loc)) {


                sm.amountMap.put(en.getUniqueId(), sm.locMap.get(loc));
                sm.locMap.remove(loc);

                if (Bukkit.getVersion().contains("1.8")) {
                    String version = Bukkit.getServer().getClass().getPackage().getName();
                    if (version.contains("v1_8_R1")) {
                        net.minecraft.server.v1_8_R1.Entity nms = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity) en).getHandle();
                        net.minecraft.server.v1_8_R1.NBTTagCompound nbt = nms.getNBTTag();
                        if (nbt == null) {
                            nbt = new net.minecraft.server.v1_8_R1.NBTTagCompound();
                        }
                        nms.c(nbt);
                        nbt.setByte("NoAI", (byte) 0);
                        nms.f(nbt);
                    } else if (version.contains("v1_8_R2")) {
                        net.minecraft.server.v1_8_R2.Entity nms1 = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity) en).getHandle();
                        net.minecraft.server.v1_8_R2.NBTTagCompound nbt1 = nms1.getNBTTag();
                        if (nbt1 == null) {
                            nbt1 = new net.minecraft.server.v1_8_R2.NBTTagCompound();
                        }
                        nms1.c(nbt1);
                        nbt1.setByte("NoAI", (byte) 0);
                        nms1.f(nbt1);
                    } else {
                        net.minecraft.server.v1_8_R3.Entity nms2 = ((CraftEntity) en).getHandle();
                        NBTTagCompound nbt2 = nms2.getNBTTag();
                        if (nbt2 == null) {
                            nbt2 = new NBTTagCompound();
                        }
                        nms2.c(nbt2);
                        nbt2.setByte("NoAI", (byte) 0);
                        nms2.f(nbt2);
                    }
                } else if (Bukkit.getVersion().contains("1.9")) {
                    ((LivingEntity) en).setAI(true);
                } else {
                    en.setGravity(true);
                }
            }
            if(sm.debuggerMode){
                for(Entity f : e.getChunk().getEntities()) {
                    if(!sm.amountMap.containsKey(f.getUniqueId()) && f.getCustomName() != null) {
                        if(f.getCustomName().toUpperCase().contains(f.getType().toString().replace("_", " "))){
                            sm.getLogger().info("DEBUGGER: Entity " + f.getUniqueId().toString() + ", " + f.getType().toString() + " appears to be a stacked mob, but isn't in the database? " + f.getLocation().toString());
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.teleport(f.getLocation());
                            }
                        }
                    }
                }
            }
        }



    }
}
