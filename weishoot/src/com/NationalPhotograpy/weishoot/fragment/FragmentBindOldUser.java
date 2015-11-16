
package com.NationalPhotograpy.weishoot.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.ClickUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class FragmentBindOldUser extends Fragment {
    private EditText edt_username, edt_password;

    private Button btn_bind;

    private ProgressiveDialog dialog;

    private String thirdId = "";

    private String cate = "";

    private String accesscode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View createView = inflater.inflate(R.layout.fragment_bind_old_user, null, false);

        initView(createView);
        initData();
        setListener();

        return createView;
    }

    private void initView(View createView) {
        dialog = new ProgressiveDialog(getActivity());
        edt_username = (EditText) createView.findViewById(R.id.edt_username);
        edt_password = (EditText) createView.findViewById(R.id.edt_password);
        btn_bind = (Button) createView.findViewById(R.id.btn_bind);
    }

    private void initData() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            thirdId = bundle.getString("id");
            accesscode = bundle.getString("accesscode");
            if ("qq".equals(bundle.getString("flag"))) {
                cate = "1";
            } else if ("wechat".equals(bundle.getString("flag"))) {
                cate = "2";
            } else if ("weibo".equals(bundle.getString("flag"))) {
                cate = "0";
            }
        }

    }

    private void setListener() {
        btn_bind.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                String name = edt_username.getText().toString();
                String password = edt_password.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    WeiShootToast.makeErrorText(getActivity(), "用户名不能为空",
                            WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    WeiShootToast.makeText(getActivity(), "密码不能为空", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }

                requestLogin(name, password);

            }

        });
    }

    private void requestLogin(final String user, final String password) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("loginName", user);
        params.addBodyParameter("Passwords", password);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.CheckLoin, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                if (getActivity() == null) {
                    return;
                }
                dialog.dismiss();
                WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                if (getActivity() == null) {
                    return;
                }
                dialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {

                            WeiShootToast.makeText(getActivity(), "登录成功",
                                    WeiShootToast.LENGTH_SHORT).show();
                            JSONObject object = jsonObject.getJSONObject("data");
                            requestBindUser(object.optString("UCode"));

                            UserInfo.getInstance(getActivity()).clearUserInfo();

                            UserInfo.getInstance(getActivity()).setUserUCode(
                                    object.optString("UCode"));
                            UserInfo.getInstance(getActivity()).setUserHead(
                                    object.optString("UserHead"));
                            UserInfo.getInstance(getActivity()).setUserNickName(
                                    object.optString("NickName"));
                            UserInfo.getInstance(getActivity()).setUserSex(object.optString("Sex"));
                            UserInfo.getInstance(getActivity()).setUserIntroduction(
                                    object.optString("Introduction"));
                            UserInfo.getInstance(getActivity()).setUserTelephone(
                                    object.optString("Telephone"));
                            UserInfo.getInstance(getActivity()).setUserPassword(password);
                            UserInfo.getInstance(getActivity()).setUserLoginName(user);
                        } else {
                            WeiShootToast.makeErrorText(getActivity(),
                                    jObject.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout),
                            WeiShootToast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void requestBindUser(String uCode) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("UCode", uCode);
        params.addBodyParameter("ThirdId", thirdId);
        params.addBodyParameter("AccessCode", accesscode);
        params.addBodyParameter("Cate", cate);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        Log.v("----", "=======" + uCode + "  thirdId  " + thirdId + " accesscode " + accesscode
                + " cate " + cate);

        httpUtils.send(HttpMethod.POST, HttpUrl.BindUser, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                if (getActivity() == null) {
                    return;
                }
                dialog.dismiss();
                WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                if (getActivity() == null) {
                    return;
                }
                dialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {

                            WeiShootToast.makeText(getActivity(), "绑定成功",
                                    WeiShootToast.LENGTH_SHORT).show();
                            JSONObject object = jsonObject.getJSONObject("data");
                            StaticInfo.OnceFlag = false;
                            UserInfo.getInstance(getActivity()).setUserLoginStatus(true);

                            getActivity().finish();
                        } else {
                            WeiShootToast.makeErrorText(getActivity(),
                                    jObject.optString("ResultMsg"), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout),
                            WeiShootToast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
