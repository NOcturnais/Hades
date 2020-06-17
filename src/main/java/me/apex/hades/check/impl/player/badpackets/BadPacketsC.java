package me.apex.hades.check.impl.player.badpackets;

import io.github.retrooper.packetevents.event.PacketEvent;
import me.apex.hades.check.Check;
import me.apex.hades.check.CheckInfo;
import me.apex.hades.event.impl.packetevents.FlyingEvent;
import me.apex.hades.event.impl.packetevents.SwingEvent;
import me.apex.hades.user.User;

@CheckInfo(name = "BadPackets", type = "C")
public class BadPacketsC extends Check {

    private int ticks;

    @Override
    public void onEvent(PacketEvent e, User user) {
        if (e instanceof SwingEvent){
            int ticks = this.ticks;
            this.ticks = 0;

            if (ticks < 1) {
                if (preVL++ > 2)
                    flag(user, "ticks = " + ticks);
            } else preVL = 0;
        }else if (e instanceof FlyingEvent){
            ticks++;
        }
    }
}