
package com.NationalPhotograpy.weishoot.view.adcyclegallery;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.NationalPhotograpy.weishoot.bean.AdvertisingBean.ADBean;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 首页轮循图片适配器
 * 
 * @author wuchx
 * @date 2013-12-5 下午5:04:33
 */
public class HomeAdCycleGalleryAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<ADBean> adList = new ArrayList<ADBean>();

    private int screenWidth;

    private String path = "";

    /**
     * 下载图片的
     */
    protected ImageLoader imageLoader;

    public HomeAdCycleGalleryAdapter(Context context, List<ADBean> data, int screenWidth) {
        this.adList.addAll(data);
        this.mContext = context;
        this.imageLoader = ImageLoader.getInstance();
        this.screenWidth = screenWidth;
    }

    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public Object getItem(int position) {
        return adList.get(position % adList.size());
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = new ImageView(mContext.getApplicationContext());
        }
        ImageView imgview = (ImageView) convertView;
        imgview.setScaleType(ImageView.ScaleType.FIT_XY);
        imgview.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        String url = adList.get(position % adList.size()).ImgName;
        if (!"".equals(url))
            imageLoader.displayImage(url, imgview);

        return imgview;
    }
}
