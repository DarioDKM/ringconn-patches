package com.gdjztech.ringconn.provider;

import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import com.gdjztech.ringconn.ui.HookHelper;
import java.io.File;

public class HealthDataProvider extends ContentProvider {
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        try {
            Context context = getContext();
            if (context != null) {
                Context appCtx = context.getApplicationContext();
                if (appCtx instanceof Application) {
                    ((Application) appCtx).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                        @Override
                        public void onActivityResumed(Activity activity) {
                            if (activity != null && !activity.getClass().getName().contains("IntervalsActivity")) {
                                HookHelper.attachFloatingButton(activity);
                            }
                        }

                        @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
                        @Override public void onActivityStarted(Activity activity) {}
                        @Override public void onActivityPaused(Activity activity) {}
                        @Override public void onActivityStopped(Activity activity) {}
                        @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
                        @Override public void onActivityDestroyed(Activity activity) {}
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private synchronized SQLiteDatabase getDb() {
        if (db != null && db.isOpen()) {
            return db;
        }
        Context ctx = getContext();
        if (ctx == null) return null;
        try {
            File dbFile = ctx.getDatabasePath("ring_conn.db");
            if (dbFile != null && dbFile.exists()) {
                db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = getDb();
        if (database == null) return null;
        String path = uri.getPath();
        if (path == null) path = "";
        String table;
        if (path.contains("daily")) {
            table = "DailyModel";
        } else if (path.contains("temp")) {
            table = "TempOffsetModel";
        } else {
            table = "SleepSyncModel";
        }
        try {
            return database.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.com.gdjztech.ringconn.provider";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
