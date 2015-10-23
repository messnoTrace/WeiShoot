
package com.NationalPhotograpy.weishoot.activity.shouye;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;

/**
 * 关于微摄
 */
public class AboutWeiShootActivity extends BaseActivity {
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_about_weishoot);
        initView();
    }

    private void initView() {
        layout = (LinearLayout) findViewById(R.id.layout);
        switch ((int) (Math.random() * 3 + 1)) {
            case 1:
                layout.setBackgroundResource(R.drawable.spl1);
                break;
            case 2:
                layout.setBackgroundResource(R.drawable.spl2);
                break;
            case 3:
                layout.setBackgroundResource(R.drawable.spl3);
                break;

            default:
                break;
        }
    }
}
