# RingConn Morphe Patch

Official Morphe patch bundle for the **RingConn** Android application (`com.gdjztech.ringconn`), enabling local health data synchronization for [Intervals Direct](https://github.com/DarioDKM/IntervalsDirect).

---

## What Does This Patch Do?

The official RingConn app stores high-resolution biometric readings (sleep staging, overnight resting heart rate, and skin temperature offset) in a local encrypted SQLite database (`ring_conn.db`). By default, third-party companion apps on Android cannot query this data without root access or ADB.

This patch modifies the RingConn APK using the [Morphe](https://morphe.software) framework:
1. **Manifest Registration:** Adds a read-only `ContentProvider` (`com.gdjztech.ringconn.provider.HealthDataProvider`) with authority `com.gdjztech.ringconn.debug.provider`.
2. **Debug Mode:** Enables `android:debuggable="true"` so the database can be queried securely by authorized local companion tools.
3. **Bytecode Injection:** Injects compiled Dalvik bytecode (`HealthDataProvider.dex`) that handles read-only queries against `SleepSyncModel`, `DailyModel`, and `TempOffsetModel`.

---

## How to Install (Using Morphe Manager on Android)

You do **not** need a computer, terminal, ADB, or root access. Everything runs directly on your Android phone.

### Step 1: Add the Patch Source to Morphe Manager

1. Open **Morphe Manager** on your Android device.
2. Tap **Sources** (in the bottom navigation bar).
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
3. Verify that the **RingConn Health Data Provider** patch is selected.
4. Tap **Patch**. Morphe will unpack, patch, and repack the APK.
5. Tap **Install** to install the patched RingConn app.

---

## Companion App

Once patched, install [Intervals Direct](https://github.com/DarioDKM/IntervalsDirect) to automatically sync your sleep and recovery metrics to [intervals.icu](https://intervals.icu).

---

## Building From Source

To build `patches-1.0.0.mpp` locally:

```bash
git clone https://github.com/DarioDKM/ringconn-patches.git
cd ringconn-patches
./build.sh
```

Output bundle will be located at `dist/patches-1.0.0.mpp`.

---

## License

Released under the [MIT License](LICENSE).
