
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class BuyImageBean implements Serializable {

    public String resultCode;

    public String resultMsg;

    public String picAddress;

    public BuyImageBean toJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject resultJsonObject = jsonObject.getJSONObject("result");
            resultCode = resultJsonObject.optString("ResultCode");
            resultMsg = resultJsonObject.optString("ResultMsg");
            JSONObject dataJsonObject = jsonObject.getJSONObject("data");
            picAddress = dataJsonObject.optString("picAddress");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return this;
    }
}
