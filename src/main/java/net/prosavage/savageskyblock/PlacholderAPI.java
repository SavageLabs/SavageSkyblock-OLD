package net.prosavage.savageskyblock;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlacholderAPI extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "SavageSkyblock";
    }

    @Override
    @SuppressWarnings("Deprecated")
    public String getPlugin() {
        return "SavageSkyblock";
    }

    @Override
    public String getAuthor() {
        return "ProSavage";
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
                case "leader":
                    return u.getIsland().getownername();
                case "online":
                    return u.getIsland().getOnline() + "";
                case "value":
                    return u.getIsland().getLevel() + "";
                case "crystals":
                    return u.getIsland().getCrystals() + "";
            }
        }
        return null;
    }
}