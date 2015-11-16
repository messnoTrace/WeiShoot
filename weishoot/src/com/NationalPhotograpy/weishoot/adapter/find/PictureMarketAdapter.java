
package com.NationalPhotograpy.weishoot.adapter.find;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.find.DiscoverImgActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.PictureDetailSingleActivity;
import com.NationalPhotograpy.weishoot.bean.PhotoShopTypeBean.PhotoShopBean;
import com.NationalPhotograpy.weishoot.bean.PhotoShopTypeBean.PhotoShopType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PictureMarketAdapter extends BaseAdapter {

    private Context mContext;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsPic;

    private List<PhotoShopType> data = new ArrayList<PhotoShopType>();

    public PictureMarketAdapter(Context context, List<PhotoShopType> data) {
        this.mContext = context;
        this.data.addAll(data);
        this.imageLoader = ImageLoader.getInstance();
        mOptionsPic = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .showImageForEmptyUri(R.drawable.ic_stub).build();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pic_market, null);
            holder = new ViewHolder();
            holder.tv_TuClass = (TextView) convertView.findViewById(R.id.tv_TuClass);
            holder.btn_more = (Button) convertView.findViewById(R.id.btn_more);
            holder.iv01 = (ImageView) convertView.findViewById(R.id.iv01);
            holder.iv02 = (ImageView) convertView.findViewById(R.id.iv02);
            holder.iv03 = (ImageView) convertView.findViewById(R.id.iv03);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_TuClass.setText(data.get(position).TuClass);
        imageLoader.displayImage(data.get(position).PhotoList.get(0).SmallPath, holder.iv01,
                mOptionsPic);
        imageLoader.displayImage(data.get(position).PhotoList.get(1).SmallPath, holder.iv02,
                mOptionsPic);
        imageLoader.displayImage(data.get(position).PhotoList.get(2).SmallPath, holder.iv03,
                mOptionsPic);
        setPicListener(holder, data.get(position).PhotoList);
        holder.btn_more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, DiscoverImgActivity.class);
                it.putExtra("fId", data.get(position).Id);
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_TuClass;

        Button btn_more;

        ImageView iv01, iv02, iv03;
    }

    private void setPicListener(ViewHolder holder, final List<PhotoShopBean> PhotoList) {
        holder.iv01.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                it.putExtra("PhotoShopBean", PhotoList.get(0));
                mContext.startActivity(it);
            }
        });
        holder.iv02.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                it.putExtra("PhotoShopBean", PhotoList.get(1));
                mContext.startActivity(it);
            }
        });
        holder.iv03.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                it.putExtra("PhotoShopBean", PhotoList.get(2));
                mContext.startActivity(it);
            }
        });
    }
}
