package com.example.administrator.zichanqingcha.googoost.main.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.zichanqingcha.R;
import com.example.administrator.zichanqingcha.googoost.main.base.LazyFragment;

/**
*
 *
 */
public class zhuyeFragment extends LazyFragment {
    @Override
    protected void lazyLoad() {

    }

    @Override
    public int initLayout() {
        return R.layout.fragment_zhuye;
    }

    @Override
    public void doBusiness(Context mContext, View view) {

    }

    @Override
    public boolean $onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }






}
