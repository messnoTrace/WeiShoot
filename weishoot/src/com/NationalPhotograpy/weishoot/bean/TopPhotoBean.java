
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;

public class TopPhotoBean implements Serializable {
    public String PCode;

    public String ImgName;

    public String SortOrder;

    public String IsSale;

    public String IsBuyed;

    public TopPhotoBean() {
        super();
    }

    public TopPhotoBean(String pCode, String imgName) {
        super();
        PCode = pCode;
        ImgName = imgName;
    }
}
