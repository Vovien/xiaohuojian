package com.jmbon.widget.tablayout.utils;


import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.jmbon.widget.tablayout.widget.MsgView;


/**
 * 未读消息提示View,显示小红点或者带有数字的红点:
 * 数字一位,圆
 * 数字两位,圆角矩形,圆角是高度的一半
 * 数字超过两位,显示99+
 */
public class UnreadMsgUtils {
    public static void show(MsgView msgView, int num) {
        if (msgView == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(View.VISIBLE);
        if (num <= 0) {//圆点,设置默认宽高
            msgView.setStrokeWidth(0);
            msgView.setText("");

            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
        } else {
            lp.height = (int) (18 * dm.density);
            if (num < 10) {//圆
                lp.width = (int) (18 * dm.density);
                msgView.setText(num + "");
            } else if (num < 100) {//圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                msgView.setText(num + "");
            } else {
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setText(num > 100 && num < 999 ? num + "" : "999+");
            }
        }
        msgView.setLayoutParams(lp);
    }

    public static void showNotRound(MsgView msgView, int num) {
        if (msgView == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(View.VISIBLE);
        if (num <= 0) {//圆点,设置默认宽高
            msgView.setStrokeWidth(0);
            msgView.setText("");

            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
        } else {
            lp.height = (int) (18 * dm.density);
            lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            msgView.setText(num < 999 ? num + "" : "999+");
        }
        msgView.setLayoutParams(lp);
    }

    //未读0 默认是0
    public static void showNotRound2(MsgView msgView, int num) {
        if (msgView == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(View.VISIBLE);
        if (num < 0) {//圆点,设置默认宽高
            msgView.setStrokeWidth(0);
            msgView.setText("");
            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
        } else {
            lp.height = (int) (18 * dm.density);
            lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            msgView.setText(num < 999 ? num + "" : "999+");
        }
        msgView.setLayoutParams(lp);
    }

    public static void setSize(MsgView rtv, int size) {
        if (rtv == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rtv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        rtv.setLayoutParams(lp);
    }
}
