package net.prosavage.savageskyblock;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlacholderAPI extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "savageftop";
    }

    @Override
    @SuppressWarnings("Deprecated")
    public String getPlugin() {
        return "SavageSkyblock";
    }

    @Override
    public String getAuthor() {
        return "Peaches_MLG";
    }

    @Override
    public String getVersion() {
        return SavageSkyBlock.getSkyblock.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        User u = User.getbyPlayer(player);
        if (u.getIsland() != null) {
            switch (s) {
                case "{SavageSkyblock_Leader}":
                    return u.getIsland().getownername();
                case "{SavageSkyblock_Online}":
                    return u.getIsland().getonline() + "";
                case "{SavageSkyblock_Value}":
                    return u.getIsland().getLevel() + "";
                case "{SavageSkyblock_Crystals}":
                    return u.getIsland().getCrystals() + "";
            }
        }
        return null;
    }
}