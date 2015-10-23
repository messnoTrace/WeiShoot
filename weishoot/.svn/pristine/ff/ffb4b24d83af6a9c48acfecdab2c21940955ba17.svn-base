
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class PayBean implements Serializable {

    public String resultCode;

    public String resultMsg;

    public PayBean toJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject resultJsonObject = jsonObject.getJSONObject("result");
            resultCode = resultJsonObject.optString("ResultCode");
            resultMsg = resultJsonObject.optString("ResultMsg");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return this;
    }
}
