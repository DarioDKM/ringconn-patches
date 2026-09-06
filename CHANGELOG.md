# Changelog

## 1.1.0 (2026-09-06)

### Features
* Embedded Intervals Direct Dashboard directly inside RingConn.
* Draggable Floating Action Button (FAB) on the RingConn main interface for instant access.
* Standalone launcher activity for Intervals Direct.
* Native Dark Theme UI with sleep staging visualization, 30 days history, and manual sync controls.
* Direct upload engine to intervals.icu without requiring companion apps.
* Full retention of `HealthDataProvider` and `android:debuggable="true"` for external companion apps.

## 1.0.0 (2026-09-06)

### Features
* Initial release of RingConn Morphe Patch.
* Registers `com.gdjztech.ringconn.provider.HealthDataProvider` with authority `com.gdjztech.ringconn.debug.provider`.
* Enables `android:debuggable="true"` in `AndroidManifest.xml`.
* Injects Dalvik bytecode to query local RingConn sleep, resting HR, and temperature database tables for Intervals Direct.
