package uk.antiperson.stackmob.utils;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.plugins.MythicMobs;

/**
 * Created by nathat on 28/10/16.
 */
public class EntityUtils {
    
    private MythicMobs mm;
    private Configuration config;
    private StackMob st;
    private boolean zombieIsVillager;
    private boolean villagerStackSameProfession;
    private boolean horseSameColor;
    private boolean horseSameType;
    private boolean slimeSameSize;
    private boolean sheepSheared;
    private boolean separateAges;
    private boolean sheepSameColor;
    private boolean skeletonType;
    private boolean checkBreed;
    private boolean keepFire;
    private boolean sameLlama;
    public EntityUtils(StackMob sm){
        if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null){
            mm = new MythicMobs(sm);
        }
        config = sm.config;
        st = sm;
        sheepSameColor = config.getFilecon().getBoolean("creature.sheep.stacksamecolor");
        separateAges = config.getFilecon().getBoolean("creature.separateages");
        sheepSheared = config.getFilecon().getBoolean("creature.sheep.stacksheared");
        slimeSameSize = config.getFilecon().getBoolean("creature.slime.stacksamesize");
        horseSameType = config.getFilecon().getBoolean("creature.horses.stacksametype");
        horseSameColor = config.getFilecon().getBoolean("creature.horses.stacksamecolor");
        villagerStackSameProfession = config.getFilecon().getBoolean("creature.villager.stacksameprofession");
        zombieIsVillager = config.getFilecon().getBoolean("creature.zombie.stacksamevillager");
        skeletonType = config.getFilecon().getBoolean("creature.skeleton.stacksametype");
        checkBreed = config.getFilecon().getBoolean("creature.breeding");
        sameLlama = config.getFilecon().getBoolean("creature.stacksamellama");
        keepFire = config.getFilecon().getBoolean("creature.keepfireondeath");
    }
    
    public Entity createEntity(Entity ea, boolean a, boolean b){
        Entity en;
        if(Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            if (config.getFilecon().getBoolean("mythicmobs.enabled")) {
                if (mm.getMythicMobs().isMythicMob(ea)) {
                    en = mm.spawnMythicMob(ea);
                } else {
                    en = ea.getWorld().spawnEntity(ea.getLocation(), ea.getType());
                }
            } else {
                en = ea.getWorld().spawnEntity(ea.getLocation(), ea.getType());
            }
        } else {
            en = ea.getWorld().spawnEntity(ea.getLocation(), ea.getType());
        }
        if(sheepSameColor && ea instanceof Sheep){
            Sheep sh = (Sheep) en;
            Sheep sh1 = (Sheep) ea;
            sh.setColor(sh1.getColor());
        }
        if(separateAges && (ea instanceof Ageable || ea instanceof Zombie)){
            if(ea instanceof Zombie){
                ((Zombie)en).setBaby(((Zombie)ea).isBaby());
            }else{
                if(((Ageable)ea).isAdult()){
                    ((Ageable)en).setAdult();
                }else{
                    ((Ageable)en).setBaby();
                }
            }
        }
        if(sheepSheared && ea instanceof Sheep){
            ((Sheep)en).setSheared(((Sheep)ea).isSheared());
        }
        if(slimeSameSize && ea instanceof Slime){
            ((Slime)en).setSize(((Slime)ea).getSize());
        }
        if(horseSameColor && ea instanceof Horse ){
            ((Horse)en).setColor(((Horse)ea).getColor());
        }
        if(villagerStackSameProfession && ea instanceof Villager){
            ((Villager)en).setProfession(((Villager)ea).getProfession());
        }
        if(!Bukkit.getVersion().contains("1.11")){
            if(skeletonType && ea instanceof Skeleton){
                ((Skeleton)en).setSkeletonType(((Skeleton)ea).getSkeletonType());
            }
            if(zombieIsVillager && ea instanceof Zombie ){
                if(!Bukkit.getVersion().contains("1.8") && !st.isLegacy()) {
                    ((Zombie) en).setVillagerProfession(((Zombie) ea).getVillagerProfession());
                }else{
                    ((Zombie) en).setVillager(((Zombie) ea).isVillager());
                }
            }
            if(horseSameType && ea instanceof Horse){
                ((Horse)en).setDomestication(((Horse)ea).getDomestication());
            }
        }else{
            if(horseSameType && ea instanceof AbstractHorse){
                ((AbstractHorse)en).setDomestication(((AbstractHorse)ea).getDomestication());
            }
            if(sameLlama && ea instanceof Llama){
                ((Llama)en).setColor(((Llama)ea).getColor());
            }
        }
        if(keepFire){
            en.setFireTicks(ea.getFireTicks());
        }
        if(!Bukkit.getVersion().contains("1.8") && !st.isLegacy()) {
            ((LivingEntity) en).setAI(!config.getFilecon().getBoolean("creature.disablemobai"));
        }else if((Bukkit.getVersion().contains("1.8.8") || Bukkit.getVersion().contains("1.8.9")) && config.getFilecon().getBoolean("creature.disablemobai")){
            setAI(en);
        }
        if(b && checkBreed && ea instanceof Animals){
            ((Animals)en).setBreed(((Animals)ea).canBreed());
        }
        if(a){
            st.mobUuids.add(ea.getUniqueId());
            st.amountMap.put(en.getUniqueId(), (st.amountMap.get(ea.getUniqueId()) - 1));
            st.amountMap.remove(ea.getUniqueId());
            st.noStack.add(en.getUniqueId());
        }
        if(config.getFilecon().getBoolean("mcmmo.disable-xp") && st.isLegacy()){
            if(config.getFilecon().getBoolean("mcmmo.use-whitelist")){
                if(!config.getFilecon().getStringList("mcmmo.whitelist").contains(ea.getType().toString())){
                    ea.setMetadata("mcMMO: Spawned Entity", new FixedMetadataValue(st.getServer().getPluginManager().getPlugin("mcMMO"), false));
                }
            }else{
                ea.setMetadata("mcMMO: Spawned Entity", new FixedMetadataValue(st.getServer().getPluginManager().getPlugin("mcMMO"), false));
            }
        }
        return en;
    }

    public void setAI(Entity e){
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) e).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);
        tag.setBoolean("NoAI", true);
        EntityLiving el = (EntityLiving) nmsEntity;
        el.a(tag);
    }
}
