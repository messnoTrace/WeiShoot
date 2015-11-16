
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
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.bean.PicTemplateChildBean.PicTemplateChild;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ScreenshotPictorialAdapter extends BaseAdapter {

    private Context mContext;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsPic;

    private List<PicTemplateChild> data = new ArrayList<PicTemplateChild>();

    private int picTempLayoutHeight, picTempLayoutWidth;

    public ScreenshotPictorialAdapter(Context mContext, List<PicTemplateChild> data, int viewWidth) {
        this.mContext = mContext;
        this.data.addAll(data);
        this.imageLoader = ImageLoader.getInstance();
        mOptionsPic = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .showImageForEmptyUri(R.drawable.ic_stub).build();
        picTempLayoutHeight = viewWidth;
        picTempLayoutWidth = (int) (picTempLayoutHeight / Constant.PICTORIAL_SIZE);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_screenshot_pictorial,
                    null);
            holder = new ViewHolder();
            holder.layout_picTemp = (LinearLayout) convertView.findViewById(R.id.layout_picTemp);
            holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_sale = (TextView) convertView.findViewById(R.id.tv_sale);

            LinearLayout.LayoutParams lyp1 = new LinearLayout.LayoutParams(picTempLayoutWidth,
                    picTempLayoutHeight);
            int tempHeight = (int) (picTempLayoutHeight - DimensionPixelUtil.getDimensionPixelSize(
                    1, 30, mContext));
            int tempWidth = (int) (tempHeight / Constant.PICTORIAL_SIZE);
            LinearLayout.LayoutParams lyp2 = new LinearLayout.LayoutParams(tempWidth, tempHeight);
            holder.layout_picTemp.setLayoutParams(lyp1);
            holder.iv_pic.setLayoutParams(lyp2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(data.get(position).PicUrl, holder.iv_pic, mOptionsPic);
        holder.tv_name.setText(data.get(position).PicName);
        if ("0.00".equals(data.get(position).PicPrice)) {
            holder.tv_sale.setText("免费");
        } else {
            holder.tv_sale.setText(data.get(position).PicPrice);
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout layout_picTemp;

        ImageView iv_pic;

        TextView tv_name, tv_sale;
    }
}
