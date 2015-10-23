
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProvinceBean implements Serializable {

    public List<CityBean> CityBean = new ArrayList<CityBean>();

    public String id = "";

    public String name = "";
}
