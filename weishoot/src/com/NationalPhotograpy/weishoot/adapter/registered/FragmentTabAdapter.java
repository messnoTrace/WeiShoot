package com.NationalPhotograpy.weishoot.adapter.registered;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentTabAdapter extends FragmentStatePagerAdapter{

    private List<Fragment> mFragments;
    private Map<Integer, Fragment> mPageReferenceMap;

    public FragmentTabAdapter(FragmentManager manager, List<Fragment> fragments) {
        super(manager);
        mFragments = fragments;
        mPageReferenceMap = new HashMap<Integer, Fragment>(mFragments.size());
    }

    public Fragment getFragment(int index) {
        return mPageReferenceMap.get(Integer.valueOf(index));
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = mFragments == null ? null : mFragments.get(i);
        mPageReferenceMap.put(Integer.valueOf(i), fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container, position);
        mPageReferenceMap.put(Integer.valueOf(position), fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(Integer.valueOf(position));
    }
}
