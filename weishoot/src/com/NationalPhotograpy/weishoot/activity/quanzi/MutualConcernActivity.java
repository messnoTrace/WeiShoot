
package com.NationalPhotograpy.weishoot.activity.quanzi;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.adapter.quanzi.FriendsListAdapter;
import com.NationalPhotograpy.weishoot.adapter.quanzi.SideBar;
import com.NationalPhotograpy.weishoot.bean.MutualConcernBean;
import com.NationalPhotograpy.weishoot.bean.ZambiaBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
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

/**
 * 圈子-联系人列表
 */
public class MutualConcernActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private ListView lvContent;

    private SideBar indexBar;

    private ProgressiveDialog progressDialog;

    private List<ZambiaBean> data;

    private FriendsListAdapter friendsAdapter;

    private MutualConcernBean mutualBean;

    private EditText et_search;

    private LinearLayout layout_edittext, layout_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_mutual_concern);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        progressDialog = new ProgressiveDialog(this);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        lvContent = (ListView) findViewById(R.id.lvContent);
        indexBar = (SideBar) findViewById(R.id.sideBar);
        et_search = (EditText) findViewById(R.id.et_search);
        layout_edittext = (LinearLayout) findViewById(R.id.layout_edittext);
        layout_hint = (LinearLayout) findViewById(R.id.layout_hint);
        layout_edittext.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_title)).setText("我的摄友");
    }

    private void initData() {
        requestGetFriendByCode();
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        lvContent.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RongIM.getInstance().startPrivateChat(MutualConcernActivity.this,
//                        data.get(position).UCode.replace("-", ""), data.get(position).NickName);
            }
        });
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
                    layout_hint.setVisibility(View.GONE);
                } else {
                    layout_hint.setVisibility(View.VISIBLE);
                }
                if (mutualBean != null && mutualBean.data != null && mutualBean.data.size() > 0) {
                    if (sbstr.length() == 0) {
                        if (friendsAdapter.getCount() == mutualBean.data.size()) {
                            // 如果adapter的数量与datas相同(就是一直在按DEL键),直接返回
                            return;
                        } else {
                            // 否则载入所有数据,这种情况出现在只有一个字符,当按下删除键时输入框为空,载入所有城市列表
                            friendsAdapter.setData(mutualBean.data);
                        }
                    } else {
                        ArrayList<ZambiaBean> SearchData = new ArrayList<ZambiaBean>();
                        if (mutualBean.data != null) {
                            for (int i = 0; i < mutualBean.data.size(); i++) {
                                if (mutualBean.data.get(i).NickName.indexOf(s.toString()) > -1) {
                                    SearchData.add(mutualBean.data.get(i));
                                }
                            }
                            friendsAdapter.setData(SearchData);
                            friendsAdapter.setLightHeightText(s.toString());
                            friendsAdapter.notifyDataSetChanged();
                        }
                    }
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
            default:
                break;
        }
    }

    private void onRefresh(List<ZambiaBean> data) {
        this.data = data;
        friendsAdapter = new FriendsListAdapter(this, "通讯录");
        friendsAdapter.setFlag("attention");
        friendsAdapter.setData(data);
        lvContent.setAdapter(friendsAdapter);
        HashSet<Character> set = new HashSet<Character>();
        for (int i = 0; i < data.size(); i++) {
            String fanyi = StringsUtil.converterToFirstSpell(data.get(i).NickName
                    .toUpperCase(Locale.ENGLISH));
            char[] strChar = fanyi.toCharArray();
            set.add(strChar[0]);
        }
        indexBar.setIndex(set);
        indexBar.setListView(lvContent);
    }

    /**
     * 获取联系人
     */
    private void requestGetFriendByCode() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", SharePreManager.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetFriendByCode, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(MutualConcernActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        mutualBean = new Gson().fromJson(temp, new TypeToken<MutualConcernBean>() {
                        }.getType());
                        if (mutualBean != null && mutualBean.result != null) {
                            if ("200".equals(mutualBean.result.ResultCode)) {
                                if (mutualBean.data != null) {
                                    mutualBean.data.add(new ZambiaBean("KEFU1440650315357", "微摄客服",
                                            "http://im.weishoot.com/Content/images/logokf.png"));
                                    onRefresh(mutualBean.data);
                                    Set<ZambiaBean> zamSet = new HashSet(
                                            StaticInfo.mutualConcernList);
                                    zamSet.addAll(mutualBean.data);
                                    StaticInfo.mutualConcernList.clear();
                                    StaticInfo.mutualConcernList.addAll(new ArrayList(zamSet));
                                }
                            } else {
                                WeiShootToast.makeErrorText(MutualConcernActivity.this,
                                        mutualBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(MutualConcernActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }
}
