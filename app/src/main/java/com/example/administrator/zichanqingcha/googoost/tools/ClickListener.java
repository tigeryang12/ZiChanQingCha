package com.example.administrator.zichanqingcha.googoost.tools;

import android.view.View;

import java.util.Calendar;

/**
 * Created by 刘月 on 17/04/12.
 * 自定义间隔点击事件（间隔1s）
 */

public abstract class ClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNewClick(v);
        }
    }

    protected abstract void onNewClick(View v);
}

