
package com.NationalPhotograpy.weishoot.activity.shouye;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.photograph.WeiShootWebActiivty;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;

public class SettingActivity extends BaseActivity implements OnClickListener {

    private TextView tv_title;

    private Button btn_new_push, cb_circle_push;

    private RelativeLayout layout_clear_cache, layout_about_weishoot, layout_help, layout_advice;

    private Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_setting);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        if (!TextUtils.isEmpty(UserInfo.getInstance(this).getUserNickName())) {
            tv_title.setText(UserInfo.getInstance(this).getUserNickName());
        }
        if (JPushInterface.isPushStopped(SettingActivity.this)) {
            btn_new_push.setSelected(false);
        } else {
            btn_new_push.setSelected(true);
        }
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_new_push = (Button) findViewById(R.id.btn_new_push);
        cb_circle_push = (Button) findViewById(R.id.cb_circle_push);
        layout_clear_cache = (RelativeLayout) findViewById(R.id.layout_clear_cache);
        layout_about_weishoot = (RelativeLayout) findViewById(R.id.layout_about_weishoot);
        layout_help = (RelativeLayout) findViewById(R.id.layout_help);
        layout_advice = (RelativeLayout) findViewById(R.id.layout_advice);
        btn_exit = (Button) findViewById(R.id.btn_exit);
    }

    private void setListener() {

        tv_title.setOnClickListener(this);
        layout_clear_cache.setOnClickListener(this);
        layout_about_weishoot.setOnClickListener(this);
        layout_help.setOnClickListener(this);
        layout_advice.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_new_push.setOnClickListener(this);
        cb_circle_push.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {

            case R.id.tv_title:
                finish();
                break;
            case R.id.layout_clear_cache:
                // ToDo
                WeiShootToast.makeText(this, "已清除缓存", WeiShootToast.LENGTH_SHORT).show();
                break;
            case R.id.layout_about_weishoot:
                // ToDo
                it = new Intent(SettingActivity.this, AboutWeiShootActivity.class);
                startActivity(it);
                break;
            case R.id.layout_help:
                // ToDo
                it = new Intent(SettingActivity.this, WeiShootWebActiivty.class);
                it.putExtra("url", "http://weishoot.com/app/help");
                String postData = "u="
                        + UserInfo.getInstance(SettingActivity.this).getUserLoginName() + "&p="
                        + UserInfo.getInstance(SettingActivity.this).getUserPassword();
                it.putExtra("postData", postData);
                startActivity(it);
                break;
            case R.id.layout_advice:
                // ToDo
                it = new Intent(SettingActivity.this, UserAdviceActivity.class);
                it.putExtra("flag", "advice");
                startActivityForResult(it, 1);
                break;
            case R.id.btn_exit:
                // ToDo
                UserInfo.getInstance(SettingActivity.this).logout();
                Intent intetn1 = new Intent("logout");
                sendBroadcast(intetn1);

                Intent intent2 = new Intent();
                setResult(1, intent2);

                finish();
                break;
            case R.id.btn_new_push:
                if (JPushInterface.isPushStopped(SettingActivity.this)) {
                    JPushInterface.resumePush(SettingActivity.this);
                    SharePreManager.getInstance(SettingActivity.this).setJpushState(true);
                    btn_new_push.setSelected(true);
                } else {
                    JPushInterface.stopPush(SettingActivity.this);
                    SharePreManager.getInstance(SettingActivity.this).setJpushState(false);
                    btn_new_push.setSelected(false);
                }
                break;
            case R.id.cb_circle_push:
                if (cb_circle_push.isSelected()) {
                    cb_circle_push.setSelected(false);
                } else {
                    cb_circle_push.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

}
