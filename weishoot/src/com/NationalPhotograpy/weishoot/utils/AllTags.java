
package com.NationalPhotograpy.weishoot.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseApplication;
import com.NationalPhotograpy.weishoot.bean.CommonBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AllTags {
    private static final String TAG = "AllTags";

    public static void getAllTagForNet(final Context context) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);

        httpUtils.send(HttpMethod.POST, HttpUrl.GetAllTag, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String temp = (String) objectResponseInfo.result;
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject jsonObject = new JSONObject(temp);
                        JSONObject jObject = jsonObject.getJSONObject("result");
                        if ("200".equals(jObject.optString("ResultCode"))) {
                            SharePreManager.getInstance(context).setAllTag(temp);

                        } else {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }

            }
        });

    }
}
