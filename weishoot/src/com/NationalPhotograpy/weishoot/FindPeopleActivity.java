package com.NationalPhotograpy.weishoot;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.find.RecommendedActivity;
import com.NationalPhotograpy.weishoot.activity.find.SearchWeishootActivity;
import com.NationalPhotograpy.weishoot.activity.registered.LoginActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.bean.RecommendBean;
import com.NationalPhotograpy.weishoot.bean.UserInfosBean.UserInfos;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FindPeopleActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_back;
	private EditText et_search;
	private Button btn_allCommended;
	private LinearLayout layout_tuijian,layout_search_hint;
    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsHead;
    private ProgressiveDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_people);
		findViews();
		bindListener();
		initData();
	}

	private void findViews() {
	     progressDialog = new ProgressiveDialog(this);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		layout_tuijian = (LinearLayout) findViewById(R.id.layout_tuijian);
		et_search = (EditText) findViewById(R.id.et_search);
		btn_allCommended = (Button) findViewById(R.id.btn_allCommended);
		
		 imageLoader = ImageLoader.getInstance();
	        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
	                .displayer(new RoundedBitmapDisplayer(100))
	                .showImageForEmptyUri(R.drawable.default_head).build();
	        layout_search_hint = (LinearLayout) findViewById(R.id.layout_search_hint);
	}

	private void bindListener() {
		iv_back.setOnClickListener(this);
		
		 btn_allCommended.setOnClickListener(this);
		
		et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                StringBuilder sbstr = new StringBuilder(s.toString().trim());
                if (sbstr.length() > 0) {
                    layout_search_hint.setVisibility(View.GONE);
                } else {
                    layout_search_hint.setVisibility(View.VISIBLE);
                }
            }
        });
        et_search.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {// 修改回车键功能
                    String KeyWord = et_search.getText().toString();
                    if (KeyWord.length() < 2) {
                        WeiShootToast.makeErrorText(FindPeopleActivity.this, "请输入2个字以上进行搜索",
                                WeiShootToast.LENGTH_SHORT).show();
                        return true;
                    }
                    // 先隐藏键盘
                    ((InputMethodManager)FindPeopleActivity.this.getSystemService(
                            Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(FindPeopleActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    // 跳转到搜索结果界面
                    Intent it = new Intent(FindPeopleActivity.this, SearchWeishootActivity.class);
                    it.putExtra("KeyWord", et_search.getText().toString());
                  startActivity(it);
                    return true;
                }
                return false;
            }
        });
	}

	private void initData() {
		requestGetRecommendedUsers();
	}

	@Override
	public void onClick(View v) {
		
		Intent it;
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

        case R.id.btn_allCommended:
            it = new Intent(FindPeopleActivity.this, RecommendedActivity.class);
            startActivity(it);
            break;
		default:
			break;
		}

	}

	/**
	 * 获取推荐用户
	 */
	private void requestGetRecommendedUsers() {
		HttpUtils httpUtils = new HttpUtils(1000 * 20);
		RequestParams params = new RequestParams();
		params.addBodyParameter("UCode", SharePreManager.getInstance(FindPeopleActivity.this).getUserUCode());
		params.addBodyParameter("DisplayNum", "4");
		params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
		httpUtils.send(HttpMethod.POST, HttpUrl.GetRecommendedUsers, params, new RequestCallBack<Object>() {

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				WeiShootToast.makeErrorText(FindPeopleActivity.this, getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
				String temp = (String) objectResponseInfo.result;
				RecommendBean userBean = new Gson().fromJson(temp, new TypeToken<RecommendBean>() {
				}.getType());
				if (userBean != null && userBean.result != null) {
					if ("200".equals(userBean.result.ResultCode)) {
						setTuiJianLayout(userBean.data);
					} else {
						WeiShootToast.makeErrorText(FindPeopleActivity.this, userBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
					}
				} else {
					WeiShootToast.makeErrorText(FindPeopleActivity.this, getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void setTuiJianLayout(List<UserInfos> data) {
		btn_allCommended.setVisibility(View.VISIBLE);
		layout_tuijian.removeAllViews();
		for (int i = 0; i < data.size(); i++) {
			final String uCode = data.get(i).UCode;
			View tjView = LayoutInflater.from(FindPeopleActivity.this).inflate(R.layout.item_attention_user, null);
			ImageView iv_com_head = (ImageView) tjView.findViewById(R.id.iv_com_head);
			TextView tv_com_name = (TextView) tjView.findViewById(R.id.tv_com_name);
			TextView tv_com_content = (TextView) tjView.findViewById(R.id.tv_com_content);
			Button btn_guanzhu = (Button) tjView.findViewById(R.id.btn_guanzhu);
			imageLoader.displayImage(data.get(i).UserHead, iv_com_head, mOptionsHead);
			setTuiJianListener(tjView, data.get(i));
			tv_com_name.setText(data.get(i).NickName);
			tv_com_content.setText(data.get(i).Introduction);
			if ("1".equals(data.get(i).IsAttention)) {
				btn_guanzhu.setVisibility(View.GONE);
			} else {
				btn_guanzhu.setVisibility(View.VISIBLE);
			}

			btn_guanzhu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!UserInfo.getInstance(FindPeopleActivity.this).isLogin()) {
						Intent intent = new Intent(FindPeopleActivity.this, LoginActivity.class);
						FindPeopleActivity.this.startActivity(intent);
						return;
					}

					requestAttentionOption(uCode);
				}
			});

			layout_tuijian.addView(tjView);
		}
	}

    private void setTuiJianListener(View iv_com_head, final UserInfos userInfo) {
        iv_com_head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(FindPeopleActivity.this, UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, userInfo.UCode);
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, userInfo.NickName);
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, userInfo.UserHead);
                startActivity(it);
            }
        });
    }
    private void requestAttentionOption(String fCode) {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("FCode", fCode);
        params.addBodyParameter("PCode", UserInfo.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AttentionOption, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(FindPeopleActivity.this,
                        		FindPeopleActivity.this.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {
                                    WeiShootToast.makeErrorText(FindPeopleActivity.this, "关注成功",
                                            WeiShootToast.LENGTH_SHORT).show();
                                    requestGetRecommendedUsers();
                                } else {
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(FindPeopleActivity.this,
                            		FindPeopleActivity.this.getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
