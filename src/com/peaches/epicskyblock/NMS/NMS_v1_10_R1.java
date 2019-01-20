package com.peaches.epicskyblock.NMS;

import com.peaches.epicskyblock.EpicSkyBlock;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


public class NMS_v1_10_R1 {

    public static List<Location> pasteSchematic(File f, Location loc) {
        List<Location> locations = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(f);
            NBTTagCompound nbt = NBTCompressedStreamTools.a(fis);
            short width = nbt.getShort("Width");
            short height = nbt.getShort("Height");
            short length = nbt.getShort("Length");
            byte[] blocks = nbt.getByteArray("Blocks");
            byte[] data = nbt.getByteArray("Data");
            fis.close();
            //paste
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    for (int z = 0; z < length; ++z) {
                        int index = y * width * length + z * width + x;
                        final Location l = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ());
                        int b = blocks[index] & 0xFF;//make the block unsigned, so that blocks with an id over 127, like quartz and emerald, can be pasted
                        final Block block = l.getBlock();
                        Material m = Material.getMaterial(b);
                        block.setType(m);
                        //you can check what type the block is here, like if(m.equals(Material.BEACON)) to check if it's a beacon
                        locations.add(l);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locations;
    }

    public static void setBlockSuperFast(int X, int Y, int Z, int blockId, byte data, boolean applyPhysics) {
        net.minecraft.server.v1_10_R1.World w = ((org.bukkit.craftbukkit.v1_10_R1.CraftWorld) EpicSkyBlock.getSkyblock.getWorld()).getHandle();
        net.minecraft.server.v1_10_R1.Chunk chunk = w.getChunkAt(X >> 4, Z >> 4);
        net.minecraft.server.v1_10_R1.BlockPosition bp = new net.minecraft.server.v1_10_R1.BlockPosition(X, Y, Z);
        int combined = blockId + (data << 12);
        net.minecraft.server.v1_10_R1.IBlockData ibd = net.minecraft.server.v1_10_R1.Block.getByCombinedId(combined);
        if (applyPhysics) {
            w.setTypeAndData(bp, ibd, 3);
        } else {
            w.setTypeAndData(bp, ibd, 2);
        }
        chunk.a(bp, ibd);
    }

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


