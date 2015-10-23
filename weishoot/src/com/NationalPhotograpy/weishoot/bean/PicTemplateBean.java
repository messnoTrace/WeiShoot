
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.List;

public class PicTemplateBean {
    public ResultStatus result;

    public List<PicTemplate> data;

    public class PicTemplate implements Serializable {
        public String CId;

        public String CoverName;

        public String MobileCoverIco;

        public String Sort;
    }
}
