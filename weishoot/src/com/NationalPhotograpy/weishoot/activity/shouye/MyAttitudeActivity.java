
package com.NationalPhotograpy.weishoot.activity.shouye;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.attitude.MyAttitudeFragmentPagerAdapter;
import com.NationalPhotograpy.weishoot.fragment.FragmentMyAttitude;
/**
 * 态度
 */
public class MyAttitudeActivity extends BaseActivity implements OnClickListener {
    private TextView tv_title;

    private ViewPager vpager_attitude;

    public static final String ARG_PAGE = "arg_page";
    public static final String ARG_UCODE = "arg_ucode";
    public static final String ARG_SEX = "arg_sex";

    private View point1, point2;

    private String uCode = "";
    private String sex = "";

    private MyAttitudeFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_my_attitude);

        initView();
        setListener();
    }

   
    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        vpager_attitude = (ViewPager) findViewById(R.id.vpager_attitude);
        point1 = findViewById(R.id.point1);
        point2 = findViewById(R.id.point2);
        setPointBg(point1);
        uCode = getIntent().getStringExtra("uCode");
        sex = getIntent().getStringExtra("sex");
        
        pagerAdapter = new MyAttitudeFragmentPagerAdapter(getSupportFragmentManager());
        vpager_attitude.setAdapter(pagerAdapter);
        pagerAdapter.setData(getFragmentMyAttitudes());
    }
    
    private void setListener() {
        tv_title.setOnClickListener(this);
        vpager_attitude.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        setPointBg(point1);
                        break;
                    case 1:
                        setPointBg(point2);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    public List<FragmentMyAttitude> getFragmentMyAttitudes() {
        List<FragmentMyAttitude> fragmentMyAttitudes = new ArrayList<FragmentMyAttitude>();
        for (int i = 0; i < 2; i++) {
            FragmentMyAttitude fs = new FragmentMyAttitude();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_PAGE, i);
            bundle.putString(ARG_UCODE, uCode);
            bundle.putString(ARG_SEX, sex);
            
            fs.setArguments(bundle);
            fragmentMyAttitudes.add(fs);
        }

        return fragmentMyAttitudes;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title:
                finish();
                break;

            default:
                break;
        }
    }

    public void setPointBg(View v) {
        point1.setBackgroundResource(R.drawable.point_shape);
        point2.setBackgroundResource(R.drawable.point_shape);
        v.setBackgroundResource(R.drawable.point_shape2);
    }

}
