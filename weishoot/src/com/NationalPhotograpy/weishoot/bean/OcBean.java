
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.List;

public class OcBean implements Serializable {

    public ResultStatus result;

    public List<OcBeans> data;

    public class OcBeans implements Serializable {
        public String Id;

        public String Name;

    }
}
