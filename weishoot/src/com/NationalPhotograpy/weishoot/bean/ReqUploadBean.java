
package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.List;

public class ReqUploadBean implements Serializable {

    public String str_Title = "";

    public String str_Mark = "";

    public PicSortBean picSortBean;

    public String str_PicAddress = "";

    public String str_Coordinate = "";

    public String str_IsSale = "";

    public List<String> uploadPic;

    public String str_Cropping = "";

    public String str_CN = "";

    public String str_EN = "";

    public String str_CPId;

    public String TType;

    public String pictorialPic;

    public String cid;

    public ReqUploadBean() {
        super();
    }

    /**
     * 发微摄
     */
    public ReqUploadBean(String TType, String str_Title, String str_Mark, PicSortBean picSortBean,
            String str_PicAddress, String str_Coordinate, String str_IsSale,
            List<String> uploadPic, String cid) {
        super();
        this.TType = TType;
        this.str_Title = str_Title;
        this.str_Mark = str_Mark;
        this.picSortBean = picSortBean;
        this.str_PicAddress = str_PicAddress;
        this.str_Coordinate = str_Coordinate;
        this.str_IsSale = str_IsSale;
        this.uploadPic = uploadPic;
        this.cid = cid;
    }

    /**
     * 发影片
     */
    public ReqUploadBean(String TType, String str_Mark, PicSortBean picSortBean,
            String str_PicAddress, String str_Coordinate, List<String> uploadPic,
            String str_Cropping, String str_CN, String str_EN) {
        super();
        this.TType = TType;
        this.str_Mark = str_Mark;
        this.picSortBean = picSortBean;
        this.str_PicAddress = str_PicAddress;
        this.str_Coordinate = str_Coordinate;
        this.uploadPic = uploadPic;
        this.str_Cropping = str_Cropping;
        this.str_CN = str_CN;
        this.str_EN = str_EN;
    }

    /**
     * 发封面
     */
    public ReqUploadBean(String TType, String str_Mark, PicSortBean picSortBean,
            String str_PicAddress, String str_Coordinate, List<String> uploadPic,
            String pictorialPic, String str_CPId) {
        super();
        this.TType = TType;
        this.str_Mark = str_Mark;
        this.picSortBean = picSortBean;
        this.str_PicAddress = str_PicAddress;
        this.str_Coordinate = str_Coordinate;
        this.uploadPic = uploadPic;
        this.str_CPId = str_CPId;
        this.pictorialPic = pictorialPic;
    }
}
