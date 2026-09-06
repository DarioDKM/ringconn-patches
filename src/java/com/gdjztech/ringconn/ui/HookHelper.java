package com.gdjztech.ringconn.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HookHelper {
    public static void attachFloatingButton(final Activity activity) {
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    View decorView = activity.getWindow().getDecorView();
                    if (!(decorView instanceof ViewGroup)) return;
                    ViewGroup root = (ViewGroup) decorView;

                    if (root.findViewWithTag("INTERVALS_SYNC_FAB") != null) {
                        return;
                    }

                    int sizeDp = 48;
                    int sizePx = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, sizeDp, activity.getResources().getDisplayMetrics()
                    );
                    int marginDp = 16;
                    int marginPx = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, marginDp, activity.getResources().getDisplayMetrics()
                    );
                    int bottomOffsetPx = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 110, activity.getResources().getDisplayMetrics()
                    );

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sizePx, sizePx);
                    params.gravity = Gravity.BOTTOM | Gravity.END;
                    params.setMargins(0, 0, marginPx, bottomOffsetPx);

                    final TextView fab = new TextView(activity);
                    fab.setTag("INTERVALS_SYNC_FAB");
                    fab.setText("⚡");
                    fab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    fab.setGravity(Gravity.CENTER);
                    fab.setTextColor(Color.WHITE);

                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.OVAL);
                    shape.setColors(new int[]{0xFF0284C7, 0xFF38BDF8});
                    shape.setOrientation(GradientDrawable.Orientation.TL_BR);
                    shape.setStroke(2, 0xFFBAE6FD);
                    fab.setBackground(shape);
                    fab.setElevation(20f);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, IntervalsActivity.class);
                            activity.startActivity(intent);
                        }
                    });

                    fab.setOnTouchListener(new View.OnTouchListener() {
                        private float initialTouchX, initialTouchY;
                        private boolean isDragging = false;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    initialTouchX = event.getRawX();
                                    initialTouchY = event.getRawY();
                                    isDragging = false;
                                    return true;
                                case MotionEvent.ACTION_MOVE:
                                    float dx = event.getRawX() - initialTouchX;
                                    float dy = event.getRawY() - initialTouchY;
                                    if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                                        isDragging = true;
                                        fab.setTranslationX(fab.getTranslationX() + dx);
                                        fab.setTranslationY(fab.getTranslationY() + dy);
                                        initialTouchX = event.getRawX();
                                        initialTouchY = event.getRawY();
                                    }
                                    return true;
                                case MotionEvent.ACTION_UP:
                                    if (!isDragging) {
                                        v.performClick();
                                    }
                                    return true;
                            }
                            return false;
                        }
                    });

                    root.addView(fab, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
