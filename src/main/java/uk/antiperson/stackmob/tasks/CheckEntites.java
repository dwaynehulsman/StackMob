package uk.antiperson.stackmob.tasks;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.*;
import uk.antiperson.stackmob.api.events.EntityStackEvent;
import uk.antiperson.stackmob.api.MergeType;
import uk.antiperson.stackmob.plugins.WorldGuard;
import uk.antiperson.stackmob.utils.EntityUtils;

import java.util.List;

/**
 * Created by nathat on 08/10/16.
 */
public class CheckEntites extends BukkitRunnable {


    private Configuration config;
    private StackMob sm;
    private uk.antiperson.stackmob.plugins.MythicMobs mm;
    private EntityUtils eu;
    public CheckEntites(StackMob sm) {
        config = sm.config;
        this.sm = sm;
        if(Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            mm = new uk.antiperson.stackmob.plugins.MythicMobs(sm);
        }
        eu = new EntityUtils(sm);
    }

    @Override
    public void run() {
        double x = config.getFilecon().getDouble("creature.radius.x");
        double y = config.getFilecon().getDouble("creature.radius.y");
        double z = config.getFilecon().getDouble("creature.radius.z");
        boolean blacklistEnabled = config.getFilecon().getBoolean("creature.blacklist.enabled");
        boolean whitelistEnabled = config.getFilecon().getBoolean("creature.whitelist.enabled");
        boolean sheepSameColor = config.getFilecon().getBoolean("creature.sheep.stacksamecolor");
        boolean slimeSameSize = config.getFilecon().getBoolean("creature.slime.stacksamesize");
        boolean sheepSheared = config.getFilecon().getBoolean("creature.sheep.stacksheared");
        boolean separateAges = config.getFilecon().getBoolean("creature.separateages");
        boolean worldsEnabled = config.getFilecon().getBoolean("creature.worlds.enabled");
        boolean horseSameType = config.getFilecon().getBoolean("creature.horses.stacksametype");
        boolean horseSameColor = config.getFilecon().getBoolean("creature.horses.stacksamecolor");
        boolean villagerStackSameProfession = config.getFilecon().getBoolean("creature.villager.stacksameprofession");
        boolean zombieIsVillager = config.getFilecon().getBoolean("creature.zombie.stacksamevillager");
        boolean sameSkeleton = config.getFilecon().getBoolean("creature.skeleton.stacksametype");
        boolean checkBreeding = config.getFilecon().getBoolean("creature.breeding");
        boolean sameLlama = config.getFilecon().getBoolean("creature.stacksamellama");
        boolean sameParrot = config.getFilecon().getBoolean("creature.check-parrot-color");
        List<String> worlds = config.getFilecon().getStringList("creature.worlds.list");
        List<String> blacklist = config.getFilecon().getStringList("creature.blacklist.list");
        List<String> whitelist = config.getFilecon().getStringList("creature.whitelist.list");
        int maxStack = config.getFilecon().getInt("creature.stack.max");
        for (World w : Bukkit.getWorlds()) {
            if(worldsEnabled){
                if(!worlds.contains(w.getName())){
                    continue;
                }
            }
            for (LivingEntity e : w.getLivingEntities()) {
                if(!sm.amountMap.containsKey(e.getUniqueId())){
                    continue;
                }
                if(sm.isLegacy()){
                    if (e.getType() == EntityType.ARMOR_STAND) {
                        continue;
                    }
                }
                if(e.getType() == EntityType.UNKNOWN){
                    return;
                }
                if (blacklistEnabled) {
                    if(blacklist.contains(e.getType().toString())){
                       continue;
                    }
                }
                if (whitelistEnabled) {
                    if(!whitelist.contains(e.getType().toString())){
                        continue;
                    }
                }
                if (checks(e)) {
                    continue;
                }
                for (Entity le : e.getNearbyEntities(x, y, z)) {
                    if (le instanceof LivingEntity && sm.amountMap.containsKey(le.getUniqueId())) {
                        if (!checks((LivingEntity) le)) {
                            if (e.getType() == le.getType()) {
                                if (sheepSameColor && le instanceof Sheep) {
                                    if ((((Sheep) e).getColor() != ((Sheep) le).getColor())) {
                                        continue;
                                    }
                                }
                                if (separateAges && (le instanceof Ageable || le instanceof Zombie)) {
                                    if(le instanceof Zombie){
                                        if(((Zombie)le).isBaby() != ((Zombie)e).isBaby()){
                                            continue;
                                        }
                                    }else{
                                        Ageable aea = (Ageable) le;
                                        Ageable ie = (Ageable) e;
                                        if (aea.isAdult() != ie.isAdult()) {
                                            continue;
                                        }
                                    }
                                }
                                if (sheepSheared && le instanceof Sheep) {
                                    if ((((Sheep) e).isSheared() != ((Sheep) le).isSheared())) {
                                        continue;
                                    }
                                }
                                if (slimeSameSize && e instanceof Slime) {
                                    if (((Slime) e).getSize() != ((Slime) le).getSize()) {
                                        continue;
                                    }
                                }
                                if(horseSameColor && e instanceof Horse){
                                    if(((Horse)e).getColor() != ((Horse)le).getColor()){
                                        continue;
                                    }
                                }
                                if(villagerStackSameProfession && e instanceof Villager){
                                    if(((Villager)e).getProfession() != ((Villager)le).getProfession()){
                                        continue;
                                    }
                                }
                                if(Bukkit.getVersion().contains("1.12")){
                                    if(sameParrot && e instanceof Parrot){
                                        if(((Parrot)e).getVariant() != ((Parrot)le).getVariant()){
                                            continue;
                                        }
                                    }
                                }
                                if(!Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12")){
                                    if(horseSameType && e instanceof Horse){
                                        if(((Horse)e).getDomestication() != ((Horse)le).getDomestication()){
                                            continue;
                                        }
                                    }
                                    if(zombieIsVillager && e instanceof Zombie ){
                                        if(!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.7")){
                                            if(((Zombie)e).getVillagerProfession() != ((Zombie)le).getVillagerProfession()){
                                                continue;
                                            }
                                        }else{
                                            if(((Zombie)e).isVillager() != ((Zombie)le).isVillager()){
                                                continue;
                                            }
                                        }
                                    }
                                    if(sameSkeleton && e instanceof Skeleton){
                                        if(((Skeleton)e).getSkeletonType() != ((Skeleton)le).getSkeletonType()){
                                            continue;
                                        }
                                    }
                                }else {
                                    if(horseSameType && e instanceof AbstractHorse){
                                        if(((AbstractHorse)e).getDomestication() != ((AbstractHorse)le).getDomestication()){
                                            continue;
                                        }
                                    }
                                    if(sameLlama && e instanceof Llama){
                                        if(((Llama)e).getColor() != ((Llama) le).getColor()){
                                            continue;
                                        }
                                    }
                                }
                                if(checkBreeding && e instanceof Animals){
                                    if(((Animals)e).canBreed() != ((Animals)le).canBreed()){
                                        continue;
                                    }
                                }
                                if(Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null){
                                    if(config.getFilecon().getBoolean("mythicmobs.enabled")) {
                                        if (mm.getMythicMobs().isActiveMob(e.getUniqueId()) && mm.getMythicMobs().isActiveMob(le.getUniqueId())) {
                                            ActiveMob am = mm.getMythicMobs().getMythicMobInstance(e);
                                            ActiveMob am2 = mm.getMythicMobs().getMythicMobInstance(le);
                                            if (am.getType() != am2.getType()) {
                                                continue;
                                            }
                                        }else if(mm.getMythicMobs().isActiveMob(e.getUniqueId()) != mm.getMythicMobs().isActiveMob(le.getUniqueId())){
                                            continue;
                                        }
                                    }
                                }
                                if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
                                    WorldGuard wg = new WorldGuard(sm);
                                    if(wg.checkEntitesInRegion(e, le)){
                                        continue;
                                    }
                                }
                                if(sm.amountMap.containsKey(e.getUniqueId()) && sm.amountMap.containsKey(le.getUniqueId())) {
                                    if ((sm.amountMap.get(le.getUniqueId()) <= maxStack && sm.amountMap.get(e.getUniqueId()) <= maxStack) || maxStack == -1) {
                                        ok(le, e, maxStack);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean checks(LivingEntity e){
        boolean tamedCheck = config.getFilecon().getBoolean("creature.tamed.check");
        boolean leashCheck = config.getFilecon().getBoolean("creature.leashed.check");
        if(tamedCheck && e instanceof Tameable){
            if(((Tameable)e).isTamed()){
                return true;
            }
        }
        if(leashCheck){
            if(e.isLeashed()){
                return true;
            }
        }
        if(e.hasMetadata("FactionMob")){
            return true;
        }
        if(e.hasMetadata("NPC")){
            return true;
        }
        if(sm.noStackingAtAll.contains(e.getUniqueId())){
            return true;
        }
        return false;
    }

    private void ok(Entity p ,Entity e, int maxStack){
        int mazStack = maxStack;
        if(p != null && e != null){
            if(config.getFilecon().getInt("creature.special." + p.getType().toString().toLowerCase() + ".maxstack")  != 0){
                mazStack = config.getFilecon().getInt("creature.special." + p.getType().toString().toLowerCase() + ".maxstack");
            }
            if(!sm.mobUuids.contains(e.getUniqueId())) {
                int a = sm.amountMap.get(p.getUniqueId()) + sm.amountMap.get(e.getUniqueId());
                EntityStackEvent ese = new EntityStackEvent(p, e, sm, MergeType.TWO_MEET);
                Bukkit.getServer().getPluginManager().callEvent(ese);
                if(ese.isCancelled()){
                    return;
                }
                if (a > mazStack) {
                    if ((sm.amountMap.get(p.getUniqueId()) != mazStack) && (sm.amountMap.get(e.getUniqueId()) != mazStack)) {
                        e.remove();
                        int a1 = a - mazStack;
                        Entity neb = eu.createEntity(p, false, true);
                        sm.amountMap.put(neb.getUniqueId(), a1);
                        sm.amountMap.put(p.getUniqueId(), mazStack);
                        sm.amountMap.remove(e.getUniqueId());
                        sm.noStack.add(neb.getUniqueId());
                    }
                } else {
                    if(config.getFilecon().getBoolean("creature.bigger-priority")){
                        if(sm.amountMap.get(p.getUniqueId()) >= sm.amountMap.get(e.getUniqueId())){
                            e.remove();
                            sm.amountMap.put(p.getUniqueId(), a);
                            sm.amountMap.remove(e.getUniqueId());
                        }else{
                            p.remove();
                            sm.amountMap.put(e.getUniqueId(), a);
                            sm.amountMap.remove(p.getUniqueId());
                        }
                    }else{
                        p.remove();
                        sm.amountMap.put(e.getUniqueId(), a);
                        sm.amountMap.remove(p.getUniqueId());
                    }
                }
            }else{
                sm.mobUuids.remove(e.getUniqueId());
            }
        }
    }
}
