
package com.NationalPhotograpy.weishoot.activity.shouye;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.adapter.UserDetialAdapter;
import com.NationalPhotograpy.weishoot.bean.TopCommentBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean.TopicData;
import com.NationalPhotograpy.weishoot.bean.UserInfosBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshBase.OnRefreshListener2;
import com.NationalPhotograpy.weishoot.view.pullzoomlistview.MyPullZoomListView;
import com.NationalPhotograpy.weishoot.view.pullzoomlistview.PullToZoomListView;
import com.NationalPhotograpy.weishoot.view.pullzoomlistview.PullToZoomListView.OnPullDownHeightListener;
import com.NationalPhotograpy.weishoot.view.pullzoomlistview.PullToZoomListView.OnPullDownListener;
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

/**
 * 个人中心 跳转至个人中心需要的参数UCode nikeName userHead 对应键值分别为ARG_UCODE ARG_NIKENAME
 * ARG_USERHEAD
 */
public class UserInfoDetialActivity extends Activity implements OnClickListener {
    private static final String TAG = "UserInfoDetialActivity";

    private MyPullZoomListView myPullListView;

    private PullToZoomListView listView;

    private UserDetialAdapter detialAdapter;

    private View mHeadView;

    private LinearLayout layout_tab, layout_tab_hide, layout_back;

    public static final String ARG_UCODE = "arg_ucode";

    public static final String ARG_NIKENAME = "arg_nikeName";

    public static final String ARG_USERHEAD = "arg_userHead";

    private String uCode = "";

    private String nikeName = "";

    private String userHead = "";

    private String sex = "";

    private TextView tv_zy,tv_sc, tv_td, tv_wdzl, tv_zy_hide, 
             tv_sc_hide, tv_td_hide, tv_wdzl_hide;
    private ImageView iv_v;

    private View view_zy,view_sc, view_td, view_wdzl, view_zy_hide,
            view_sc_hide, view_td_hide, view_wdzl_hide;

    private Button btn_gz;

    private RelativeLayout btn_right;

    private ImageView iv_head, img_sex, img_gxqm;

    private TextView tv_name, tv_gxqm, tv_fans, tv_attention;

    // private TopicBean homeTopicBean, ypTopicBean, fmTopicBean, scTopicBean;

    private List<TopicData> homeTopics = new ArrayList<TopicBean.TopicData>();

    private List<TopicData> ypTopics = new ArrayList<TopicBean.TopicData>();

    private List<TopicData> fmTopics = new ArrayList<TopicBean.TopicData>();

    private List<TopicData> scTopics = new ArrayList<TopicBean.TopicData>();

    private String lastDataHomeTime = "";

    private String lastDataYPTime = "";

    private String lastDataFMTime = "";

    private int pageIndex = 1;

    private int flag = 1;

    private ProgressiveDialog progressDialog;

    private ProgressBar pBar;

    private boolean flagGZ = false;

    private ImageView imgCover;

