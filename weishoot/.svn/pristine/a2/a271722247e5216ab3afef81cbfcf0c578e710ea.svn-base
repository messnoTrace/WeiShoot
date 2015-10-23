
package com.NationalPhotograpy.weishoot.activity.quanzi;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;

public class ConversationActivity extends BaseActivity {

    private TextView tv_title;

    private LinearLayout layout_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);

    }

    private void initData() {
        tv_title.setText(getIntent().getData().getQueryParameter("title"));
    }

    private void setListener() {
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
