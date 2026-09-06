package com.gdjztech.ringconn.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IntervalsSyncEngine {

    public static final String PREFS_NAME = "ringconn_intervals_sync_prefs";
    public static final String KEY_ATHLETE_ID = "athlete_id";
    public static final String KEY_API_KEY = "api_key";
    public static final String KEY_AUTO_SYNC = "auto_sync";
    public static final String KEY_LAST_SYNC_TIME = "last_sync_time";
    public static final String KEY_LAST_SYNC_STATUS = "last_sync_status";
    public static final String KEY_LOGS = "sync_logs";

    public static SQLiteDatabase getDatabase(Context context) {
        try {
            File dbFile = context.getDatabasePath("ring_conn.db");
            if (dbFile != null && dbFile.exists()) {
                return SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static String getSettingsJson(Context context) {
        SharedPreferences prefs = getPrefs(context);
        JSONObject obj = new JSONObject();
        try {
            obj.put("athleteId", prefs.getString(KEY_ATHLETE_ID, ""));
            obj.put("apiKey", prefs.getString(KEY_API_KEY, ""));
            obj.put("autoSync", prefs.getBoolean(KEY_AUTO_SYNC, true));
            obj.put("lastSyncTime", prefs.getLong(KEY_LAST_SYNC_TIME, 0));
            obj.put("lastSyncStatus", prefs.getString(KEY_LAST_SYNC_STATUS, "Never synced"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static void saveSettings(Context context, String athleteId, String apiKey, boolean autoSync) {
        SharedPreferences.Editor ed = getPrefs(context).edit();
        ed.putString(KEY_ATHLETE_ID, athleteId != null ? athleteId.trim() : "");
        ed.putString(KEY_API_KEY, apiKey != null ? apiKey.trim() : "");
        ed.putBoolean(KEY_AUTO_SYNC, autoSync);
        ed.apply();
    }

    public static void appendLog(Context context, String message) {
        SharedPreferences prefs = getPrefs(context);
        String oldLogs = prefs.getString(KEY_LOGS, "[]");
        try {
            JSONArray arr = new JSONArray(oldLogs);
            JSONObject entry = new JSONObject();
            String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            entry.put("time", timeStr);
            entry.put("message", message);
            
            JSONArray newArr = new JSONArray();
            newArr.put(entry);
            for (int i = 0; i < Math.min(arr.length(), 49); i++) {
                newArr.put(arr.get(i));
            }
            prefs.edit().putString(KEY_LOGS, newArr.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLogsJson(Context context) {
        return getPrefs(context).getString(KEY_LOGS, "[]");
    }

    public static String getSleepHistoryJson(Context context, int limit) {
        JSONArray array = new JSONArray();
        SQLiteDatabase db = getDatabase(context);
        if (db == null) {
            return array.toString();
        }

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "SleepSyncModel",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "dateSleep DESC",
                    String.valueOf(limit)
            );

            if (cursor != null) {
                int colDate = cursor.getColumnIndex("dateSleep");
                int colRestingHr = cursor.getColumnIndex("restingHr");
                int colHrAvg = cursor.getColumnIndex("hrAvg");
                int colHrvAvg = cursor.getColumnIndex("hrvAvg");
                int colRrAvg = cursor.getColumnIndex("rrAvg");
                int colTempOffset = cursor.getColumnIndex("tempOffset");
                int colTempBench = cursor.getColumnIndex("tempBenchmark");
                int colDeep = cursor.getColumnIndex("deepDuration");
                int colRem = cursor.getColumnIndex("remDuration");
                int colLight = cursor.getColumnIndex("lightDuration");
                int colAwake = cursor.getColumnIndex("awakeDuration");
                int colSleepDur = cursor.getColumnIndex("sleepDuration");
                int colScore = cursor.getColumnIndex("sleepScore");
                int colSpo2 = cursor.getColumnIndex("spo2Avg");

                while (cursor.moveToNext()) {
                    JSONObject row = new JSONObject();
                    String date = colDate >= 0 ? cursor.getString(colDate) : "";
                    row.put("date", date);

                    if (colRestingHr >= 0 && !cursor.isNull(colRestingHr)) {
                        row.put("restingHr", cursor.getInt(colRestingHr));
                    }
                    if (colHrAvg >= 0 && !cursor.isNull(colHrAvg)) {
                        row.put("hrAvg", (int) cursor.getFloat(colHrAvg));
                    }
                    if (colHrvAvg >= 0 && !cursor.isNull(colHrvAvg)) {
                        row.put("hrv", Math.round(cursor.getFloat(colHrvAvg) * 10.0f) / 10.0);
                    }
                    if (colRrAvg >= 0 && !cursor.isNull(colRrAvg)) {
                        float rawRr = cursor.getFloat(colRrAvg);
                        if (rawRr > 0) {
                            row.put("respiration", Math.round((rawRr / 8.0f) * 10.0f) / 10.0);
                        }
                    }
                    if (colTempOffset >= 0 && !cursor.isNull(colTempOffset)) {
                        row.put("tempOffset", Math.round(cursor.getFloat(colTempOffset) * 100.0f) / 100.0);
                    }
                    if (colDeep >= 0 && !cursor.isNull(colDeep)) {
                        row.put("deepMinutes", (int) cursor.getFloat(colDeep));
                    }
                    if (colRem >= 0 && !cursor.isNull(colRem)) {
                        row.put("remMinutes", (int) cursor.getFloat(colRem));
                    }
                    if (colLight >= 0 && !cursor.isNull(colLight)) {
                        row.put("lightMinutes", (int) cursor.getFloat(colLight));
                    }
                    if (colAwake >= 0 && !cursor.isNull(colAwake)) {
                        row.put("awakeMinutes", (int) cursor.getFloat(colAwake));
                    }
                    if (colSleepDur >= 0 && !cursor.isNull(colSleepDur)) {
                        row.put("sleepDurationMinutes", (int) cursor.getFloat(colSleepDur));
                    }
                    if (colScore >= 0 && !cursor.isNull(colScore)) {
                        row.put("score", cursor.getInt(colScore));
                    }
                    if (colSpo2 >= 0 && !cursor.isNull(colSpo2)) {
                        row.put("spo2", Math.round(cursor.getFloat(colSpo2) * 10.0f) / 10.0);
                    }

                    array.put(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return array.toString();
    }

    public static JSONObject syncDate(Context context, String targetDate) {
        JSONObject result = new JSONObject();
        SharedPreferences prefs = getPrefs(context);
        String athleteId = prefs.getString(KEY_ATHLETE_ID, "").trim();
        String apiKey = prefs.getString(KEY_API_KEY, "").trim();

        if (athleteId.isEmpty() || apiKey.isEmpty()) {
            try {
                result.put("success", false);
                result.put("message", "Athlete ID and API Key are required in Settings.");
            } catch (Exception ignored) {}
            return result;
        }

        SQLiteDatabase db = getDatabase(context);
        if (db == null) {
            try {
                result.put("success", false);
                result.put("message", "Could not open RingConn local database.");
            } catch (Exception ignored) {}
            return result;
        }

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "SleepSyncModel",
                    null,
                    "dateSleep = ?",
                    new String[]{targetDate},
                    null,
                    null,
                    null
            );

            if (cursor == null || !cursor.moveToFirst()) {
                result.put("success", false);
                result.put("message", "No sleep record found for date: " + targetDate);
                return result;
            }

            JSONObject payload = new JSONObject();
            payload.put("id", targetDate);
            payload.put("steps", -1); // prevent phone pedometer overwrite

            int colRestingHr = cursor.getColumnIndex("restingHr");
            int colHrAvg = cursor.getColumnIndex("hrAvg");
            int colHrvAvg = cursor.getColumnIndex("hrvAvg");
            int colRrAvg = cursor.getColumnIndex("rrAvg");
            int colTempOffset = cursor.getColumnIndex("tempOffset");
            int colDeep = cursor.getColumnIndex("deepDuration");
            int colRem = cursor.getColumnIndex("remDuration");
            int colLight = cursor.getColumnIndex("lightDuration");
            int colSleepDur = cursor.getColumnIndex("sleepDuration");
            int colScore = cursor.getColumnIndex("sleepScore");
            int colSpo2 = cursor.getColumnIndex("spo2Avg");

            if (colSleepDur >= 0 && !cursor.isNull(colSleepDur)) {
                int totalMins = (int) cursor.getFloat(colSleepDur);
                if (totalMins > 0) payload.put("sleepSecs", totalMins * 60);
            }
            if (colScore >= 0 && !cursor.isNull(colScore)) {
                int sc = cursor.getInt(colScore);
                if (sc > 0) payload.put("sleepScore", sc);
            }
            if (colRestingHr >= 0 && !cursor.isNull(colRestingHr)) {
                int rhr = cursor.getInt(colRestingHr);
                if (rhr > 0) payload.put("restingHR", rhr);
            }
            if (colHrAvg >= 0 && !cursor.isNull(colHrAvg)) {
                int ahr = (int) cursor.getFloat(colHrAvg);
                if (ahr > 0) payload.put("avgSleepingHR", ahr);
            }
            if (colHrvAvg >= 0 && !cursor.isNull(colHrvAvg)) {
                float hrv = cursor.getFloat(colHrvAvg);
                if (hrv > 0) payload.put("hrv", Math.round(hrv * 10.0) / 10.0);
            }
            if (colRrAvg >= 0 && !cursor.isNull(colRrAvg)) {
                float rawRr = cursor.getFloat(colRrAvg);
                if (rawRr > 0) {
                    payload.put("respiration", Math.round((rawRr / 8.0f) * 10.0f) / 10.0);
                }
            }
            if (colTempOffset >= 0 && !cursor.isNull(colTempOffset)) {
                payload.put("skinTemp", Math.round(cursor.getFloat(colTempOffset) * 100.0) / 100.0);
            }
            if (colDeep >= 0 && !cursor.isNull(colDeep)) {
                int d = (int) cursor.getFloat(colDeep);
                if (d > 0) payload.put("DeepSleep", d);
            }
            if (colRem >= 0 && !cursor.isNull(colRem)) {
                int r = (int) cursor.getFloat(colRem);
                if (r > 0) payload.put("RemSleep", r);
            }
            if (colLight >= 0 && !cursor.isNull(colLight)) {
                int l = (int) cursor.getFloat(colLight);
                if (l > 0) payload.put("LightSleep", l);
            }
            if (colSpo2 >= 0 && !cursor.isNull(colSpo2)) {
                float sp = cursor.getFloat(colSpo2);
                if (sp > 0) payload.put("spO2", Math.round(sp * 10.0) / 10.0);
            }

            // HTTP PUT to intervals.icu
            String endpoint = "https://intervals.icu/api/v1/athlete/" + athleteId + "/wellness/" + targetDate;
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            String auth = "API_KEY:" + apiKey;
            String basicAuth = "Basic " + Base64.encodeToString(auth.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
            conn.setRequestProperty("Authorization", basicAuth);

            byte[] jsonBytes = payload.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBytes);
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
            StringBuilder resp = new StringBuilder();
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resp.append(line);
                    }
                }
            }

            boolean isSuccess = (responseCode >= 200 && responseCode < 300);
            result.put("success", isSuccess);
            result.put("statusCode", responseCode);
            result.put("message", "HTTP " + responseCode + (isSuccess ? " OK" : ": " + resp.toString()));
            result.put("date", targetDate);

            String statusSummary = isSuccess ? "Synced " + targetDate + " (HTTP " + responseCode + ")" : "Failed " + targetDate + " (" + responseCode + ")";
            prefs.edit()
                    .putLong(KEY_LAST_SYNC_TIME, System.currentTimeMillis())
                    .putString(KEY_LAST_SYNC_STATUS, statusSummary)
                    .apply();

            appendLog(context, statusSummary);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                result.put("success", false);
                result.put("message", "Exception: " + e.getMessage());
                appendLog(context, "Error syncing " + targetDate + ": " + e.getMessage());
            } catch (Exception ignored) {}
        } finally {
            if (cursor != null) cursor.close();
        }

        return result;
    }
}
