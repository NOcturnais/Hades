package me.purplex.packetevents.event.impl;

import org.bukkit.entity.Player;

import me.purplex.packetevents.event.PacketEvent;

public class PlayerInjectEvent extends PacketEvent {
    private final Player player;
    public PlayerInjectEvent(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
