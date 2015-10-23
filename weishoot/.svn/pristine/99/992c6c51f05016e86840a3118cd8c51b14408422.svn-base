
package com.NationalPhotograpy.weishoot.activity.shouye;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.shouye.BiaoQingAdapter;
import com.NationalPhotograpy.weishoot.bean.TopCommentBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean.TopicData;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.HorizontalListView;
import com.NationalPhotograpy.weishoot.view.InputMethodLayout;
import com.NationalPhotograpy.weishoot.view.InputMethodLayout.onKeyboardsChangeListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CreateCommentActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private Button btn_right;

    private EditText et_content;

    private TopicData topicData;

    private TopCommentBean commentBean;

    private String type = "";

    public static final String ARG_DATA = "arg_data";

    public static final String ARG_TYPE = "arg_type";

    public static final String ARG_CHILD_DATA = "arg_child_data";

    private HorizontalListView lv_biaoqing;

    private List<String> biaoqingList;

    private int pos;

    private InputMethodLayout input_method_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_create_comment);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        et_content = (EditText) findViewById(R.id.et_content);
        lv_biaoqing = (HorizontalListView) findViewById(R.id.lv_biaoqing);
        input_method_layout = (InputMethodLayout) findViewById(R.id.input_method_layout);
    }

    private void initData() {
        // et_content.append(StringsUtil.strToSmiley(this,
        // "辛苦、问好、祝愉快！！！[em_13][em_3]"));
        topicData = (TopicData) getIntent().getSerializableExtra(ARG_DATA);
        type = getIntent().getStringExtra(ARG_TYPE);
        pos = getIntent().getIntExtra("position", -1);

        commentBean = (TopCommentBean) getIntent().getSerializableExtra(ARG_CHILD_DATA);
        biaoqingList = new ArrayList<String>();
        for (int i = 1; i <= 97; i++) {
            biaoqingList.add("em_" + i);
        }
        BiaoQingAdapter biaoqingAdapter = new BiaoQingAdapter(this, biaoqingList);
        lv_biaoqing.setAdapter(biaoqingAdapter);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_content.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_content, 0);
            }

        }, 500);
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        lv_biaoqing.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = et_content.getSelectionStart();
                Editable editable = et_content.getText();
                CharSequence temp = StringsUtil.strToSmiley(CreateCommentActivity.this, "["
                        + biaoqingList.get(position) + "]");
                editable.insert(index, temp);
                if (editable.equals("") || editable.length() == 0 || editable.equals("null")) {

                } else {
                    et_content.setText(editable);
                    et_content.setSelection(index + temp.length());
                }
            }
        });
        input_method_layout.setOnkeyboarddStateListener(new onKeyboardsChangeListener() {// 监听软键盘状态

                    @Override
                    public void onKeyBoardStateChange(int state) {
                        switch (state) {
                            case InputMethodLayout.KEYBOARD_STATE_SHOW:
                                // 打开软键盘
                                lv_biaoqing.setVisibility(View.VISIBLE);
                                break;
                            case InputMethodLayout.KEYBOARD_STATE_HIDE:
                                // 关闭软键盘
                                lv_biaoqing.setVisibility(View.INVISIBLE);
                                break;
                        }
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
                String comment = et_content.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    WeiShootToast.makeErrorText(this, "评论不能为空", WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                if (topicData != null) {
                    requestAddComment(comment);
                }

                break;

            default:
                break;
        }
    }

    private void requestAddComment(final String comment) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        if ("parent".equals(type)) {

        } else if ("child".equals(type) && commentBean != null) {
            params.addBodyParameter("PCode", commentBean.CCode);
        }

        params.addBodyParameter("FCode", topicData.TCode);
        params.addBodyParameter("CType", topicData.TType);
        params.addBodyParameter("UCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("Contents", comment);

        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AddComment, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();

                WeiShootToast.makeErrorText(CreateCommentActivity.this,
                        CreateCommentActivity.this.getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;

                Log.v("TAG", "yyyy___" + temp);
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject object = new JSONObject(temp);

                        JSONObject obj = object.getJSONObject("result");
                        if ("200".equals(obj.optString("ResultCode"))) {

                            TopCommentBean com = new TopCommentBean();
                            com.UCode = UserInfo.getInstance(CreateCommentActivity.this)
                                    .getUserUCode();
                            com.UserHead = UserInfo.getInstance(CreateCommentActivity.this)
                                    .getUserHead();
                            com.NickName = UserInfo.getInstance(CreateCommentActivity.this)
                                    .getUserNickName();
                            com.CreateDate = System.currentTimeMillis() + "";
                            com.Contents = comment;
                            if ("child".equals(type) && commentBean != null) {
                                com.CCode = commentBean.CCode;
                            }

                            Intent intent = new Intent("comment");
                            intent.putExtra("data", com);
                            intent.putExtra("position", pos);
                            sendBroadcast(intent);

                            WeiShootToast.makeText(CreateCommentActivity.this, "评论成功",
                                    WeiShootToast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            WeiShootToast.makeErrorText(CreateCommentActivity.this,
                                    obj.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    WeiShootToast.makeErrorText(CreateCommentActivity.this,
                            CreateCommentActivity.this.getString(R.string.http_timeout),
                            WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
