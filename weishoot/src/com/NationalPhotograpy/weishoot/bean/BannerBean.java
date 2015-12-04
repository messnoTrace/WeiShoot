package com.NationalPhotograpy.weishoot.bean;

import java.io.Serializable;
import java.util.List;

public class BannerBean {

	public ResultStatus result;

	public List<Banner> data;

	public class Banner implements Serializable {

		public String ID;
		public String imgDesc;
		public String ImgUrl;
		public String PicHeight;
		public String PicWidth;
		public String Title;
		public String Ucode;
		public String Url;
		public String Tcode;
		public String Rcmd;

	}

}
