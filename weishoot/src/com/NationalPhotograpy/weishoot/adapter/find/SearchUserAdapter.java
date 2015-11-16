
package com.NationalPhotograpy.weishoot.adapter.find;

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
import com.NationalPhotograpy.weishoot.bean.ZambiaBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SearchUserAdapter extends BaseAdapter {

    private Context mContext;

    private List<ZambiaBean> searchUser = new ArrayList<ZambiaBean>();

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsHead;

    public SearchUserAdapter(Context mContext, List<ZambiaBean> searchUser) {
        this.mContext = mContext;
        this.searchUser = searchUser;
        this.imageLoader = ImageLoader.getInstance();
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
    }

    @Override
    public int getCount() {
        return searchUser.size();
    }

    @Override
    public Object getItem(int position) {
        return searchUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_head, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(searchUser.get(position).NickName);
        imageLoader.displayImage(searchUser.get(position).UserHead, holder.iv_head, mOptionsHead);
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;

        ImageView iv_head;
    }
}
