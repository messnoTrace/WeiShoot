
package com.NationalPhotograpy.weishoot.adapter.photograph;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.bean.PicTemplateBean.PicTemplate;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SelectPicTemplateAdapter extends BaseAdapter {
    private Context mContext;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsPic;

    private List<PicTemplate> data = new ArrayList<PicTemplate>();

    private int picTemplateHeight;

    public SelectPicTemplateAdapter(Context mContext, List<PicTemplate> data) {
        this.mContext = mContext;
        this.data.addAll(data);
        this.imageLoader = ImageLoader.getInstance();
        mOptionsPic = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .showImageForEmptyUri(R.drawable.ic_stub).build();
        picTemplateHeight = (int) (StaticInfo.widthPixels * Constant.PICTORIAL_TEMPLATE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_pictemp, null);
            holder = new ViewHolder();
            holder.iv_template = (ImageView) convertView.findViewById(R.id.iv_template);

            LinearLayout.LayoutParams lyp = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT, picTemplateHeight);
            holder.iv_template.setLayoutParams(lyp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        imageLoader
                .displayImage(data.get(position).MobileCoverIco, holder.iv_template, mOptionsPic);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_template;
    }
}
