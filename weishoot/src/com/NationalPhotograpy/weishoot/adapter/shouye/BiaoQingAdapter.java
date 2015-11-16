
package com.NationalPhotograpy.weishoot.adapter.shouye;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.Dailyfood.meirishejian.R;

public class BiaoQingAdapter extends BaseAdapter {

    private Context mContext;

    private List<String> biaoqingList;

    public BiaoQingAdapter(Context mContext, List<String> biaoqingList) {
        this.mContext = mContext;
        this.biaoqingList = biaoqingList;
    }

    @Override
    public int getCount() {
        return biaoqingList.size();
    }

    @Override
    public Object getItem(int position) {
        return biaoqingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_biaoqing, null);
            holder = new ViewHolder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageview.setBackgroundResource(mContext.getResources().getIdentifier(
                biaoqingList.get(position), "drawable", "com.Dailyfood.meirishejian"));
        return convertView;
    }

    class ViewHolder {
        ImageView imageview;
    }
}
