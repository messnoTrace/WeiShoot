
package com.NationalPhotograpy.weishoot.view.baidulocation;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.baidu.mapapi.search.core.PoiInfo;

public class AroundPoiAdapter extends BaseAdapter {
    private Context mContext;

    private List<PoiInfo> mkPoiInfoList;

    private int selected = -1;

    public AroundPoiAdapter(Context context, List<PoiInfo> list) {
        this.mContext = context;
        this.mkPoiInfoList = list;
    }

    @Override
    public int getCount() {
        return mkPoiInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mkPoiInfoList != null) {
            return mkPoiInfoList.get(position);
        }
        return null;
    }

    public void setNewList(List<PoiInfo> list) {
        this.mkPoiInfoList = list;
        this.notifyDataSetChanged();
    }

    public void setNewList(int index) {
        this.selected = index;
        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class RecordHolder {
        public RelativeLayout rlMLPIItem;

        public ImageView ivMLISelected;

        public TextView tvMLIPoiName, tvMLIPoiAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder = null;
        if (convertView == null) {
            holder = new RecordHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_select_address, null);
            holder.ivMLISelected = (ImageView) convertView.findViewById(R.id.iv_ok);
            holder.tvMLIPoiName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvMLIPoiAddress = (TextView) convertView.findViewById(R.id.tv_address);
            holder.rlMLPIItem = (RelativeLayout) convertView.findViewById(R.id.rlMLPIItem);
            convertView.setTag(holder);
        } else {
            holder = (RecordHolder) convertView.getTag();
        }
        holder.tvMLIPoiName.setText(mkPoiInfoList.get(position).name);
        holder.tvMLIPoiAddress.setText(mkPoiInfoList.get(position).address);
        if (selected >= 0 && selected == position) {
            holder.ivMLISelected.setVisibility(View.VISIBLE);
        } else {
            holder.ivMLISelected.setVisibility(View.GONE);
        }
        return convertView;
    }
}
