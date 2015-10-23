package com.NationalPhotograpy.weishoot.view.pageindicator;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.registered.ThirdLoginActivity;
import com.NationalPhotograpy.weishoot.adapter.registered.FragmentTabAdapter;


public class ExcellentSectionTabs extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnClickListener  {
    private RelativeLayout mTradeRl;
    private RelativeLayout mBrowseRl;
    private ArrayList<RelativeLayout> mToolsList;

    private UnderLineIndicator mLineIndicator;

    private TextView mTradeTv;
    private TextView mBrowseTv;

    private FragmentTabAdapter adapter;
    private ThirdLoginActivity thirdLoginActivity;
    private int selectIndex = 0;

    public ExcellentSectionTabs(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(ViewPager pager) {
        mLineIndicator.setViewPager(pager);
        mLineIndicator.setOnPageChangeListener(this);
    }

    public void setFragmentAdapter(FragmentTabAdapter adapter) {
        this.adapter = adapter;
    }

    public void setExcellentFragment(ThirdLoginActivity activity) {
        thirdLoginActivity = activity;
    }

    public void setSelectIndex(int index) {
        selectIndex = index;
        mLineIndicator.setCurrentItem(selectIndex);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        mTradeRl = (RelativeLayout) findViewById(R.id.trade_rl);
        mBrowseRl = (RelativeLayout) findViewById(R.id.browse_rl);

        mToolsList = new ArrayList<RelativeLayout>(2);
        mToolsList.add(mTradeRl);
        mToolsList.add(mBrowseRl);

        mTradeTv = (TextView) findViewById(R.id.trade_tv);
        mBrowseTv = (TextView) findViewById(R.id.browse_tv);


        mLineIndicator = (UnderLineIndicator) findViewById(R.id.indicator);

        mTradeRl.setOnClickListener(this);
        mBrowseRl.setOnClickListener(this);

        mTradeRl.setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setTabSelectedState(arg0);
        onTabSelected(arg0);
    }

    private void setTabSelectedState(int index) {
        for (int i = 0; i < 2; i++) {
            boolean selected = i == index;
            mToolsList.get(i).setSelected(selected);
        }
    }

    private void onTabSelected(int index) {
        if (thirdLoginActivity == null || (!(thirdLoginActivity instanceof ThirdLoginActivity))) {
            return;
        }
        thirdLoginActivity.doSelectLogic(index);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.trade_rl:
                mLineIndicator.setCurrentItem(0);
                break;
            case R.id.browse_rl:
                mLineIndicator.setCurrentItem(1);
                break;
           
        }
    }

    public void setSkipPage(int index) {
        if (index >= 0 && index <= 2) {
            mLineIndicator.setCurrentItem(index);
        }
    }
}
