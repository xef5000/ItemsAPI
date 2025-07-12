package com.github.xef5000.api.util;

import org.bukkit.Bukkit;

public class ServerVersion implements Comparable<ServerVersion> {

    public static final ServerVersion CURRENT = new ServerVersion(Bukkit.getBukkitVersion());

    private final int major;
    private final int minor;
    private final int patch;

    public ServerVersion(String versionString) {
        // Example versionString: "1.20.5-R0.1-SNAPSHOT"
        String[] parts = versionString.split("-")[0].split("\\.");
        this.major = Integer.parseInt(parts[0]);
        this.minor = Integer.parseInt(parts[1]);
        this.patch = (parts.length > 2) ? Integer.parseInt(parts[2]) : 0;
    }

    /**
     * Checks if the current server version is greater than or equal to the specified version.
     *
     * @param major The major version (e.g., 1).
     * @param minor The minor version (e.g., 20).
     * @param patch The patch version (e.g., 5).
     * @return true if the current version is at or after the specified version.
     */
    public boolean isAtLeast(int major, int minor, int patch) {
        return compareTo(new ServerVersion(major, minor, patch)) >= 0;
    }

    @Override
    public int compareTo(ServerVersion other) {
        int majorCompare = Integer.compare(this.major, other.major);
        if (majorCompare != 0) return majorCompare;

        int minorCompare = Integer.compare(this.minor, other.minor);
        if (minorCompare != 0) return minorCompare;

        return Integer.compare(this.patch, other.patch);
    }

    // Private constructor for internal use
    private ServerVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
}
