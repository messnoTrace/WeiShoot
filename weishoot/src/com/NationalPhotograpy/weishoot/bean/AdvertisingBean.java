
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.List;

public class AdvertisingBean implements Serializable {

    public ResultStatus result;

    public List<ADBean> data;

    public class ADBean implements Serializable {
        public String ADName;

        public String ALink;

        public String AClass;

        public String StartTime;

        public String EndTime;

        public String ImgName;

        public String CreateDate;
    }
}
