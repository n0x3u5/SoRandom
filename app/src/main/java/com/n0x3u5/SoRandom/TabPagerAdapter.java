package com.n0x3u5.SoRandom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private Bundle args;
    private List<Fragment> fragments;

    public TabPagerAdapter(FragmentManager fm, List<Fragment> fragments,
                                  Bundle args) {
        super(fm);
        this.fragments = fragments;
        this.args = args;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

   /* @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }*/
}