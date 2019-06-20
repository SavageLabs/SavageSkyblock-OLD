package net.prosavage.savageskyblock.nms;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import net.prosavage.savageskyblock.Island;

public abstract class NMSHandler {

   public abstract int calculate(Chunk chunk, Island island);

   public abstract void removeBlockSuperFast(int X, int Y, int Z, boolean applyPhysics);

   public abstract void sendBorder(Player p, double x, double z, double radius);
   
   public abstract void sendTitle(Player p, String text, int in, int stay, int out, String type);
      
   
   
}
