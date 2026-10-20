package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.*;

public class BootLaunchPolicyTest {
    @Test public void nextBootOpensFirstAvailableApp() {
        assertTrue(BootLaunchPolicy.shouldLaunch(true, true, 12, 11, true));
    }
    @Test public void homeAndProcessRestartsDoNotLoop() {
        assertFalse(BootLaunchPolicy.shouldLaunch(true, true, 12, 12, true));
    }
    @Test public void disabledNeverLaunches() {
        assertFalse(BootLaunchPolicy.shouldLaunch(false, true, 12, 11, true));
    }
    @Test public void nonDefaultHomeNeverLaunches() {
        assertFalse(BootLaunchPolicy.shouldLaunch(true, false, 12, 11, true));
    }
    @Test public void unavailableBootCountFailsClosed() {
        assertFalse(BootLaunchPolicy.shouldLaunch(true, true, -1, 11, true));
    }
    @Test public void noAppKeepsHomeAvailable() {
        assertFalse(BootLaunchPolicy.shouldLaunch(true, true, 12, 11, false));
    }
    @Test public void enablingNowWaitsForNextBoot() {
        assertFalse(BootLaunchPolicy.shouldLaunch(true, true, 12, 12, true));
        assertTrue(BootLaunchPolicy.shouldLaunch(true, true, 13, 12, true));
    }
}
