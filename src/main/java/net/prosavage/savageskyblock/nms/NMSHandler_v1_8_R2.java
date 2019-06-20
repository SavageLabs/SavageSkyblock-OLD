package net.prosavage.savageskyblock.nms;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_8_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R2.TileEntity;
import net.minecraft.server.v1_8_R2.TileEntityMobSpawner;
import net.minecraft.server.v1_8_R2.WorldBorder;
import net.prosavage.savageskyblock.Island;
import net.prosavage.savageskyblock.SavageSkyBlock;

public class NMSHandler_v1_8_R2 extends NMSHandler {

   @Override
   public int calculate(Chunk chunk, Island island) {
      int level = 0;

      final CraftChunk craftChunk = (CraftChunk) chunk;

      for (final Map.Entry<BlockPosition, TileEntity> entry : craftChunk.getHandle().tileEntities.entrySet()) {
         if (island.isblockinisland(entry.getKey().getX(), entry.getKey().getZ())) {
            final TileEntity tileEntity = entry.getValue();

            String key = null;

            if (tileEntity instanceof TileEntityMobSpawner) {
               key = "IsTop.Spawners." + ((TileEntityMobSpawner) tileEntity).getSpawner().getMobName().toUpperCase();
            } else {
               key = "IsTop.Blocks." + tileEntity.w().getName().toUpperCase();
            }

            if (SavageSkyBlock.getSkyblock.getConfig().contains(key)) level += SavageSkyBlock.getSkyblock.getConfig().getInt(key);

         }
      }
      return level;
   }

   @Override
   public void removeBlockSuperFast(int X, int Y, int Z, boolean applyPhysics) {
      net.minecraft.server.v1_8_R2.World w = ((org.bukkit.craftbukkit.v1_8_R2.CraftWorld) SavageSkyBlock.getSkyblock.getWorld()).getHandle();
      net.minecraft.server.v1_8_R2.Chunk chunk = w.getChunkAt(X >> 4, Z >> 4);
      net.minecraft.server.v1_8_R2.BlockPosition bp = new net.minecraft.server.v1_8_R2.BlockPosition(X, Y, Z);
      net.minecraft.server.v1_8_R2.IBlockData ibd = net.minecraft.server.v1_8_R2.Block.getByCombinedId(0);

      w.setTypeAndData(bp, ibd, applyPhysics ? 3 : 2);
      chunk.a(bp, ibd);
   }

   @Override
   public void sendBorder(Player p, double x, double z, double radius) {
      final WorldBorder worldBorder = new WorldBorder();
      worldBorder.setCenter(x, z);
      worldBorder.setSize(radius * 2);
      worldBorder.setWarningDistance(0);
      final EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
      entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE));
      entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER));
      entityPlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS));
   }

   @Override
   public void sendTitle(Player p, String text, int in, int stay, int out, String type) {
      final PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.valueOf(type),
            IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\":\"" + text + " \"}")), in, stay, out);
      ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
   }

}
