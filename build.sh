#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BUILD_DIR="$SCRIPT_DIR/build"
DIST_DIR="$SCRIPT_DIR/dist"
SMALI_DIR="$SCRIPT_DIR/src/smali"
PAYLOAD_DEX="$SCRIPT_DIR/dex/HealthDataProvider.dex"

mkdir -p "$BUILD_DIR"
mkdir -p "$DIST_DIR"

echo "==> 1. Compiling smali to classes.dex with apktool..."
TMP_APKTOOL_DIR="$BUILD_DIR/apktool_workspace"
rm -rf "$TMP_APKTOOL_DIR"
mkdir -p "$TMP_APKTOOL_DIR/smali"
cp -r "$SMALI_DIR"/* "$TMP_APKTOOL_DIR/smali/"

cat << 'EOF' > "$TMP_APKTOOL_DIR/apktool.yml"
version: 3.0.3
apkFileName: patch.apk
isFrameworkApk: false
usesFramework:
  ids:
  - 1
sdkInfo:
  minSdkVersion: '26'
  targetSdkVersion: '34'
EOF

apktool b "$TMP_APKTOOL_DIR" -o "$BUILD_DIR/patch.apk" >/dev/null

echo "==> 2. Assembling Morphe .mpp package..."
MPP_STAGING="$BUILD_DIR/mpp_staging"
rm -rf "$MPP_STAGING"
mkdir -p "$MPP_STAGING/META-INF"
mkdir -p "$MPP_STAGING/extensions"

unzip -p "$BUILD_DIR/patch.apk" classes.dex > "$MPP_STAGING/classes.dex"
cp "$PAYLOAD_DEX" "$MPP_STAGING/extensions/HealthDataProvider.dex"

cat << 'EOF' > "$MPP_STAGING/META-INF/MANIFEST.MF"
Manifest-Version: 1.0
Name: RingConn Patches
Description: Morphe patch for RingConn to support Intervals Direct
Version: 1.0.0
Timestamp: 1788710000000
Source: https://github.com/DarioDKM/ringconn-patches
Author: DarioDKM
Website: https://github.com/DarioDKM/ringconn-patches
License: MIT
Patcher-Version: 1.10.0

EOF

(cd "$MPP_STAGING" && zip -q -r "$DIST_DIR/patches-1.0.0.mpp" classes.dex extensions META-INF)

echo "==> Done! Output bundle: $DIST_DIR/patches-1.0.0.mpp"
