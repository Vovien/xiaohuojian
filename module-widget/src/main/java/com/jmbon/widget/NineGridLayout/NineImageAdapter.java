package com.jmbon.widget.NineGridLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author : ChenYangQi
 * date   : 2020/1/16 10:41
 * desc   :
 */
public abstract class NineImageAdapter {
    protected abstract int getItemCount();

    protected abstract View createView(LayoutInflater inflater, ViewGroup parent, int position);

    protected abstract void bindView(View view, int position);

    public void OnItemClick(int position, View view) {

    }
}