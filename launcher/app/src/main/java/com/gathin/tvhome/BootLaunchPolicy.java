package com.gathin.tvhome;

/** A home visit is an automatic-launch opportunity only once per system boot. */
final class BootLaunchPolicy {
    static boolean shouldLaunch(boolean enabled, boolean defaultHome, int bootCount,
                                int handledBoot, boolean appAvailable) {
        return enabled && defaultHome && appAvailable && bootCount >= 0 && bootCount != handledBoot;
    }
}
