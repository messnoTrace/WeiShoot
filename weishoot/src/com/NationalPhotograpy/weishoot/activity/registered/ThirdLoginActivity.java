
package com.NationalPhotograpy.weishoot.activity.registered;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.registered.FragmentTabAdapter;
import com.NationalPhotograpy.weishoot.fragment.FragmentBindOldUser;
import com.NationalPhotograpy.weishoot.fragment.FragmentRegisteredNewUser;
import com.NationalPhotograpy.weishoot.view.pageindicator.ExcellentSectionTabs;

/**
 * 第三方登入注册绑定页面
 */
public class ThirdLoginActivity extends BaseActivity {

    private ExcellentSectionTabs tabs;

    private ViewPager pager;

    private FragmentTabAdapter mFragmentAdapter;

    private int selectIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_login);

        initView();
        initData();
    }

    private void initView() {
        tabs = (ExcellentSectionTabs) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);

    }

    private void initData() {

        Bundle bundle = new Bundle();
        bundle.putString("id", getIntent().getStringExtra("id"));
        bundle.putString("name", getIntent().getStringExtra("name"));
        bundle.putString("icon", getIntent().getStringExtra("icon"));
        bundle.putString("accesscode", getIntent().getStringExtra("accesscode"));
        bundle.putString("flag", getIntent().getStringExtra("flag"));

        FragmentRegisteredNewUser registeredNewUserFragment = new FragmentRegisteredNewUser();
        registeredNewUserFragment.setArguments(bundle);
        FragmentBindOldUser bindOldUserFragment = new FragmentBindOldUser();
        bindOldUserFragment.setArguments(bundle);

        List<Fragment> fragments = new ArrayList<Fragment>(2);
        fragments.add(registeredNewUserFragment);
        fragments.add(bindOldUserFragment);

        mFragmentAdapter = new FragmentTabAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(mFragmentAdapter);
        pager.setOffscreenPageLimit(2);
        tabs.setViewPager(pager);
        tabs.setSelectIndex(selectIndex);
        tabs.setFragmentAdapter(mFragmentAdapter);
        tabs.setExcellentFragment(ThirdLoginActivity.this);

    }

    /**
     * 会在ViewPager子页面被选中的时候调用
     */
    public void doSelectLogic(int index) {
    }

}
