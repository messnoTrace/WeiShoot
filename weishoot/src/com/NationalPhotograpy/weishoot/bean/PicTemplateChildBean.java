
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.List;

public class PicTemplateChildBean {

    public ResultStatus result;

    public List<PicTemplateChild> data;

    public class PicTemplateChild implements Serializable {
        public String CPId;

        public String PicName;

        public String PicUrl;

        public String PicUrlpng;

        public String PicPrice;

        public String MakeCount;

        // public class MakeInfo;

        public String CreateDate;
    }
}
