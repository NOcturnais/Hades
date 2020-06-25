package me.apex.hades.check.impl.combat.reach;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.HadesPlugin;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.AttackEvent;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.user.User;
import me.apex.hades.user.UserManager;
import me.apex.hades.util.ReachUtil;
import me.apex.hades.util.boundingbox.AABB;
import me.apex.hades.util.boundingbox.Ray;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@CheckInfo(name = "Reach", type = "A")
public class ReachA extends Check {

    @Override
    public void init() {
        dev = true;
    }

    boolean attacked = false;
    Entity entity;

    //idea from sim0n
    //improved by LIWK / Johannes
    //Raytrace by Tecnio / undersquire

    @Override
    public void onHandle(PacketEvent e, User user) {
        if (e instanceof AttackEvent) {
            attacked = true;
            entity = ((AttackEvent) e).getEntity();
        } else if (e instanceof FlyingEvent) {
            if (attacked) {
                attacked = false;
                Ray ray = Ray.from(user);
                double dist = AABB.from(entity).collidesD(ray, 0, 10);
                if (dist == -1) {
                    return;
                }
                ReachUtil currentLocation = user.getReachLoc();
                ReachUtil previousLocation = user.getLastReachLoc();

                double range = UserManager.getUser((Player) entity).reachQueue.stream().mapToDouble(loc -> {
                    double distanceX = Math.min(Math.min(Math.abs(currentLocation.getX() - loc.getMinX()), Math.abs(currentLocation.getX() - loc.getMaxX())), Math.min(Math.abs(previousLocation.getX() - loc.getMinX()), Math.abs(previousLocation.getX() - loc.getMaxX())));
                    double distanceZ = Math.min(Math.min(Math.abs(currentLocation.getZ() - loc.getMinZ()), Math.abs(currentLocation.getZ() - loc.getMaxZ())), Math.min(Math.abs(previousLocation.getZ() - loc.getMinZ()), Math.abs(previousLocation.getZ() - loc.getMaxZ())));

                    return Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
                }).min().orElse(0.0);

                if (range >= HadesPlugin.getInstance().getConfig().getDouble("Max-Reach") && dist > 2.9) {
                    if (++preVL > 1) {
                        flag(user, "hitting farther than possbile. r: " + range + ", d: " + dist);
                    }
                } else preVL = 0;
            }
        }
    }
}
