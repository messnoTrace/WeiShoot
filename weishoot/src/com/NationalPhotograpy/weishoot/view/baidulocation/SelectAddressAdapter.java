
package com.NationalPhotograpy.weishoot.view.baidulocation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.baidu.mapapi.search.core.PoiInfo;

public class SelectAddressAdapter extends BaseAdapter {

    private Context mContext;

    private List<PoiInfo> poiInfoList = new ArrayList<PoiInfo>();

    private String addressName;

    public SelectAddressAdapter(Context context, String addressName) {
        this.mContext = context;
        this.addressName = addressName;
    }

    public void setData(List<PoiInfo> arraylist) {
        this.poiInfoList.clear();
        this.poiInfoList.addAll(arraylist);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return poiInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return poiInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_address, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.iv_ok = (ImageView) convertView.findViewById(R.id.iv_ok);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(poiInfoList.get(position).name);
        holder.tv_address.setText(poiInfoList.get(position).address);
        if (addressName.equals(poiInfoList.get(position).name)) {
            holder.iv_ok.setVisibility(View.VISIBLE);
        } else {
            holder.iv_ok.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_address;

        ImageView iv_ok;
    }

}
