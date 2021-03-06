package com.framgia.soundclound.screen.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Sony on 1/4/2018.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private static final int TAB_COUNT = 4;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TabType.HOME:
                return new Fragment();
            case TabType.PLAY_LIST:
                return new Fragment();
            case TabType.LOCAL:
                return new Fragment();
            case TabType.FAVORITE:
                return new Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
