package me.apex.hades.check.impl.player.badpackets;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.PlaceEvent;
import me.apex.hades.user.User;
import me.apex.hades.util.PacketUtil;

@CheckInfo(name = "BadPackets", type = "F")
public class BadPacketsF extends Check {

    private int ticks;

    @Override
    public void onEvent(PacketEvent e, User user) {
        if (e instanceof PlaceEvent){
            PlaceEvent packet = (PlaceEvent)e;
            int ticks = this.ticks;
            this.ticks = 0;

            if (ticks < 2 && PacketUtil.isBlockPacket(packet.getItemStack().getType().toString())) {
                if (preVL++ > 4)
                    flag(user, "ticks = " + ticks);
            } else preVL = 0;
        }else if (e instanceof FlyingEvent){
            ticks++;
        }
    }
}
