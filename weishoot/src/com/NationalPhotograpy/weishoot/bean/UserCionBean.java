
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserCionBean implements Serializable {

    public String resultCode;

    public String resultMsg;

    public String Coin;

    public String Fee;

    public UserCionBean toJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject resultJsonObject = jsonObject.getJSONObject("result");
            resultCode = resultJsonObject.optString("ResultCode");
            resultMsg = resultJsonObject.optString("ResultMsg");
            JSONObject dataJsonObject = jsonObject.getJSONObject("data");
            Coin = dataJsonObject.optString("Coin");
            Fee = dataJsonObject.optString("Fee");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return this;
    }
}
