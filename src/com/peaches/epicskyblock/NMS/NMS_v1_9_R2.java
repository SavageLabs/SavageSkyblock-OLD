package com.peaches.epicskyblock.NMS;


import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_9_R2.WorldBorder;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class NMS_v1_9_R2 {

    public static void sendBorder(Player p, double x, double z, double radius) {
        if (!p.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            WorldBorder worldBorder = new WorldBorder();
            worldBorder.setCenter(x, z);
            worldBorder.setSize(radius * 2);
            worldBorder.setWarningDistance(0);
            EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
            setBorder(entityPlayer, worldBorder);
        }
    }

    private static void setBorder(EntityPlayer entityPlayer, WorldBorder worldBorder) {
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS));
    }
}


