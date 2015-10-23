
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class ZambiaBean implements Serializable {

    public ZambiaBean() {

    }

    public ZambiaBean(String UCode, String NickName, String UserHead) {
        this.UCode = UCode;
        this.NickName = NickName;
        this.UserHead = UserHead;
    }

    public int id;

    public String IsAttention = "0";

    public String UserHead = "";

    public String UCode = "";

    public String Introduction = "";

    public String NickName = "";

    public String PCode = "";

    public String FCode = "";

    public String Createdate = "";

    public String WLevel = "";

    public ZambiaBean toJson(JSONObject object) {

        this.IsAttention = object.optString("IsAttention");
        this.UserHead = object.optString("UserHead");
        this.UCode = object.optString("UCode");
        this.Introduction = object.optString("Introduction");
        this.NickName = object.optString("NickName");
        this.PCode = object.optString("PCode");
        this.FCode = object.optString("FCode");
        this.Createdate = object.optString("Createdate");
        this.WLevel = object.optString("WLevel");

        return this;
    }

    @Override
    public int hashCode() {
        return this.UCode.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ZambiaBean) {
            ZambiaBean user = (ZambiaBean) obj;
            return this.UCode.equals(user.UCode);
        }
        return super.equals(obj);
    }
}
