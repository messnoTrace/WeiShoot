
package com.NationalPhotograpy.weishoot.activity;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.photograph.PhotoHomeActivity;
import com.NationalPhotograpy.weishoot.activity.quanzi.MutualConcernActivity;
import com.NationalPhotograpy.weishoot.activity.registered.LoginActivity;
import com.NationalPhotograpy.weishoot.bean.MutualConcernBean;
import com.NationalPhotograpy.weishoot.bean.ReqUploadBean;
import com.NationalPhotograpy.weishoot.bean.ZambiaBean;
import com.NationalPhotograpy.weishoot.fragment.CategoryFragment;
import com.NationalPhotograpy.weishoot.fragment.FragmentDiscovery;
import com.NationalPhotograpy.weishoot.fragment.FragmentFind;
import com.NationalPhotograpy.weishoot.fragment.FragmentHome;
import com.NationalPhotograpy.weishoot.fragment.FragmentMarket;
import com.NationalPhotograpy.weishoot.fragment.IndexFragment;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.push.PushUtil;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.view.FriendsAddPopupWindow;
import com.NationalPhotograpy.weishoot.view.StyleAlertDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MainActivity extends BaseActivity implements OnClickListener {

    private ImageButton ibtn_sy,ibtn_kj,ibtn_fl ,ibtn_fx,   ibtn_qz, ibtn_zx,ibtn_sc;

    
    
    
    
 
    /**
     * 当前fragment的index
     */
    private int CurrentFragment;

    private List<Fragment> fragments;

    private final int FR_SY = 0;// 首页

    private final int FR_KJ = 1;// 空间

    private final int FR_FL = 2;// 分类

    private final int FR_FX = 3;// 发现
    
    
    
    
    
    

    private long mExitTime;

    private Button btn_lianxiren, btn_quanzi_right;

    private RelativeLayout layout_top;

    private boolean isConnectionStatus;

    private DbUtils attenDB;

    public static String intentRequest = "";

    public static ReqUploadBean uploadBean;

    public static boolean isClickPhoto;

    public FragmentMarket fragmentMarket;

    public TextView iv_sendMessageState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sp.userIsLogin() && !StaticInfo.OnceFlag) {
            StaticInfo.OnceFlag = true;
//            requestGetFriendByCode();
//            requestGetToken();
//            initPush();
        }
        if (isConnectionStatus) {

        }
        android.os.Message msg = new android.os.Message();
        try {
//            msg.arg1 = RongIM.getInstance().getRongIMClient().getTotalUnreadCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendMsgHandler.sendMessage(msg);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String intentResult = intent.getStringExtra("Result");
        intentRequest = intent.getStringExtra("Request");
        uploadBean = (ReqUploadBean) intent.getSerializableExtra("ReqUploadBean");
        if ("RESULT_UPLOAD_HOME".equals(intentResult)) {
            checkBtn(ibtn_kj);
            addFragment(FR_KJ);
        }
//        } else if ("WEBVIEW".equals(intentResult)) {
//            checkBtn(ibtn_sc);
//            addFragment(FR_FX);
//        }
    }

    private void initView() {
        ibtn_sy = (ImageButton) findViewById(R.id.ibtn_sy);
        ibtn_kj=(ImageButton) findViewById(R.id.ibtn_kj);
        ibtn_fl=(ImageButton) findViewById(R.id.ibtn_fl);
        ibtn_fx=(ImageButton) findViewById(R.id.ibtn_fx);

        
        
        ibtn_qz = (ImageButton) findViewById(R.id.ibtn_qz);
        ibtn_zx = (ImageButton) findViewById(R.id.ibtn_zx);
        ibtn_sc = (ImageButton) findViewById(R.id.ibtn_sc);
        
        


        layout_top = (RelativeLayout) findViewById(R.id.layout_top);
        layout_top.setVisibility(View.GONE);
        btn_lianxiren = (Button) findViewById(R.id.btn_lianxiren);
        btn_quanzi_right = (Button) findViewById(R.id.btn_quanzi_right);
        iv_sendMessageState = (TextView) findViewById(R.id.iv_sendMessageState);
        attenDB = DbUtils.create(this, "user_attention");
        attenDB.configAllowTransaction(true);
    }

    private void initData() {
        // 添加fragment
        fragments = new ArrayList<Fragment>();
        fragments.add(new IndexFragment());
        fragments.add(new FragmentHome());
        fragments.add(new CategoryFragment());
//        fragments.add(new FragmentFind());
        fragments.add(new FragmentDiscovery());
        
        
//        fragments.add(initConversationListFragment());
//        fragments.add(new FragmentFind());
//        fragmentMarket = new FragmentMarket();
//        fragments.add(fragmentMarket);

        ibtn_sy.setSelected(true);
        addFragment(FR_SY);
//        ShareSDK.initSDK(this);
        // 读取联系人数据库
        QueryAttention();
        requestGetNewVersion();
    }

    private void QueryAttention() {
        try {
            List<ZambiaBean> tempBean = attenDB.findAll(ZambiaBean.class);
            if (tempBean != null) {
                StaticInfo.mutualConcernList.addAll(tempBean);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        ibtn_sy.setOnClickListener(this);
        ibtn_kj.setOnClickListener(this);
        ibtn_fl.setOnClickListener(this);
        ibtn_fx.setOnClickListener(this);
        
        ibtn_qz.setOnClickListener(this);
        ibtn_zx.setOnClickListener(this);
        ibtn_sc.setOnClickListener(this);
        
        btn_lianxiren.setOnClickListener(this);
        btn_quanzi_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.ibtn_sy:
                if (ibtn_sy.isSelected())
                    return;
                checkBtn(v);
                addFragment(FR_SY);
                break;
                
            case R.id.ibtn_kj:
            	if(ibtn_kj.isSelected())
            		return;
            	checkBtn(v);
            	addFragment(FR_KJ);
            	break;
            case R.id.ibtn_fl:
            	if(ibtn_fl.isSelected())
            		return;
            	checkBtn(v);
            	addFragment(FR_FL);
            	break;
            case R.id.ibtn_fx:
                if (ibtn_fx.isSelected())
                    return;
                checkBtn(v);
                addFragment(FR_FX);
                break;
                
                
                
            case R.id.ibtn_qz:
//                if (ibtn_qz.isSelected())
//                    return;
//                if (!com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo.getInstance(
//                        MainActivity.this).isLogin()) {
//                    it = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(it);
//                    return;
//                }
//                if (isConnectionStatus) {
//                    setRongIMConnect();
//                }
//                checkBtn(v);
//                addFragment(FR_QZ);
//                layout_top.setVisibility(View.VISIBLE);
                break;
            case R.id.ibtn_zx:
                if (!com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo.getInstance(
                        MainActivity.this).isLogin()) {
                    it = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(it);
                    return;
                }
                if (isClickPhoto) {
                    return;
                }
                it = new Intent(MainActivity.this, PhotoHomeActivity.class);
                startActivity(it);
                break;

            case R.id.ibtn_sc:
//                if (ibtn_sc.isSelected())
//                    return;
//                checkBtn(v);
//                addFragment(FR_SC);
                break;
            case R.id.btn_lianxiren:
                it = new Intent(MainActivity.this, MutualConcernActivity.class);
                startActivity(it);
                break;
            case R.id.btn_quanzi_right:
                FriendsAddPopupWindow sharePopWindow = new FriendsAddPopupWindow(MainActivity.this);
                if (sharePopWindow.isShowing()) {
                    sharePopWindow.dismiss();
                } else {
                    sharePopWindow.showAtLocation(btn_quanzi_right, Gravity.TOP | Gravity.RIGHT, 0,
                            (int) DimensionPixelUtil
                                    .getDimensionPixelSize(1, 77, MainActivity.this));
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && ibtn_sc.isSelected()) {
            if (fragmentMarket.index == 1) {
                if (!TextUtils.isEmpty(fragmentMarket.postData)) {
                    fragmentMarket.mwebview.postUrl(fragmentMarket.str_url,
                            fragmentMarket.postData.getBytes());
                } else {
                    fragmentMarket.mwebview.loadUrl(fragmentMarket.str_url);
                }
                fragmentMarket.index--;
                return true;
            } else if (fragmentMarket.index > 1) {
                fragmentMarket.mwebview.goBack();
                fragmentMarket.index--;
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                WeiShootToast.makeErrorText(MainActivity.this, "再按一次退出程序",
                        WeiShootToast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
                return false;
            } else {
//                RongIM.getInstance().logout();
                StaticInfo.OnceFlag = false;
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkBtn(View view) {
        ibtn_sy.setSelected(false);
        ibtn_kj.setSelected(false);
        ibtn_fl.setSelected(false);
        ibtn_fl.setSelected(false);
        
        
        
        ibtn_qz.setSelected(false);
        ibtn_zx.setSelected(false);
        ibtn_fx.setSelected(false);
        ibtn_sc.setSelected(false);
        layout_top.setVisibility(View.GONE);
        view.setSelected(true);

    }


    private Handler sendMsgHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.arg1 > 0) {
                iv_sendMessageState.setVisibility(View.VISIBLE);
                iv_sendMessageState.setText(msg.arg1 + "");
            } else {
                iv_sendMessageState.setVisibility(View.INVISIBLE);
            }
        };
    };

    private void addFragment(int temp) {
        Fragment fragment = fragments.get(temp);
        FragmentTransaction ft = obtainFragmentTransaction(temp);
        fragments.get(CurrentFragment).onPause(); // 暂停当前tab
        fragments.get(CurrentFragment).onStop(); // 暂停当前tab
        if (fragment.isAdded()) {
            fragment.onStart(); // 启动目标tab的onStart()
            fragment.onResume(); // 启动目标tab的onResume()
        } else {
            ft.add(R.id.myfragment, fragment);
        }
        showTab(temp); // 显示目标tab
        ft.commit();
    }

    /**
     * 获取一个带动画的FragmentTransaction
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        return ft;
    }

    /**
     * 切换tab
     */
    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        CurrentFragment = idx; // 更新目标tab为当前tab
    }

    private void showUpdateDialog(String updateContent, final String updateUrl) {
        final StyleAlertDialog warnAlert = new StyleAlertDialog(this, R.style.MyDialog);
        // 没有网络
        warnAlert.setTitleMsg("版本更新");
        warnAlert.setCancelable(true);
        warnAlert.setContent(updateContent);
        warnAlert.setPositiveButton("更新", new OnClickListener() {
            @Override
            public void onClick(View v) {
                warnAlert.dismiss();
                // 下载操作
                Uri uri = Uri.parse(updateUrl);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
        warnAlert.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                warnAlert.dismiss();
            }
        });
        warnAlert.show();
    }

    /**
     * initPush(初始化推送)
     */
    private void initPush() {
        JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
        // 异步设置推送别名
        new Thread(new Runnable() {
            public void run() {
                PushUtil pu = new PushUtil();
                pu.setAlias(MainActivity.this, SharePreManager.getInstance(MainActivity.this)
                        .getUserUCode());
                if (!SharePreManager.getInstance(MainActivity.this).getJpushState()) {
                    JPushInterface.stopPush(MainActivity.this);
                }
            }
        }).start();
    }




//    private UserInfo findUserById(String userId) {
//        UserInfo us = null;
//        for (int i = 0; i < StaticInfo.mutualConcernList.size(); i++) {
//            if (StaticInfo.mutualConcernList.get(i).UCode.replace("-", "").equals(userId)) {
//                ZambiaBean tempBean = StaticInfo.mutualConcernList.get(i);
//                us = new UserInfo(tempBean.UCode.replace("-", ""), tempBean.NickName,
//                        Uri.parse(tempBean.UserHead));
//            }
//        }
//        return us;
//    }

    /**
     * 获取联系人
     */
    private void requestGetFriendByCode() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", SharePreManager.getInstance(this).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetFriendByCode, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        MutualConcernBean mutualBean = new Gson().fromJson(temp,
                                new TypeToken<MutualConcernBean>() {
                                }.getType());
                        if (mutualBean != null && mutualBean.result != null) {
                            if ("200".equals(mutualBean.result.ResultCode)) {
                                StaticInfo.mutualConcernList.add(new ZambiaBean(sp.getUserUCode()
                                        .replace("-", ""), sp.getUserNickName(), sp.getUserHead()));
                                StaticInfo.mutualConcernList.add(new ZambiaBean(
                                        "KEFU1440650315357", "微摄客服",
                                        "http://im.weishoot.com/Content/images/logokf.png"));
                                Set<ZambiaBean> zamSet = new HashSet(StaticInfo.mutualConcernList);
                                zamSet.addAll(mutualBean.data);
                                StaticInfo.mutualConcernList.clear();
                                StaticInfo.mutualConcernList.addAll(new ArrayList(zamSet));
//                                setUserInfoProvider();
                                try {
                                    attenDB.deleteAll(ZambiaBean.class);
                                    attenDB.saveAll(StaticInfo.mutualConcernList);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            } else {
                            }
                        } else {
                        }
                    }
                });

    }

    /**
     * 版本检测
     */
    private void requestGetNewVersion() {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.GetNewVersion, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        // {"resultCode":"200","version":"3.1","url":"http://weishoot.com/DownLoad/weishoot.apk"}
                        try {
                            JSONObject jsonobj = new JSONObject(temp);
                            String resultCode = jsonobj.optString("resultCode");
                            String version = jsonobj.optString("version");
                            String url = jsonobj.optString("url");
                            if ("200".equals(resultCode)) {
                                String locatVersion = StringsUtil.getVersionName(MainActivity.this);
                                if (!locatVersion.equals(version)) {
                                    showUpdateDialog("", url);
                                }
                            } else {
                                // /
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

}
