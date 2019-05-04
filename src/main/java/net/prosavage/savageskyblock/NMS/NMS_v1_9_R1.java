package net.prosavage.savageskyblock.NMS;

import net.prosavage.savageskyblock.SavageSkyBlock;
import net.prosavage.savageskyblock.Island;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class NMS_v1_9_R1 {

    public static int calculate(Object object, Island island) {
        int level = 0;
        CraftChunk chunk = (CraftChunk) object;
        for (final Map.Entry<BlockPosition, TileEntity> entry : chunk.getHandle().tileEntities.entrySet()) {
            if (island.isblockinisland(entry.getKey().getX(), entry.getKey().getZ())) {
                final TileEntity tileEntity = entry.getValue();
                if (tileEntity instanceof TileEntityMobSpawner) {
                    if (SavageSkyBlock.getSkyblock.getConfig().contains("IsTop.Spawners." + ((TileEntityMobSpawner) tileEntity).getSpawner().getMobName().toUpperCase())) {
                        level += SavageSkyBlock.getSkyblock.getConfig().getInt("IsTop.Spawners." + ((TileEntityMobSpawner) tileEntity).getSpawner().getMobName().toUpperCase());
                    }
                } else {
                    if (SavageSkyBlock.getSkyblock.getConfig().contains("IsTop.Blocks." + tileEntity.getBlock().getName().toUpperCase())) {
                        level += SavageSkyBlock.getSkyblock.getConfig().getInt("IsTop.Blocks." + tileEntity.getBlock().getName().toUpperCase());
                    }
                }
            }
        }
        return level;
    }

    public static void setBlockSuperFast(int X, int Y, int Z, int blockId, byte data, boolean applyPhysics) {
        net.minecraft.server.v1_9_R1.World w = ((org.bukkit.craftbukkit.v1_9_R1.CraftWorld) SavageSkyBlock.getSkyblock.getWorld()).getHandle();
        net.minecraft.server.v1_9_R1.Chunk chunk = w.getChunkAt(X >> 4, Z >> 4);
        net.minecraft.server.v1_9_R1.BlockPosition bp = new net.minecraft.server.v1_9_R1.BlockPosition(X, Y, Z);
        int combined = blockId + (data << 12);
        net.minecraft.server.v1_9_R1.IBlockData ibd = net.minecraft.server.v1_9_R1.Block.getByCombinedId(combined);
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


