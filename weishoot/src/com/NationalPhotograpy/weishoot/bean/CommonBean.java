
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonBean implements Serializable {

    public String resultCode;

    public String resultMsg;

    public String PersonCount;

    public String OtherCount;

    public CommonBean toJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject resultJsonObject = jsonObject.getJSONObject("result");
            resultCode = resultJsonObject.optString("ResultCode");
            resultMsg = resultJsonObject.optString("ResultMsg");
            JSONObject dataJsonObject = jsonObject.getJSONObject("data");
            PersonCount = dataJsonObject.optString("PersonCount");
            OtherCount = dataJsonObject.optString("OtherCount");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return this;
    }
}
