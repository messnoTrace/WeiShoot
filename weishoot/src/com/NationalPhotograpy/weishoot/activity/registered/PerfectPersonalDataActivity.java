
package com.NationalPhotograpy.weishoot.activity.registered;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.photograph.SelectPicActivity;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PerfectPersonalDataActivity extends BaseActivity implements OnClickListener {

    private ImageButton ibtn_sex_man, ibtn_sex_woman;

    private ImageView ibtn_edit_head;

    private TextView tv_user_sex;

    private EditText edt_username;

    private Button btn_ok;

    public static final String ARG_UCODE = "arg_ucode";

    public static final String ARG_NICKNAME = "arg_nickname";

    private String uCode = "";

    private String nickName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_personal_data);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        ibtn_edit_head = (ImageView) findViewById(R.id.ibtn_edit_head);
        ibtn_sex_man = (ImageButton) findViewById(R.id.ibtn_sex_man);
        ibtn_sex_woman = (ImageButton) findViewById(R.id.ibtn_sex_woman);
        tv_user_sex = (TextView) findViewById(R.id.tv_user_sex);
        edt_username = (EditText) findViewById(R.id.edt_username);
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }

    private void initData() {
        Intent it = getIntent();
        if (it != null) {
            uCode = it.getStringExtra(ARG_UCODE);
            nickName = it.getStringExtra(ARG_NICKNAME);
            if (!TextUtils.isEmpty(nickName)) {
                edt_username.setText(nickName);
            }

        }
    }

    private void setListener() {
        ibtn_edit_head.setOnClickListener(this);
        ibtn_sex_man.setOnClickListener(this);
        ibtn_sex_woman.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.ibtn_edit_head:
                // ToDo相机或者相册
                it = new Intent(PerfectPersonalDataActivity.this, SelectPicActivity.class);
                it.putExtra("type", "head");
                startActivityForResult(it, 1);
                break;
            case R.id.ibtn_sex_man:
                tv_user_sex.setText("男");
                ibtn_sex_man.setImageResource(R.drawable.select_sex_man_select);
                ibtn_sex_woman.setImageResource(R.drawable.select_sex_woman);
                break;
            case R.id.ibtn_sex_woman:
                tv_user_sex.setText("女");
                ibtn_sex_man.setImageResource(R.drawable.select_sex_man);
                ibtn_sex_woman.setImageResource(R.drawable.select_sex_woman_select);
                break;
            case R.id.btn_ok:
                // 上传资料信息
                String nikeName = edt_username.getText().toString();
                String sex = tv_user_sex.getText().toString();
                if (TextUtils.isEmpty(nikeName)) {
                    WeiShootToast.makeErrorText(PerfectPersonalDataActivity.this, "昵称不能为空", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if ("请选择性别".equals(sex)) {
                    WeiShootToast.makeErrorText(PerfectPersonalDataActivity.this, "请选择性别", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }
                updata(nikeName, sex);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULT_HEAD) {
            String headPath = data.getStringExtra("picUri");
            ImageLoader.getInstance().displayImage(
                    "file://" + headPath,
                    ibtn_edit_head,
                    new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                            .displayer(new RoundedBitmapDisplayer(200))
                            .showImageForEmptyUri(R.drawable.default_head).build());
        }
    }

    public void updata(String nikeName, String sex) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        params.addBodyParameter("updateField", "NickName,Sex");
        params.addBodyParameter("NickName", nikeName);
        params.addBodyParameter("Sex", sex);
        httpUtils.send(HttpMethod.POST, HttpUrl.UpdateUser, params, new RequestCallBack<Object>() {
            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                WeiShootToast.makeErrorText(PerfectPersonalDataActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;
                Log.v("PerfectPersonalDataActivity", "PerfectPersonalDataActivity" + temp);
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {
                            finish();
                        } else {
                            WeiShootToast.makeErrorText(PerfectPersonalDataActivity.this,
                                    jObject.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    WeiShootToast.makeErrorText(PerfectPersonalDataActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
