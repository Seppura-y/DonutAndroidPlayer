package com.example.donutplayer;

import android.view.View;

public class DoubleClickListener implements View.OnClickListener {
    private long doubleClickTimeLimitMills = 500;
    private long lastClicked = -1L;
    private Callback callback;

    public DoubleClickListener(Callback callback) {
        this.callback = callback;
    }

    public DoubleClickListener(long doubleClickTimeLimitMills, Callback callback) {
        this.doubleClickTimeLimitMills = doubleClickTimeLimitMills;
        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        if (lastClicked == -1L) {
            lastClicked = System.currentTimeMillis();
        } else if (isDoubleClicked()) {
            callback.doubleClicked();
            lastClicked = -1L;
        } else {
            lastClicked = System.currentTimeMillis();
        }
    }

    private long getTimeDiff(long from, long to) {
        return to - from;
    }

    private boolean isDoubleClicked() {
        return getTimeDiff(lastClicked, System.currentTimeMillis()) <= doubleClickTimeLimitMills;
    }

    public interface Callback {
        void doubleClicked();
    }
}