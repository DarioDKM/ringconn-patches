# Changelog

## 1.2.0 (2026-09-06)

### Features & Architecture
* **Modular Multi-Patch System:** Split into two independent Morphe patches:
  * `RingConn Health Data Provider`: Injects ContentProvider `com.gdjztech.ringconn.debug.provider` and sets `android:debuggable="true"`.
  * `Embedded Intervals Direct Dashboard`: Injects the embedded dashboard UI, floating action button, and auto-sync engine (depends on Provider).
* **Calibrated Athletic Sleep Score:**
  * Replaces inflated commercial scoring with a non-linear endurance recovery model based on total sleep duration ($T^{2.2}$), slow-wave deep sleep ($D^{1.5}$), REM ($R^{1.5}$), and wakefulness penalty (WASO).
  * Example: 6h 13m of sleep yields a realistic 60 score instead of an inflated 84.
* **Configurable Sleep Score Source:** Added a selector in Settings to switch between `Athletic (Recommended)` and `RingConn Official`.
* **Full English UI & Visual Polish:**
  * Complete English translation across all screens, cards, modals, and notifications.
  * OLED dark styling (`#070A11`), refined cards, stage percentage bars, and smooth closing animations.
  * Conditional floating action button that only appears if the dashboard patch is installed.

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
