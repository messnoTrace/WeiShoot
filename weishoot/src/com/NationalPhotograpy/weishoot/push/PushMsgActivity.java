
package com.NationalPhotograpy.weishoot.push;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;

public class PushMsgActivity extends Activity implements OnClickListener {

    private LinearLayout layout_back;

    private TextView tv_centent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_push_msg);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        tv_centent = (TextView) findViewById(R.id.tv_centent);
    }

    private void initData() {
        String string = getIntent().getStringExtra("msg");
        tv_centent.setText(string);
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;

            default:
                break;
        }
    }

}
