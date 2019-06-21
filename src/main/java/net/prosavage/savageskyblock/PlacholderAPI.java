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
        return SavageSkyBlock.getInstance().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        User u = User.getbyPlayer(player);
        switch (s) {
            case "leader":
                return u.getIsland() != null ? u.getIsland().getownername() : "N/A";
            case "online":
                return u.getIsland() != null ? u.getIsland().getOnline() + "" : "N/A";
            case "value":
                return  u.getIsland() != null ? u.getIsland().getLevel() + "" : "N/A";
            case "crystals":
                return  u.getIsland() != null ? u.getIsland().getCrystals() + "" : "N/A";
        }
        return null;
    }
}