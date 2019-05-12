package net.prosavage.savageskyblock.NMS;

import org.bukkit.Bukkit;

public enum Version {

    TOO_OLD(-1),
    v1_7_R1(171), v1_7_R2(172), v1_7_R3(173), v1_7_R4(174),
    v1_8_R1(181), v1_8_R2(182), v1_8_R3(183),
    v1_9_R1(191), v1_9_R2(192),
    v1_10_R1(1101),
    v1_11_R1(1111),
    v1_12_R1(1121),v1_12_R2(1122),
    v1_13_R1(1131), v1_13_R2(1132),
    v1_14_R1(1141),
    TOO_NEW(-2);

    private static Version currentVersion;
    private static Version latest;
    private final Integer versionInteger;

    Version(int versionInteger) {
        this.versionInteger = versionInteger;
    }


    public static Version getVersion() {
        if (currentVersion == null) {
            String ver = Bukkit.getServer().getClass().getPackage().getName();
            int v = Integer.parseInt(ver.substring(ver.lastIndexOf('.') + 1).replaceAll("_", "").replaceAll("R", "").replaceAll("v", ""));
            for (Version version : values()) {
                if (version.getVersionInteger() == v) {
                    currentVersion = version;
                    break;
                }
            }
            if (v > Version.getLatestVersion().getVersionInteger()) {
                currentVersion = Version.getLatestVersion();
            }
            if (currentVersion == null) {
                currentVersion = Version.TOO_NEW;
            }
        }
        return currentVersion;
    }

    private static Version getLatestVersion() {
        if (latest == null) {
            Version v = Version.TOO_OLD;
            for (Version version : values()) {
                if (version.comparedTo(v) == 1) {
                    v = version;
                }
            }
            return v;
        } else {
            return latest;
        }
    }

    public Integer getVersionInteger() {
        return this.versionInteger;
    }

    public Integer comparedTo(Version version) {
        int resault = -1;
        int current = this.getVersionInteger();
        int check = version.getVersionInteger();
        if (current > check || check == -2) {// check is newer then current
            resault = 1;
        } else if (current == check) {// check is the same as current
            resault = 0;
        } else if (current < check || check == -1) {// check is older then current
            resault = -1;
        }
        return resault;
    }

}

