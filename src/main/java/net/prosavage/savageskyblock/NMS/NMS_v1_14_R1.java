package net.prosavage.savageskyblock.NMS;

import net.minecraft.server.v1_14_R1.*;
import net.prosavage.savageskyblock.Island;
import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_14_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;

public class NMS_v1_14_R1 {

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

    public static void sendBorder(Player p, double x, double z, double radius) {
        WorldBorder worldBorder = new WorldBorder();
        worldBorder.setCenter(x, z);
        worldBorder.setSize(radius * 2);
        worldBorder.setWarningDistance(0);
        EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS));
    }

    public static void sendTitle(Player p, String text, int in, int stay, int out, String type) {
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.valueOf(type), IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\":\"" + text + " \"}")), in, stay, out);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
    }
}


