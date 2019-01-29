package net.prosavage.savageskyblock.NMS;

import net.prosavage.savageskyblock.SavageSkyBlock;
import net.prosavage.savageskyblock.Island;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class NMS_v1_13_R2 {

    public static int calculate(Object object, Island island) {
        int level = 0;
        CraftChunk chunk = (CraftChunk) object;
        for (final Map.Entry<BlockPosition, TileEntity> entry : chunk.getHandle().tileEntities.entrySet()) {
            if (island.isblockinisland(entry.getKey().getX(), entry.getKey().getZ())) {
                final TileEntity tileEntity = entry.getValue();
                if (tileEntity instanceof TileEntityMobSpawner) {
                    if (SavageSkyBlock.getSkyblock.getConfig().contains("IsTop.Spawners." + ((TileEntityMobSpawner) tileEntity).getSpawner().getMobName().toString().toUpperCase())) {
                        level += SavageSkyBlock.getSkyblock.getConfig().getInt("IsTop.Spawners." + ((TileEntityMobSpawner) tileEntity).getSpawner().getMobName().toString().toUpperCase());
                    }
                } else {
                    if (SavageSkyBlock.getSkyblock.getConfig().contains("IsTop.Blocks." + tileEntity.getBlock().getMaterial().toString().toUpperCase())) {
                        level += SavageSkyBlock.getSkyblock.getConfig().getInt("IsTop.Blocks." + tileEntity.getBlock().getMaterial().toString().toUpperCase());
                    }
                }
            }
        }
        return level;
    }

    public static void pasteSchematic(File f, Location loc) {
        try {
            FileInputStream fis = new FileInputStream(f);
            NBTTagCompound nbt = NBTCompressedStreamTools.a(fis);
            short width = nbt.getShort("Width");
            short height = nbt.getShort("Height");
            short length = nbt.getShort("Length");
            byte[] blocks = nbt.getByteArray("Blocks");
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
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendBorder(Player p, double x, double z, double radius, ColorType colorType) {
        EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
        WorldBorder worldBorder = new WorldBorder();
        worldBorder.setCenter(x, z);
        worldBorder.setSize(radius);
        worldBorder.setWarningDistance(0);
        worldBorder.world = ((WorldServer) entityPlayer.world);
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

    private static void setBorder(EntityPlayer entityPlayer, WorldBorder worldBorder) {
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS));
    }

    public static void sendTitle(Player p, String text, int in, int stay, int out, String type) {
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.valueOf(type), IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\":\"" + text + " \"}")), in, stay, out);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
    }
}


