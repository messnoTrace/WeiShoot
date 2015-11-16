
package com.NationalPhotograpy.weishoot.fragment;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.photograph.SelectPicActivity;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class FragmentRegisteredNewUser extends Fragment implements OnClickListener {

    private ImageButton ibtn_edit_head, ibtn_sex_man, ibtn_sex_woman;

    private Button btn_registered;

    private EditText edt_username, edt_password, edt_password_again;

    private TextView tv_user_sex;

    private String thirdId = "";

    private String name = "";

    private String icon = "";

    private String accesscode = "";

    private String cate = "";

    private ProgressiveDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View createView = inflater.inflate(R.layout.fragment_registered_new_user, null, false);

        initView(createView);
        initData();
        setListener();

        return createView;
    }

    private void initView(View createView) {
        dialog = new ProgressiveDialog(getActivity());
        ibtn_edit_head = (ImageButton) createView.findViewById(R.id.ibtn_edit_head);
        ibtn_sex_man = (ImageButton) createView.findViewById(R.id.ibtn_sex_man);
        ibtn_sex_woman = (ImageButton) createView.findViewById(R.id.ibtn_sex_woman);
        btn_registered = (Button) createView.findViewById(R.id.btn_registered);
        edt_username = (EditText) createView.findViewById(R.id.edt_username);
        edt_password = (EditText) createView.findViewById(R.id.edt_password);
        edt_password_again = (EditText) createView.findViewById(R.id.edt_password_again);
        tv_user_sex = (TextView) createView.findViewById(R.id.tv_user_sex);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            thirdId = bundle.getString("id");
            accesscode = bundle.getString("accesscode");
            name = bundle.getString("name");
            icon = bundle.getString("icon");
            if ("qq".equals(bundle.getString("flag"))) {
                cate = "1";
            } else if ("wechat".equals(bundle.getString("flag"))) {
                cate = "2";
            } else if ("weibo".equals(bundle.getString("flag"))) {
                cate = "0";
            }
        }

        if (!TextUtils.isEmpty(name)) {
            edt_username.setText(name);
        }

        ImageLoader.getInstance().displayImage(
                icon,
                ibtn_edit_head,
                new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                        .displayer(new RoundedBitmapDisplayer(200))
                        .showImageForEmptyUri(R.drawable.default_head).build());

    }

    private void setListener() {
        // ibtn_edit_head.setOnClickListener(this);
        ibtn_sex_man.setOnClickListener(this);
        ibtn_sex_woman.setOnClickListener(this);
        btn_registered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.ibtn_edit_head:
                // ToDo相机或者相册
                it = new Intent(getActivity(), SelectPicActivity.class);
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
            case R.id.btn_registered:
                // 上传资料信息
                String sex = tv_user_sex.getText().toString();
                String name = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                String passwordAgain = edt_password_again.getText().toString();
                if ("请选择性别".equals(sex)) {
                    WeiShootToast.makeErrorText(getActivity(), "请选择性别", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    WeiShootToast.makeErrorText(getActivity(), "请输入昵称", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    WeiShootToast.makeErrorText(getActivity(), "请输入密码", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(passwordAgain)) {
                    WeiShootToast.makeErrorText(getActivity(), "请确认密码", WeiShootToast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!password.equals(passwordAgain)) {
                    WeiShootToast.makeErrorText(getActivity(), "密码和确认密码不一致",
                            WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                requestRegist(name, password, sex);
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    public void requestRegist(String name, String password, String sex) {
        dialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("NickName", name);
        params.addBodyParameter("Passwords", password);
        params.addBodyParameter("Sex", sex);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.UserRegist, params, new RequestCallBack<Object>() {

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
                Log.v("-----", "--------<" + temp);
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {
                            JSONObject object = jsonObject.getJSONObject("data");

                            UserInfo.getInstance(getActivity()).setUserUCode(
                                    object.optString("UCode"));
                            UserInfo.getInstance(getActivity()).setUserNickName(
                                    object.optString("NickName"));
                            requestBindUser(object.optString("UCode"));
                            requestAddTopic(icon);

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

    /**
     * 上传头像
     */
    private void requestAddTopic(final String picUri) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(getActivity()).getUserUCode());
        params.addBodyParameter("picUri", new File(picUri));
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.ModifyAvatar, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        if (getActivity() == null) {
                            return;
                        }
                        WeiShootToast.makeErrorText(getActivity(),
                                getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        if (getActivity() == null) {
                            return;
                        }
                        String temp = (String) objectResponseInfo.result;
                        String resultCode = "-1";
                        String resultMsg = "";
                        if (temp != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(temp);
                                JSONObject resultJson = jsonObj.getJSONObject("result");
                                resultCode = resultJson.optString("ResultCode");
                                resultMsg = resultJson.optString("ResultMsg");
                                if ("200".equals(resultCode)) {
                                    SharePreManager.getInstance(getActivity())
                                            .setUserHeadTimestamp(StringsUtil.getTimeFormat());
                                    // Intent intent = new Intent();
                                    // intent.putExtra("picUri", picUri);
                                    // setResult(Constant.RESULT_HEAD, intent);
                                    // finish();
                                } else {
                                    WeiShootToast.makeErrorText(getActivity(), resultMsg,
                                            WeiShootToast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            WeiShootToast.makeErrorText(getActivity(),
                                    getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });

    }

}
