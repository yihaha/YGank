package com.ybh.lovemeizi.module.category.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ybh.lovemeizi.module.BaseFragment;

import java.util.List;

/**
 * Created by y on 2016/5/10.
 */
public class YViewPagerAdapter extends FragmentStatePagerAdapter {
    private  String [] titles;
    private List<BaseFragment> fragments;

    public YViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public YViewPagerAdapter(FragmentManager fm, String [] titles, List<BaseFragment> fragments) {
        super(fm);
        this.titles=titles;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
