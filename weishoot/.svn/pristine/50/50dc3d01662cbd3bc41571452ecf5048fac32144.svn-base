
package com.NationalPhotograpy.weishoot.adapter.shouye;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.sharesdk.framework.i;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.bean.OcBean.OcBeans;

public class OcAdapter extends BaseAdapter {
    private Context mContext;

    private List<OcBeans> data;

    private ItemClickListener listener;

    public OcAdapter(Context mContext, List<OcBeans> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public void setOnClikListener(ItemClickListener listener) {
        this.listener = listener;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_textview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(data.get(position).Name);

        final int i = position;
        holder.tv_name.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.click(data.get(i).Name, data.get(i).Id);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView tv_name;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.item_tv);
        }

    }

    public interface ItemClickListener {
        void click(String name, String id);
    }

}
