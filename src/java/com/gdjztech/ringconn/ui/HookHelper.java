package com.gdjztech.ringconn.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gdjztech.ringconn.engine.IntervalsSyncEngine;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HookHelper {

    public static final String TAG_CARD = "CYCLING_COACH_DISCOVER_CARD";
    public static final String TAG_OLD_FAB = "INTERVALS_SYNC_FAB";

    public static void attachFloatingButton(final Activity activity) {
        if (activity == null) return;
        try {
            Intent testIntent = new Intent(activity, IntervalsActivity.class);
            if (activity.getPackageManager().resolveActivity(testIntent, 0) == null) {
                return;
            }
        } catch (Exception ignored) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    View decorView = activity.getWindow().getDecorView();
                    if (!(decorView instanceof ViewGroup)) return;
                    ViewGroup root = (ViewGroup) decorView;

                    // 1. Remove legacy floating FAB completely
                    View oldFab = root.findViewWithTag(TAG_OLD_FAB);
                    if (oldFab != null) {
                        root.removeView(oldFab);
                    }

                    // 2. Check if card already attached
                    if (root.findViewWithTag(TAG_CARD) != null) {
                        return;
                    }

                    // 3. Build native Discover Coach Card
                    final LinearLayout card = buildCoachCard(activity);
                    card.setTag(TAG_CARD);
                    card.setVisibility(View.GONE); // Default hidden until Discover tab active

                    int marginPx = dpToPx(activity, 14);
                    int bottomMarginPx = dpToPx(activity, 68); // positioned directly above bottom nav bar
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                    params.setMargins(marginPx, 0, marginPx, bottomMarginPx);

                    root.addView(card, params);

                    // 4. Hook Window Callback for instantaneous tab switching on bottom nav touch
                    Window window = activity.getWindow();
                    Window.Callback currentCallback = window.getCallback();
                    if (!(currentCallback instanceof WindowCallbackWrapper)) {
                        window.setCallback(new WindowCallbackWrapper(currentCallback, activity, card));
                    }

                    // 5. Periodic Accessibility Check to confirm Discover state
                    final Handler handler = new Handler(Looper.getMainLooper());
                    Runnable checkRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (activity.isFinishing() || activity.isDestroyed()) return;
                            try {
                                View decor = activity.getWindow().getDecorView();
                                AccessibilityNodeInfo rootNode = decor.createAccessibilityNodeInfo();
                                if (rootNode != null) {
                                    boolean hasSelectExercise = findNodeWithText(rootNode, "Select Exercise");
                                    rootNode.recycle();
                                    if (hasSelectExercise && card.getVisibility() != View.VISIBLE) {
                                        card.setVisibility(View.VISIBLE);
                                        updateCard(card, activity);
                                    } else if (!hasSelectExercise && card.getVisibility() == View.VISIBLE) {
                                        AccessibilityNodeInfo rootNode2 = decor.createAccessibilityNodeInfo();
                                        if (rootNode2 != null) {
                                            boolean isOtherTab = findNodeWithText(rootNode2, "Activity") ||
                                                    findNodeWithText(rootNode2, "Vital Signs") ||
                                                    findNodeWithText(rootNode2, "Stress") ||
                                                    findNodeWithText(rootNode2, "Insights");
                                            rootNode2.recycle();
                                            if (isOtherTab) {
                                                card.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                            } catch (Exception ignored) {}
                            handler.postDelayed(this, 400);
                        }
                    };
                    handler.postDelayed(checkRunnable, 400);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static LinearLayout buildCoachCard(final Activity activity) {
        LinearLayout card = new LinearLayout(activity);
        card.setOrientation(LinearLayout.VERTICAL);

        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setColor(0xF40D1527); // Dark slate glassmorphism
        cardBg.setCornerRadius(dpToPx(activity, 18));
        cardBg.setStroke(dpToPx(activity, 1.5f), 0x4038BDF8); // subtle cyan glow
        card.setBackground(cardBg);
        card.setElevation(dpToPx(activity, 16));
        card.setPadding(dpToPx(activity, 14), dpToPx(activity, 12), dpToPx(activity, 14), dpToPx(activity, 12));

        // ROW 1: Header (Icon + Title/FTP + Readiness Pill)
        LinearLayout headerRow = new LinearLayout(activity);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView iconBadge = new TextView(activity);
        iconBadge.setText("⚡");
        iconBadge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        iconBadge.setTextColor(Color.WHITE);
        iconBadge.setGravity(Gravity.CENTER);
        int iconSize = dpToPx(activity, 30);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(iconSize, iconSize);
        GradientDrawable iconBg = new GradientDrawable();
        iconBg.setShape(GradientDrawable.OVAL);
        iconBg.setColors(new int[]{0xFF0284C7, 0xFF38BDF8});
        iconBg.setOrientation(GradientDrawable.Orientation.TL_BR);
        iconBadge.setBackground(iconBg);
        headerRow.addView(iconBadge, iconParams);

        LinearLayout titleCol = new LinearLayout(activity);
        titleCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        titleParams.setMargins(dpToPx(activity, 10), 0, dpToPx(activity, 8), 0);

        TextView tvTitle = new TextView(activity);
        tvTitle.setText("CyclingCoach Hub");
        tvTitle.setTextColor(0xFFF8FAFC);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        titleCol.addView(tvTitle);

        TextView tvSub = new TextView(activity);
        tvSub.setText("Dario • FTP 301 W (4,56 W/kg)");
        tvSub.setTextColor(0xFF94A3B8);
        tvSub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        titleCol.addView(tvSub);

        headerRow.addView(titleCol, titleParams);

        TextView tvBadge = new TextView(activity);
        tvBadge.setTag("TAG_COACH_BADGE");
        tvBadge.setText("85% Optimal");
        tvBadge.setTextColor(0xFF10B981);
        tvBadge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        tvBadge.setTypeface(Typeface.DEFAULT_BOLD);
        tvBadge.setPadding(dpToPx(activity, 8), dpToPx(activity, 4), dpToPx(activity, 8), dpToPx(activity, 4));
        GradientDrawable pill = new GradientDrawable();
        pill.setColor(0x2610B981);
        pill.setCornerRadius(dpToPx(activity, 8));
        tvBadge.setBackground(pill);
        headerRow.addView(tvBadge);

        card.addView(headerRow);

        // ROW 2: Daily Corridor Banner
        LinearLayout banner = new LinearLayout(activity);
        banner.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams bannerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bannerParams.setMargins(0, dpToPx(activity, 10), 0, 0);
        banner.setLayoutParams(bannerParams);

        GradientDrawable bannerBg = new GradientDrawable();
        bannerBg.setColor(0x1838BDF8);
        bannerBg.setCornerRadius(dpToPx(activity, 10));
        bannerBg.setStroke(dpToPx(activity, 1.0f), 0x3338BDF8);
        banner.setBackground(bannerBg);
        banner.setPadding(dpToPx(activity, 10), dpToPx(activity, 8), dpToPx(activity, 10), dpToPx(activity, 8));

        TextView tvCorridor = new TextView(activity);
        tvCorridor.setTag("TAG_COACH_CORRIDOR");
        tvCorridor.setText("🎯 Tages-Korridor: Z4 Schwelle (270 bis 316 W)");
        tvCorridor.setTextColor(0xFF38BDF8);
        tvCorridor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tvCorridor.setTypeface(Typeface.DEFAULT_BOLD);
        banner.addView(tvCorridor);

        TextView tvClearance = new TextView(activity);
        tvClearance.setTag("TAG_COACH_CLEARANCE");
        tvClearance.setText("Freigabe: 100% Go • Volle Frische für Schwellenintervalle");
        tvClearance.setTextColor(0xFFE2E8F0);
        tvClearance.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        banner.addView(tvClearance);

        card.addView(banner);

        // ROW 3: Metric Stat Boxes (TSB / Periodisierung / OSA)
        LinearLayout statsRow = new LinearLayout(activity);
        statsRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams statsParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        statsParams.setMargins(0, dpToPx(activity, 8), 0, 0);
        statsRow.setLayoutParams(statsParams);

        statsRow.addView(createStatBox(activity, "TAG_COACH_TSB", "TSB: +2.4"));
        statsRow.addView(createStatBox(activity, "TAG_COACH_PHASE", "Phase: W2/4"));
        statsRow.addView(createStatBox(activity, "TAG_COACH_OSA", "OSA: Normal"));

        card.addView(statsRow);

        // ROW 4: Action Buttons (Sofort-Sync + Coach Dashboard)
        LinearLayout btnRow = new LinearLayout(activity);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams btnRowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        btnRowParams.setMargins(0, dpToPx(activity, 10), 0, 0);
        btnRow.setLayoutParams(btnRowParams);

        TextView btnSync = new TextView(activity);
        btnSync.setText("⚡ Sofort-Sync");
        btnSync.setTextColor(Color.WHITE);
        btnSync.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        btnSync.setTypeface(Typeface.DEFAULT_BOLD);
        btnSync.setGravity(Gravity.CENTER);
        btnSync.setPadding(0, dpToPx(activity, 10), 0, dpToPx(activity, 10));
        GradientDrawable syncBg = new GradientDrawable();
        syncBg.setCornerRadius(dpToPx(activity, 10));
        syncBg.setColors(new int[]{0xFF0284C7, 0xFF38BDF8});
        syncBg.setOrientation(GradientDrawable.Orientation.TL_BR);
        btnSync.setBackground(syncBg);
        LinearLayout.LayoutParams syncParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        syncParams.setMargins(0, 0, dpToPx(activity, 6), 0);
        btnSync.setLayoutParams(syncParams);

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "⚡ Starte Übertragung zu Intervals.icu...", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
                        JSONObject res = IntervalsSyncEngine.syncDate(activity, today);
                        final boolean ok = res != null && res.optBoolean("success", false);
                        final String msg = res != null ? res.optString("message", "") : "Keine Antwort";
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ok) {
                                    Toast.makeText(activity, "✅ " + today + " erfolgreich zu Intervals.icu synchronisiert!", Toast.LENGTH_LONG).show();
                                    updateCard(card, activity);
                                } else {
                                    Toast.makeText(activity, "Sync-Status: " + msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        btnRow.addView(btnSync);

        TextView btnOpen = new TextView(activity);
        btnOpen.setText("Coach-Dashboard ➔");
        btnOpen.setTextColor(0xFF38BDF8);
        btnOpen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        btnOpen.setTypeface(Typeface.DEFAULT_BOLD);
        btnOpen.setGravity(Gravity.CENTER);
        btnOpen.setPadding(0, dpToPx(activity, 10), 0, dpToPx(activity, 10));
        GradientDrawable openBg = new GradientDrawable();
        openBg.setColor(0xFF1E293B);
        openBg.setCornerRadius(dpToPx(activity, 10));
        openBg.setStroke(dpToPx(activity, 1.0f), 0x4438BDF8);
        btnOpen.setBackground(openBg);
        LinearLayout.LayoutParams openParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        openParams.setMargins(dpToPx(activity, 6), 0, 0, 0);
        btnOpen.setLayoutParams(openParams);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, IntervalsActivity.class);
                activity.startActivity(intent);
            }
        });
        btnRow.addView(btnOpen);

        card.addView(btnRow);

        return card;
    }

    private static LinearLayout createStatBox(Context context, String tag, String initialText) {
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(dpToPx(context, 3), 0, dpToPx(context, 3), 0);
        box.setLayoutParams(params);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0x50111827);
        bg.setCornerRadius(dpToPx(context, 8));
        bg.setStroke(dpToPx(context, 1.0f), 0x251E293B);
        box.setBackground(bg);
        box.setPadding(dpToPx(context, 4), dpToPx(context, 6), dpToPx(context, 4), dpToPx(context, 6));

        TextView tv = new TextView(context);
        tv.setTag(tag);
        tv.setText(initialText);
        tv.setTextColor(0xFFF1F5F9);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setGravity(Gravity.CENTER);
        box.addView(tv);

        return box;
    }

    public static void updateCard(final View card, final Activity activity) {
        if (card == null || activity == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonStr = IntervalsSyncEngine.getCoachDataJson(activity);
                    final JSONObject c = new JSONObject(jsonStr);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TextView tvBadge = card.findViewWithTag("TAG_COACH_BADGE");
                                TextView tvCorridor = card.findViewWithTag("TAG_COACH_CORRIDOR");
                                TextView tvClearance = card.findViewWithTag("TAG_COACH_CLEARANCE");
                                TextView tvTsb = card.findViewWithTag("TAG_COACH_TSB");
                                TextView tvPhase = card.findViewWithTag("TAG_COACH_PHASE");
                                TextView tvOsa = card.findViewWithTag("TAG_COACH_OSA");

                                int score = c.optInt("readinessScore", 85);
                                String label = c.optString("readinessLabel", "Optimal");
                                if (tvBadge != null) {
                                    tvBadge.setText(score + "% " + label);
                                    GradientDrawable pill = new GradientDrawable();
                                    pill.setCornerRadius(dpToPx(activity, 8));
                                    if (score >= 80) {
                                        pill.setColor(0x2610B981);
                                        tvBadge.setTextColor(0xFF10B981);
                                    } else if (score >= 55) {
                                        pill.setColor(0x26F59E0B);
                                        tvBadge.setTextColor(0xFFF59E0B);
                                    } else {
                                        pill.setColor(0x26EF4444);
                                        tvBadge.setTextColor(0xFFEF4444);
                                    }
                                    tvBadge.setBackground(pill);
                                }

                                if (tvCorridor != null) {
                                    tvCorridor.setText("🎯 " + c.optString("corridor", "Z4 Schwelle: 270 bis 316 W"));
                                }
                                if (tvClearance != null) {
                                    tvClearance.setText(c.optString("clearance", "Freigabe: 100% Go") + " • Volle Frische");
                                }
                                if (tvTsb != null) {
                                    double tsb = c.optDouble("tsb", 0.0);
                                    String sign = tsb > 0 ? "+" : "";
                                    tvTsb.setText("TSB: " + sign + (Math.round(tsb * 10) / 10.0));
                                }
                                if (tvPhase != null) {
                                    int w = c.optInt("blockWeek", 2);
                                    tvPhase.setText("Phase: W" + w + " von 4");
                                }
                                if (tvOsa != null) {
                                    JSONObject osa = c.optJSONObject("osa");
                                    String status = (osa != null && osa.has("status")) ? osa.optString("status") : "Aktiv";
                                    if (status.contains("Normal")) status = "Normal";
                                    tvOsa.setText("OSA: " + status);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static boolean findNodeWithText(AccessibilityNodeInfo node, String text) {
        if (node == null) return false;
        CharSequence cd = node.getContentDescription();
        if (cd != null && cd.toString().contains(text)) return true;
        CharSequence t = node.getText();
        if (t != null && t.toString().contains(text)) return true;
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                boolean found = findNodeWithText(child, text);
                child.recycle();
                if (found) return true;
            }
        }
        return false;
    }

    private static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()
        );
    }

    private static class WindowCallbackWrapper implements Window.Callback {
        private final Window.Callback wrapped;
        private final Activity activity;
        private final View card;

        public WindowCallbackWrapper(Window.Callback wrapped, Activity activity, View card) {
            this.wrapped = wrapped;
            this.activity = activity;
            this.card = card;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float y = event.getRawY();
                float x = event.getRawX();
                int h = activity.getResources().getDisplayMetrics().heightPixels;
                int w = activity.getResources().getDisplayMetrics().widthPixels;
                int threshold = dpToPx(activity, 85);
                if (y > (h - threshold)) {
                    boolean isDiscover = (x >= 0.20f * w && x <= 0.40f * w);
                    card.setVisibility(isDiscover ? View.VISIBLE : View.GONE);
                    if (isDiscover) {
                        updateCard(card, activity);
                    }
                }
            }
            return wrapped != null ? wrapped.dispatchTouchEvent(event) : false;
        }

        @Override public boolean dispatchKeyEvent(KeyEvent event) { return wrapped != null ? wrapped.dispatchKeyEvent(event) : false; }
        @Override public boolean dispatchKeyShortcutEvent(KeyEvent event) { return wrapped != null ? wrapped.dispatchKeyShortcutEvent(event) : false; }
        @Override public boolean dispatchTrackballEvent(MotionEvent event) { return wrapped != null ? wrapped.dispatchTrackballEvent(event) : false; }
        @Override public boolean dispatchGenericMotionEvent(MotionEvent event) { return wrapped != null ? wrapped.dispatchGenericMotionEvent(event) : false; }
        @Override public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) { return wrapped != null ? wrapped.dispatchPopulateAccessibilityEvent(event) : false; }
        @Override public View onCreatePanelView(int featureId) { return wrapped != null ? wrapped.onCreatePanelView(featureId) : null; }
        @Override public boolean onCreatePanelMenu(int featureId, Menu menu) { return wrapped != null ? wrapped.onCreatePanelMenu(featureId, menu) : false; }
        @Override public boolean onPreparePanel(int featureId, View view, Menu menu) { return wrapped != null ? wrapped.onPreparePanel(featureId, view, menu) : false; }
        @Override public boolean onMenuOpened(int featureId, Menu menu) { return wrapped != null ? wrapped.onMenuOpened(featureId, menu) : false; }
        @Override public boolean onMenuItemSelected(int featureId, MenuItem item) { return wrapped != null ? wrapped.onMenuItemSelected(featureId, item) : false; }
        @Override public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) { if (wrapped != null) wrapped.onWindowAttributesChanged(attrs); }
        @Override public void onContentChanged() { if (wrapped != null) wrapped.onContentChanged(); }
        @Override public void onWindowFocusChanged(boolean hasFocus) { if (wrapped != null) wrapped.onWindowFocusChanged(hasFocus); }
        @Override public void onAttachedToWindow() { if (wrapped != null) wrapped.onAttachedToWindow(); }
        @Override public void onDetachedFromWindow() { if (wrapped != null) wrapped.onDetachedFromWindow(); }
        @Override public void onPanelClosed(int featureId, Menu menu) { if (wrapped != null) wrapped.onPanelClosed(featureId, menu); }
        @Override public boolean onSearchRequested() { return wrapped != null ? wrapped.onSearchRequested() : false; }
        @Override public boolean onSearchRequested(SearchEvent searchEvent) { return wrapped != null ? wrapped.onSearchRequested(searchEvent) : false; }
        @Override public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) { return wrapped != null ? wrapped.onWindowStartingActionMode(callback) : null; }
        @Override public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) { return wrapped != null ? wrapped.onWindowStartingActionMode(callback, type) : null; }
        @Override public void onActionModeStarted(ActionMode mode) { if (wrapped != null) wrapped.onActionModeStarted(mode); }
        @Override public void onActionModeFinished(ActionMode mode) { if (wrapped != null) wrapped.onActionModeFinished(mode); }
    }
}

