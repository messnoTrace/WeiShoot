
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.List;

import com.NationalPhotograpy.weishoot.bean.PhotoShopTypeBean.PhotoShopBean;

public class PictureWallBean implements Serializable {

    public ResultStatus result;

    public List<PhotoShopBean> data;
}
