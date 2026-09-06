# RingConn Morphe Patch

Official Morphe patch bundle for the **RingConn** Android application (`com.gdjztech.ringconn`), featuring an embedded **Intervals Direct** dashboard and local sync engine for [intervals.icu](https://intervals.icu), while preserving the debug provider for companion tools.

---

## What Does This Patch Do?

The official RingConn app stores biometric readings (sleep staging, overnight resting heart rate, and skin temperature offset) in a local encrypted SQLite database (`ring_conn.db`).

This Morphe patch enriches RingConn with two modes of operation:

1. **Embedded Intervals Direct UI & Direct Sync:**
   * Injects a floating action button into the RingConn main screen and adds a standalone launcher shortcut.
   * Provides an offline hardware-accelerated dark dashboard showing sleep score, total sleep, staging breakdown (Deep, Light, REM, Awake), resting heart rate, HRV, and skin temperature offset.
   * Direct synchronization to intervals.icu via API key and athlete ID without needing external companion apps.
   * 30-day sleep history with manual sync buttons.

2. **Developer & Companion App Support:**
   * Registers `com.gdjztech.ringconn.provider.HealthDataProvider` with authority `com.gdjztech.ringconn.debug.provider`.
   * Enables `android:debuggable="true"` so external companion tools such as [Intervals Direct](https://github.com/DarioDKM/IntervalsDirect) can query data locally via ContentResolver.

---

## How to Install (Using Morphe Manager on Android)

You do not need a computer, terminal, ADB, or root access. Everything runs directly on your Android phone.

### Step 1: Add the Patch Source to Morphe Manager

1. Open **Morphe Manager** on your Android device.
2. Tap **Sources** in the bottom navigation bar.
3. Tap **+** (or **Add**) in the top right corner.
4. Select the **Remote** tab and enter:
   ```text
   github.com/DarioDKM/ringconn-patches
   ```
5. Tap **Add**. Morphe Manager will fetch the patch definitions and bundle.

> [!TIP]
> **1-Click Deep Link:** On your Android phone, tap [Add to Morphe](https://morphe.software/add-source?github=DarioDKM/ringconn-patches) to automatically open Morphe Manager and register the source.

### Step 2: Patch RingConn

1. In Morphe Manager, switch to the **Dashboard / Patcher** tab.
2. Tap **Select an application** and choose **RingConn** (installed or from an APK file).
3. Verify that the **RingConn Health Data Provider & Embedded Sync** patch is selected.
4. Tap **Patch**. Morphe will unpack, patch, and repack the APK.
5. Tap **Install** to install the patched RingConn app.

---

## Building From Source

To build `patches-1.1.0.mpp` locally:

```bash
git clone https://github.com/DarioDKM/ringconn-patches.git
cd ringconn-patches
./build.sh
```

Output bundle will be located at `dist/patches-1.1.0.mpp`.

---

## License

Released under the [MIT License](LICENSE).

