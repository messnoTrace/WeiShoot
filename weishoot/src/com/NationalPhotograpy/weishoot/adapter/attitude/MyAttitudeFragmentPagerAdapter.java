
package com.NationalPhotograpy.weishoot.adapter.attitude;

import java.util.ArrayList;
import java.util.List;

import com.NationalPhotograpy.weishoot.fragment.FragmentMyAttitude;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyAttitudeFragmentPagerAdapter extends FragmentPagerAdapter {

    List<FragmentMyAttitude> datas;

    public MyAttitudeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        datas = new ArrayList<FragmentMyAttitude>();
    }

    public void setData(List<FragmentMyAttitude> data) {
        if (data != null && data.size() > 0) {
            datas.clear();
            datas.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int arg0) {
        return datas.get(arg0);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

}
