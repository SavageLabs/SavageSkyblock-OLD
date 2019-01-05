package com.peaches.epicskyblock.NMS;


import net.minecraft.server.v1_11_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class NMS_v1_11_R1 {

    public static void sendBorder(Player p, double x, double z, double radius, ColorType colorType) {
        if (!p.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            WorldBorder worldBorder = new WorldBorder();
            worldBorder.setCenter(x, z);
            worldBorder.setSize(radius * 2);
            worldBorder.setWarningDistance(0);
            EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
            if (colorType != null) {
                if (colorType.equals(ColorType.BLUE)) {
                    setBorder(entityPlayer, worldBorder);
                } else if (colorType.equals(ColorType.RED)) {
                    setBorder(entityPlayer, worldBorder);
                    worldBorder.transitionSizeBetween(radius, radius - 1.0D, 20000000L);
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE));
                } else if (colorType.equals(ColorType.GREEN)) {
                    setBorder(entityPlayer, worldBorder);
                    worldBorder.transitionSizeBetween(radius - 0.2D, radius - 0.1D + 0.1D, 20000000L);
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE));
                }
            }
        }
    }

    public static void sendTitle(Player p, String text, int in, int stay, int out, String type) {
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.valueOf(type), IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\":\"" + text + " \"}")), in, stay, out);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
    }

    private static void setBorder(EntityPlayer entityPlayer, WorldBorder worldBorder) {
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS));
    }
}


