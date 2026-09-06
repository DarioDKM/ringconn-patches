#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BUILD_DIR="$SCRIPT_DIR/build"
DIST_DIR="$SCRIPT_DIR/dist"
SMALI_DIR="$SCRIPT_DIR/src/smali"
JAVA_SRC_DIR="$SCRIPT_DIR/src/java"
DEX_DIR="$SCRIPT_DIR/dex"
PAYLOAD_DEX="$DEX_DIR/HealthDataProvider.dex"

ANDROID_JAR="/opt/homebrew/share/android-commandlinetools/platforms/android-34/android.jar"
D8="/opt/homebrew/share/android-commandlinetools/build-tools/35.0.0/d8"

mkdir -p "$BUILD_DIR"
mkdir -p "$DIST_DIR"
mkdir -p "$DEX_DIR"

echo "==> 1. Compiling Java sources (Provider, Activity, WebView Bridge, Sync Engine)..."
BIN_DIR="$BUILD_DIR/bin"
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

javac -cp "$ANDROID_JAR" -source 1.8 -target 1.8 -d "$BIN_DIR" \
    $(find "$JAVA_SRC_DIR" -name "*.java")

echo "==> 2. Converting class files to DEX with d8..."
rm -f "$PAYLOAD_DEX"
find "$BIN_DIR" -name "*.class" | xargs "$D8" --min-api 26 --output "$DEX_DIR"
mv "$DEX_DIR/classes.dex" "$PAYLOAD_DEX"

echo "==> 3. Compiling patch smali to classes.dex with apktool..."
TMP_APKTOOL_DIR="$BUILD_DIR/apktool_workspace"
rm -rf "$TMP_APKTOOL_DIR"
mkdir -p "$TMP_APKTOOL_DIR/smali"
cp -r "$SMALI_DIR"/* "$TMP_APKTOOL_DIR/smali/"

cat << 'YAML_EOF' > "$TMP_APKTOOL_DIR/apktool.yml"
version: 3.0.3
apkFileName: patch.apk
isFrameworkApk: false
usesFramework:
  ids:
  - 1
sdkInfo:
  minSdkVersion: '26'
  targetSdkVersion: '34'
YAML_EOF

apktool b "$TMP_APKTOOL_DIR" -o "$BUILD_DIR/patch.apk" >/dev/null

echo "==> 4. Assembling Morphe .mpp package..."
MPP_STAGING="$BUILD_DIR/mpp_staging"
rm -rf "$MPP_STAGING"
mkdir -p "$MPP_STAGING/META-INF"
mkdir -p "$MPP_STAGING/extensions"

unzip -p "$BUILD_DIR/patch.apk" classes.dex > "$MPP_STAGING/classes.dex"
cp "$PAYLOAD_DEX" "$MPP_STAGING/extensions/HealthDataProvider.dex"

cat << 'MF_EOF' > "$MPP_STAGING/META-INF/MANIFEST.MF"
Manifest-Version: 1.0
Name: RingConn Patches
Description: Morphe patch for RingConn to support Intervals Direct with Embedded UI and Dev Provider
Version: 1.1.0
Timestamp: 1788710000000
Source: https://github.com/DarioDKM/ringconn-patches
Author: DarioDKM
Website: https://github.com/DarioDKM/ringconn-patches
License: MIT
Patcher-Version: 1.10.0

MF_EOF

(cd "$MPP_STAGING" && zip -q -r "$DIST_DIR/patches-1.1.0.mpp" classes.dex extensions META-INF)
cp "$DIST_DIR/patches-1.1.0.mpp" "$DIST_DIR/patches-1.0.0.mpp"

echo "==> Done! Output bundle: $DIST_DIR/patches-1.1.0.mpp"
ls -lh "$DIST_DIR/patches-1.1.0.mpp"