    private BroadcastReceiver receiver;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_user_info_detial);
        initView();
        initData();
        setListener();

    }

    private void initView() {
        progressDialog = new ProgressiveDialog(this);
        myPullListView = (MyPullZoomListView) findViewById(R.id.my_listview);
        layout_tab_hide = (LinearLayout) findViewById(R.id.layout_tab_hide);
        layout_tab_hide.setVisibility(View.INVISIBLE);
        listView = (PullToZoomListView) findViewById(R.id.mylist);
        pBar = (ProgressBar) findViewById(R.id.pBar);
        tv_zy_hide = (TextView) findViewById(R.id.tv_zy_hide);
        tv_td_hide = (TextView) findViewById(R.id.tv_td_hide);
        tv_sc_hide = (TextView) findViewById(R.id.tv_sc_hide);
        tv_wdzl_hide = (TextView) findViewById(R.id.tv_wdzl_hide);
        view_zy_hide = findViewById(R.id.view_zy_hide);
        view_td_hide = findViewById(R.id.view_td_hide);
        view_sc_hide = findViewById(R.id.view_sc_hide);
        view_wdzl_hide = findViewById(R.id.view_wdzl_hide);
        

        mHeadView = LayoutInflater.from(this).inflate(R.layout.view_userinfo_issue, null);
        layout_tab = (LinearLayout) mHeadView.findViewById(R.id.layout_tab);
        tv_zy = (TextView) mHeadView.findViewById(R.id.tv_zy);
        tv_td = (TextView) mHeadView.findViewById(R.id.tv_td);
        tv_gxqm = (TextView) mHeadView.findViewById(R.id.tv_gxqm);
        tv_wdzl = (TextView) mHeadView.findViewById(R.id.tv_wdzl);
        tv_sc = (TextView) mHeadView.findViewById(R.id.tv_sc);
        img_gxqm = (ImageView) mHeadView.findViewById(R.id.img_gxqm);
        tv_fans = (TextView) mHeadView.findViewById(R.id.tv_fans);
        tv_attention = (TextView) mHeadView.findViewById(R.id.tv_attention);
        btn_right = (RelativeLayout) mHeadView.findViewById(R.id.btn_right);
        btn_gz = (Button) mHeadView.findViewById(R.id.btn_gz);
        layout_back = (LinearLayout) mHeadView.findViewById(R.id.layout_back);
        iv_head = (ImageView) mHeadView.findViewById(R.id.iv_head);
        tv_name = (TextView) mHeadView.findViewById(R.id.tv_name);
        img_sex = (ImageView) mHeadView.findViewById(R.id.img_sex);
        view_zy = mHeadView.findViewById(R.id.view_zy);
        view_td = mHeadView.findViewById(R.id.view_td);
        view_sc = mHeadView.findViewById(R.id.view_sc);
        view_wdzl = mHeadView.findViewById(R.id.view_wdzl);
        iv_v=(ImageView) mHeadView.findViewById(R.id.iv_v);

        detialAdapter = new UserDetialAdapter(this, listView);
        listView.setAdapter(detialAdapter);
        imgCover = listView.getHeaderView();

        imgCover.setImageResource(R.drawable.bg_user_cover);
        // listView.getHeaderView().setImageResource(R.drawable.home_default);
        listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        listView.setHeaderView(mHeadView);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if ("comment".equals(arg1.getAction())) {
                    if (detialAdapter != null) {
                        TopCommentBean bean = (TopCommentBean) arg1.getSerializableExtra("data");
                        int position = arg1.getIntExtra("position", -1);
                        if (position != -1) {
                            detialAdapter.data.get(position).CommentList.add(0, bean);
                            detialAdapter.notifyDataSetChanged();
                        }

                    }

                }

            }
        };

        IntentFilter filter = new IntentFilter("comment");
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initData() {

        uCode = getIntent().getStringExtra(ARG_UCODE);
        nikeName = getIntent().getStringExtra(ARG_NIKENAME);
        userHead = getIntent().getStringExtra(ARG_USERHEAD);
        btn_gz.setVisibility(View.INVISIBLE);
        if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this).getUserUCode())) {
            tv_sc_hide.setVisibility(View.GONE);
            view_sc.setVisibility(View.GONE);
            tv_sc.setVisibility(View.GONE);
            view_sc_hide.setVisibility(View.GONE);
            tv_wdzl.setVisibility(View.GONE);
            tv_wdzl_hide.setVisibility(View.GONE);
            view_wdzl.setVisibility(View.GONE);
            view_wdzl_hide.setVisibility(View.GONE);
            btn_right.setVisibility(View.INVISIBLE);
            img_gxqm.setVisibility(View.INVISIBLE);

        }

        if (!TextUtils.isEmpty(userHead)) {
            ImageLoader.getInstance().displayImage(
                    userHead,
                    iv_head,
                    new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                            .displayer(new RoundedBitmapDisplayer(100))
                            .showImageForEmptyUri(R.drawable.default_head).build());
        }
        if (!TextUtils.isEmpty(nikeName)) {
            tv_name.setText(nikeName);
        }

        requestGetUInfo();

        requestGetHomeTopic();
        progressDialog.show();

    }

    private void setListener() {
        tv_zy.setOnClickListener(this);
        tv_td.setOnClickListener(this);
        tv_wdzl.setOnClickListener(this);
        tv_sc.setOnClickListener(this);
        tv_sc_hide.setOnClickListener(this);
        tv_zy_hide.setOnClickListener(this);
        tv_td_hide.setOnClickListener(this);
        tv_wdzl_hide.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        layout_back.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
        tv_attention.setOnClickListener(this);

        imgCover.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this).getUserUCode())) {
                    Intent it = new Intent(UserInfoDetialActivity.this, UserInfoActivity.class);
                    startActivityForResult(it, 1);
                }

            }
        });
        // 上拉加载
        myPullListView.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh() {
            }

            @Override
            public void onPullUpToRefresh() {
                switch (flag) {
                    case 1:
                        requestGetHomeTopic();
                        break;
                    case 2:
                        requestGetScTopic();
                        break;

                    default:
                        break;
                }

            }
        });
        // 下拉刷新
        listView.setOnPullDownListener(new OnPullDownListener() {

            @Override
            public void pullDown() {

                pBar.setVisibility(View.VISIBLE);
                btn_gz.setVisibility(View.GONE);
                switch (flag) {
                    case 1:
                        lastDataHomeTime = "";
                        requestGetHomeTopic();
                        break;

                    case 2:
                        pageIndex = 1;
                        requestGetScTopic();
                        break;

                    default:
                        break;
                }

            }
        });
        // 根据高度判读显示隐藏tab
        listView.setOnPullDownHeightListener(new OnPullDownHeightListener() {

            @Override
            public void pullDownHeight(int height) {
                // TODO Auto-generated method stub
                int[] location = new int[2];
                layout_tab.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];

                if (layout_tab.getHeight() >= y * 2) {
                    layout_tab_hide.setVisibility(View.VISIBLE);
                } else {
                    layout_tab_hide.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.tv_zy:
            case R.id.tv_zy_hide:
                setIndicatorBg(view_zy, view_zy_hide);
                flag = 1;
                if (homeTopics.size() > 0) {
                    detialAdapter.addData(homeTopics);
                } else {
                    lastDataHomeTime = "";
                    requestGetHomeTopic();
                    progressDialog.show();
                }
                break;


            case R.id.tv_sc:
            case R.id.tv_sc_hide:
                setIndicatorBg(view_sc, view_sc_hide);
                flag = 4;
                if (scTopics.size() > 0) {
                    detialAdapter.addData(scTopics);
                } else {
                    pageIndex = 1;
                    requestGetScTopic();
                    progressDialog.show();
                }

                break;
            case R.id.tv_td:
            case R.id.tv_td_hide:
                it = new Intent(UserInfoDetialActivity.this, MyAttitudeActivity.class);
                it.putExtra("uCode", uCode);
                it.putExtra("sex", sex);
                startActivity(it);
                break;
            case R.id.tv_wdzl:
            case R.id.tv_wdzl_hide:
                it = new Intent(UserInfoDetialActivity.this, UserInfoActivity.class);
                startActivityForResult(it, 1);
                break;
            case R.id.btn_right:
                it = new Intent(UserInfoDetialActivity.this, SettingActivity.class);
                startActivityForResult(it, 0);
                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_gz:
                requestAttentionOption();
                break;
            case R.id.tv_fans:
                it = new Intent(UserInfoDetialActivity.this, ZambiaActivity.class);
                it.putExtra(ZambiaActivity.ARG_FLAG, "attention");
                it.putExtra(ZambiaActivity.ARG_UCODE, uCode);
                startActivity(it);

                break;
            case R.id.tv_attention:
                it = new Intent(UserInfoDetialActivity.this, ZambiaActivity.class);
                it.putExtra(ZambiaActivity.ARG_FLAG, "fans");
                it.putExtra(ZambiaActivity.ARG_UCODE, uCode);
                startActivity(it);
                break;

            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {// 退出登入时销毁当前界面
            finish();
        }
        if (resultCode == Constant.RESULT_HEAD) {
            requestGetUInfo();
            requestGetHomeTopic();
            // String headPath = data.getStringExtra("picUri");
            // ImageLoader.getInstance().displayImage(
            // "file://" + headPath,
            // iv_head,
            // new
            // DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
            // .displayer(new RoundedBitmapDisplayer(100))
            // .showImageForEmptyUri(R.drawable.default_head).build());
        }

    }

    private void setIndicatorBg(View view, View viewHide) {

        view_zy.setBackgroundColor(getResources().getColor(R.color.transparent));

        view_td.setBackgroundColor(getResources().getColor(R.color.transparent));
        view_wdzl.setBackgroundColor(getResources().getColor(R.color.transparent));
        view_sc.setBackgroundColor(getResources().getColor(R.color.transparent));
        view_sc_hide.setBackgroundColor(getResources().getColor(R.color.transparent));
        view_zy_hide.setBackgroundColor(getResources().getColor(R.color.transparent));
        view_td_hide.setBackgroundColor(getResources().getColor(R.color.transparent));
        view_wdzl_hide.setBackgroundColor(getResources().getColor(R.color.transparent));
        view.setBackgroundColor(getResources().getColor(R.color.textview_orange));
        viewHide.setBackgroundColor(getResources().getColor(R.color.textview_orange));

    }

    /**
     * 获取个人信息
     */
    private void requestGetUInfo() {

        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        params.addBodyParameter("currentUCode", UserInfo.getInstance(this).getUserUCode());
        Log.v("TAG", "--->" + uCode);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetUserInfo, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;

                UserInfosBean uInfo = new Gson().fromJson(temp, new TypeToken<UserInfosBean>() {
                }.getType());
                if (uInfo != null && uInfo.result != null) {
                    if ("200".equals(uInfo.result.ResultCode) && uInfo.data != null) {

                    	if("2".equals(uInfo.data.IsRCMD)){
                    		iv_v.setVisibility(View.VISIBLE);
                    	}else {
                    		iv_v.setVisibility(View.INVISIBLE);
						}
                    	
                        tv_name.setText(uInfo.data.NickName);

                        ImageLoader.getInstance().displayImage(
                                uInfo.data.UserHead
                                        + "?"
                                        + SharePreManager.getInstance(UserInfoDetialActivity.this)
                                                .getUserHeadTimestamp(),
                                iv_head,
                                new DisplayImageOptions.Builder().cacheInMemory(true)
                                        .cacheOnDisc(true)
                                        .displayer(new RoundedBitmapDisplayer(100))
                                        .showImageForEmptyUri(R.drawable.default_head).build());

                        ImageLoader.getInstance().displayImage(
                                uInfo.data.Cover
                                        + "?"
                                        + SharePreManager.getInstance(UserInfoDetialActivity.this)
                                                .getUserHeadTimestamp(),
                                imgCover,
                                new DisplayImageOptions.Builder().cacheInMemory(true)
                                        .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(0))
                                        .showImageForEmptyUri(R.drawable.bg_user_cover).build());

                        tv_gxqm.setText(uInfo.data.Introduction);

                        tv_fans.setText("关注 " + uInfo.data.AttentionCount + " | ");
                        tv_attention.setText("粉丝 " + uInfo.data.FansCount);
                        sex = uInfo.data.Sex;
                        if ("男".equals(sex)) {
                            img_sex.setImageResource(R.drawable.sex_man);
                        } else if ("女".equals(sex)) {
                            img_sex.setImageResource(R.drawable.sex_women);
                        } else {
                            img_sex.setVisibility(View.INVISIBLE);
                        }
                        if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this)
                                .getUserUCode())) {
                            if (("0".equals(uInfo.data.IsAttention))) {
                                btn_gz.setVisibility(View.VISIBLE);
                                btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
                                btn_gz.setOnClickListener(UserInfoDetialActivity.this);
                                flagGZ = true;
                            } else {
                                btn_gz.setVisibility(View.VISIBLE);
                                btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
                                btn_gz.setOnClickListener(null);
                            }
                        } else {
                            btn_gz.setVisibility(View.GONE);
                        }
                    } else {
                        WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                                uInfo.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 获取个人主页主题
     */
    private void requestGetHomeTopic() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("PageSize", "5");
        params.addBodyParameter("DisplayCom", "5");
        params.addBodyParameter("DisplayGood", "5");
        params.addBodyParameter("CurrentUCode", UserInfo.getInstance(UserInfoDetialActivity.this)
                .getUserUCode());
        params.addBodyParameter("IsPerson", "1");
        params.addBodyParameter("CreateDate", lastDataHomeTime);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetTopic, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                progressDialog.dismiss();
                pBar.setVisibility(View.GONE);
                if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this).getUserUCode())) {
                    if (flagGZ) {
                        btn_gz.setVisibility(View.VISIBLE);
                        btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
                    } else {
                        btn_gz.setVisibility(View.VISIBLE);
                        btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
                    }
                } else {
                    btn_gz.setVisibility(View.GONE);
                }
                myPullListView.onRefreshComplete();
                WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                pBar.setVisibility(View.GONE);
                if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this).getUserUCode())) {
                    if (flagGZ) {
                        btn_gz.setVisibility(View.VISIBLE);
                        btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
                    } else {
                        btn_gz.setVisibility(View.VISIBLE);
                        btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
                    }
                } else {
                    btn_gz.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
                myPullListView.onRefreshComplete();
                String temp = (String) objectResponseInfo.result;
                Log.v(TAG, "------>Home" + temp);
                TopicBean homeTopicBean = new Gson().fromJson(temp, new TypeToken<TopicBean>() {
                }.getType());
                if (homeTopicBean != null && homeTopicBean.result != null) {
                    if ("200".equals(homeTopicBean.result.ResultCode)) {
                        if ("".equals(lastDataHomeTime)) {// 刷新操作
                            homeTopics.clear();
                        }
                        homeTopics.addAll(homeTopicBean.data);
                        if (flag == 1) {
                            detialAdapter.addData(homeTopics);
                        }
                        if (homeTopicBean.data.size() > 0) {
                            lastDataHomeTime = StringsUtil.getStringDate(homeTopicBean.data
                                    .get(homeTopicBean.data.size() - 1).CreateDate);
                        }
                    } else {
                        WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                                homeTopicBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    /**
//     * 获取个人主页影片
//     */
//    private void requestGetYpTopic() {
//        HttpUtils httpUtils = new HttpUtils(1000 * 20);
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("UCode", uCode);
//        params.addBodyParameter("PageSize", "5");
//        params.addBodyParameter("DisplayCom", "5");
//        params.addBodyParameter("DisplayGood", "5");
//        params.addBodyParameter("CurrentUCode", UserInfo.getInstance(UserInfoDetialActivity.this)
//                .getUserUCode());
//        params.addBodyParameter("IsPerson", "1");
//        params.addBodyParameter("TType", "5");
//        params.addBodyParameter("CreateDate", lastDataYPTime);
//        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
//        httpUtils.send(HttpMethod.POST, HttpUrl.GetTopic, params, new RequestCallBack<Object>() {
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                e.printStackTrace();
//                pBar.setVisibility(View.GONE);
//                if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this).getUserUCode())) {
//                    if (flagGZ) {
//                        btn_gz.setVisibility(View.VISIBLE);
//                        btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
//                    } else {
//                        btn_gz.setVisibility(View.VISIBLE);
//                        btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
//                    }
//                } else {
//                    btn_gz.setVisibility(View.GONE);
//                }
//                progressDialog.dismiss();
//                myPullListView.onRefreshComplete();
//                WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
//                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
//                pBar.setVisibility(View.GONE);
//                if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this).getUserUCode())) {
//                    if (flagGZ) {
//                        btn_gz.setVisibility(View.VISIBLE);
//                        btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
//                    } else {
//                        btn_gz.setVisibility(View.VISIBLE);
//                        btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
//                    }
//                } else {
//                    btn_gz.setVisibility(View.GONE);
//                }
//                progressDialog.dismiss();
//                myPullListView.onRefreshComplete();
//                String temp = (String) objectResponseInfo.result;
//                Log.v(TAG, "------>Yp" + temp);
//                TopicBean ypTopicBean = new Gson().fromJson(temp, new TypeToken<TopicBean>() {
//                }.getType());
//                if (ypTopicBean != null && ypTopicBean.result != null) {
//                    if ("200".equals(ypTopicBean.result.ResultCode)) {
//                        if ("".equals(lastDataYPTime)) {// 刷新操作
//                            ypTopics.clear();
//                        }
//                        ypTopics.addAll(ypTopicBean.data);
//                        if (flag == 2) {
//                            detialAdapter.addData(ypTopics);
//                        }
//                        if (ypTopicBean.data.size() > 0) {
//                            lastDataYPTime = StringsUtil.getStringDate(ypTopicBean.data
//                                    .get(ypTopicBean.data.size() - 1).CreateDate);
//                        }
//                    } else {
//                        WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
//                                ypTopicBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
//                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }

    /**
     * 获取个人主页封面
     */
    private void requestGetFmTopic() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("PageSize", "5");
        params.addBodyParameter("DisplayCom", "5");
        params.addBodyParameter("DisplayGood", "5");
        params.addBodyParameter("CurrentUCode", UserInfo.getInstance(UserInfoDetialActivity.this)
                .getUserUCode());
        params.addBodyParameter("IsPerson", "1");
        params.addBodyParameter("TType", "6");
        params.addBodyParameter("CreateDate", lastDataFMTime);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetTopic, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                pBar.setVisibility(View.GONE);
                if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this).getUserUCode())) {
                    if (flagGZ) {
                        btn_gz.setVisibility(View.VISIBLE);
                        btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
                    } else {
                        btn_gz.setVisibility(View.VISIBLE);
                        btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
                    }
                } else {
                    btn_gz.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
                myPullListView.onRefreshComplete();
                WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                        getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                pBar.setVisibility(View.GONE);
                if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this).getUserUCode())) {
                    if (flagGZ) {
                        btn_gz.setVisibility(View.VISIBLE);
                        btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
                    } else {
                        btn_gz.setVisibility(View.VISIBLE);
                        btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
                    }
                } else {
                    btn_gz.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
                myPullListView.onRefreshComplete();
                String temp = (String) objectResponseInfo.result;
                Log.v(TAG, "------>Fm" + temp);
                TopicBean fmTopicBean = new Gson().fromJson(temp, new TypeToken<TopicBean>() {
                }.getType());
                if (fmTopicBean != null && fmTopicBean.result != null) {
                    if ("200".equals(fmTopicBean.result.ResultCode)) {
                        if ("".equals(lastDataFMTime)) {// 刷新操作
                            fmTopics.clear();
                        }
                        fmTopics.addAll(fmTopicBean.data);
                        if (flag == 3) {
                            detialAdapter.addData(fmTopics);
                        }
                        if (fmTopicBean.data.size() > 0) {
                            lastDataFMTime = StringsUtil.getStringDate(fmTopicBean.data
                                    .get(fmTopicBean.data.size() - 1).CreateDate);
                        }
                    } else {
                        WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                                fmTopicBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                            getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 获取个人主页收藏
     */
    private void requestGetScTopic() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(UserInfoDetialActivity.this)
                .getUserUCode());
        params.addBodyParameter("PageSize", "5");
        params.addBodyParameter("PageIndex", pageIndex + "");
        params.addBodyParameter("DisplayCom", "5");
        params.addBodyParameter("DisplayGood", "5");
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetCollections, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        pBar.setVisibility(View.GONE);
                        if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this)
                                .getUserUCode())) {
                            if (flagGZ) {
                                btn_gz.setVisibility(View.VISIBLE);
                                btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
                            } else {
                                btn_gz.setVisibility(View.VISIBLE);
                                btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
                            }
                        } else {
                            btn_gz.setVisibility(View.GONE);
                        }
                        progressDialog.dismiss();
                        myPullListView.onRefreshComplete();
                        WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        myPullListView.onRefreshComplete();
                        String temp = (String) objectResponseInfo.result;
                        Log.v(TAG, "------>Sc" + temp);
                        TopicBean scTopicBean = new Gson().fromJson(temp,
                                new TypeToken<TopicBean>() {
                                }.getType());
                        if (scTopicBean != null && scTopicBean.result != null) {
                            if ("200".equals(scTopicBean.result.ResultCode)) {
                                if (pageIndex == 1) {
                                    scTopics.clear();
                                }
                                scTopics.addAll(scTopicBean.data);
                                if (flag == 4) {
                                    detialAdapter.addData(scTopics);
                                }
                                pageIndex++;
                            } else {
                                WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                                        scTopicBean.result.ResultMsg, WeiShootToast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    private void requestAttentionOption() {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("FCode", uCode);
        params.addBodyParameter("PCode", UserInfo.getInstance(UserInfoDetialActivity.this)
                .getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AttentionOption, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        pBar.setVisibility(View.GONE);
                        if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this)
                                .getUserUCode())) {
                            if (flagGZ) {
                                btn_gz.setVisibility(View.VISIBLE);
                                btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
                            } else {
                                btn_gz.setVisibility(View.VISIBLE);
                                btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
                            }
                        } else {
                            btn_gz.setVisibility(View.GONE);
                        }
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                                UserInfoDetialActivity.this.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        pBar.setVisibility(View.GONE);
                        if (!uCode.equals(UserInfo.getInstance(UserInfoDetialActivity.this)
                                .getUserUCode())) {
                            if (flagGZ) {
                                btn_gz.setVisibility(View.VISIBLE);
                                btn_gz.setBackgroundResource(R.drawable.icon_gz_g);
                            } else {
                                btn_gz.setVisibility(View.VISIBLE);
                                btn_gz.setBackgroundResource(R.drawable.find_bg_isattention);
                            }
                        } else {
                            btn_gz.setVisibility(View.GONE);
                        }
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {

                                    btn_gz.setVisibility(View.INVISIBLE);
                                    flagGZ = false;
                                    WeiShootToast.makeText(UserInfoDetialActivity.this, "关注成功",
                                            WeiShootToast.LENGTH_SHORT).show();

                                } else {
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(UserInfoDetialActivity.this,
                                    UserInfoDetialActivity.this.getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
