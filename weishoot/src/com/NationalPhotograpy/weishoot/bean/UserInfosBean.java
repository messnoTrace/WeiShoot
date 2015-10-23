
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;

public class UserInfosBean implements Serializable {

    public ResultStatus result;

    public UserInfos data;

    public class UserInfos implements Serializable {
        public String UCode;

        public String NickName;

        public String UserHead;

        public String Sex;

        public String TrueName;

        public String Birthday;

        public String Telephone;

        public String City;

        public String Introduction;

        public String OcName;

        public String FansCount;

        public String AttentionCount;

        public String IsAttention;

        public String Cover;
        
        public String Passwords;
    }
}
