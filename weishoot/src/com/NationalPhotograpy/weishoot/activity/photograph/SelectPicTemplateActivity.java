
package com.NationalPhotograpy.weishoot.activity.photograph;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.photograph.SelectPicTemplateAdapter;
import com.NationalPhotograpy.weishoot.bean.PicTemplateBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SelectPicTemplateActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private Button btn_right;

    private ListView lv_Content;

    private Uri picUri;

    private PicTemplateBean picTempBean;

    private ProgressiveDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_select_pic_template);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        progressDialog = new ProgressiveDialog(this);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        lv_Content = (ListView) findViewById(R.id.lv_Content);
    }

    private void initData() {
        picUri = getIntent().getParcelableExtra("picUri");
        requestGetCoverClass();
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        lv_Content.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(SelectPicTemplateActivity.this,
                        ScreenshotPictorialActivity.class);
                it.putExtra("picUri", picUri);
                it.putExtra("Cid", picTempBean.data.get(position).CId);
                startActivityForResult(it, 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_right:

                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULT_UPLOAD_SUCCESS) {
            setResult(resultCode, data);
            finish();
        }
    }

    /**
     * 获取封面分类
     */
    private void requestGetCoverClass() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetCoverClass, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(SelectPicTemplateActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        picTempBean = new Gson().fromJson(temp, new TypeToken<PicTemplateBean>() {
                        }.getType());
                        if (picTempBean != null && picTempBean.result != null) {
                            if ("200".equals(picTempBean.result.ResultCode)) {
                                SelectPicTemplateAdapter adapter = new SelectPicTemplateAdapter(
                                        SelectPicTemplateActivity.this, picTempBean.data);
                                lv_Content.setAdapter(adapter);
                            } else {
                                WeiShootToast.makeErrorText(SelectPicTemplateActivity.this,
                                        picTempBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(SelectPicTemplateActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }
}
