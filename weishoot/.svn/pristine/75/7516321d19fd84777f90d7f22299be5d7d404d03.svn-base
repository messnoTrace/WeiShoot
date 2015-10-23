
package com.NationalPhotograpy.weishoot.adapter.find;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.shouye.PictureDetailSingleActivity;
import com.NationalPhotograpy.weishoot.bean.PhotoShopTypeBean.PhotoShopBean;
import com.NationalPhotograpy.weishoot.utils.imageloader.core.ImageLoader;

public class DiscoverImgAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater inflater;

    private ImageLoader imageLoader;

    private List<PhotoShopBean> paths = new ArrayList<PhotoShopBean>();

    public DiscoverImgAdapter(Context con) {
        super();
        this.mContext = con;
        this.inflater = LayoutInflater.from(con);
        imageLoader = ImageLoader.getInstance();
    }

    public void setData(List<PhotoShopBean> data) {
        this.paths.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        int i = paths.size() % 13;
        if (i == 11 || i == 12) {
            return (paths.size() / 13 + 1) * 4;
        } else if (i >= 6 && i <= 10) {
            return (paths.size() / 13 + 1) * 4 - 1;
        } else if (i == 5 || i == 4) {
            return (paths.size() / 13 + 1) * 4 - 2;
        } else if (i >= 1 && i <= 3) {
            return (paths.size() / 13 + 1) * 4 - 3;
        } else {
            return (paths.size() / 13) * 4;
        }

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 4 == 0) {
            return 0;
        } else if (position % 4 == 1) {
            return 1;
        } else if (position % 4 == 2) {
            return 2;
        } else {
            return 3;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (getItemViewType(position) == 0) {
            ViewHolder1 holder1;

            if (view == null) {
                view = inflater.inflate(R.layout.item_discover_img1, null);
                holder1 = new ViewHolder1(view);
                view.setTag(holder1);

            } else {
                holder1 = (ViewHolder1) view.getTag();
            }
            setConvertView1(holder1, position);
        } else if (getItemViewType(position) == 1) {
            ViewHolder2 holder2;

            if (view == null) {
                view = inflater.inflate(R.layout.item_discover_img2, null);
                holder2 = new ViewHolder2(view);
                view.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) view.getTag();
            }
            setConvertView2(holder2, position);
        } else if (getItemViewType(position) == 2) {
            ViewHolder3 holder3;

            if (view == null) {
                view = inflater.inflate(R.layout.item_discover_img3, null);
                holder3 = new ViewHolder3(view);
                view.setTag(holder3);
            } else {
                holder3 = (ViewHolder3) view.getTag();
            }
            setConvertView3(holder3, position);

        } else if (getItemViewType(position) == 3) {
            ViewHolder4 holder4;

            if (view == null) {
                view = inflater.inflate(R.layout.item_discover_img4, null);
                holder4 = new ViewHolder4(view);
                view.setTag(holder4);
            } else {
                holder4 = (ViewHolder4) view.getTag();
            }
            setConvertView4(holder4, position);

        }

        return view;
    }

    // 给每套布局控件添加数据
    private void setConvertView1(ViewHolder1 holder1, final int position) {
        Log.v("TAG", "position  " + position);
        holder1.img_pic1_1.setVisibility(View.INVISIBLE);
        holder1.img_pic1_2.setVisibility(View.INVISIBLE);
        holder1.img_pic1_3.setVisibility(View.INVISIBLE);

        if (position / 4 * 13 + 0 < paths.size()) {
            holder1.img_pic1_1.setVisibility(View.VISIBLE);
            String uri_1 = paths.get(position / 4 * 13 + 0).SmallPath;
            imageLoader.displayImage(uri_1, holder1.img_pic1_1);
            holder1.img_pic1_1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 0));
                    mContext.startActivity(it);
                }
            });
        }

        if (position / 4 * 13 + 1 < paths.size()) {
            holder1.img_pic1_2.setVisibility(View.VISIBLE);
            String uri_2 = paths.get(position / 4 * 13 + 1).SmallPath;
            imageLoader.displayImage(uri_2, holder1.img_pic1_2);
            holder1.img_pic1_2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 1));
                    mContext.startActivity(it);
                }
            });
        }

        if (position / 4 * 13 + 2 < paths.size()) {
            holder1.img_pic1_3.setVisibility(View.VISIBLE);
            String uri_3 = paths.get(position / 4 * 13 + 2).SmallPath;
            imageLoader.displayImage(uri_3, holder1.img_pic1_3);
            holder1.img_pic1_3.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 2));
                    mContext.startActivity(it);
                }
            });
        }

    }

    private void setConvertView2(ViewHolder2 holder2, final int position) {
        Log.v("TAG", "position  " + position);
        holder2.img_pic2_1.setVisibility(View.INVISIBLE);
        holder2.img_pic2_2.setVisibility(View.INVISIBLE);
        if (position / 4 * 13 + 3 < paths.size()) {
            holder2.img_pic2_1.setVisibility(View.VISIBLE);
            String uri_1 = paths.get(position / 4 * 13 + 3).SmallPath;
            imageLoader.displayImage(uri_1, holder2.img_pic2_1);
            holder2.img_pic2_1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 3));
                    mContext.startActivity(it);
                }
            });
        }

        if (position / 4 * 13 + 4 < paths.size()) {
            holder2.img_pic2_2.setVisibility(View.VISIBLE);
            String uri_2 = paths.get(position / 4 * 13 + 4).SmallPath;
            imageLoader.displayImage(uri_2, holder2.img_pic2_2);
            holder2.img_pic2_2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 4));
                    mContext.startActivity(it);
                }
            });
        }

    }

    private void setConvertView3(ViewHolder3 holder3, final int position) {
        Log.v("TAG", "position  " + position);
        holder3.img_pic3_1.setVisibility(View.INVISIBLE);
        holder3.img_pic3_2.setVisibility(View.INVISIBLE);
        holder3.img_pic3_3.setVisibility(View.INVISIBLE);
        holder3.img_pic3_4.setVisibility(View.INVISIBLE);
        holder3.img_pic3_5.setVisibility(View.INVISIBLE);
        if (position / 4 * 13 + 5 < paths.size()) {
            holder3.img_pic3_1.setVisibility(View.VISIBLE);
            String uri_1 = paths.get(position / 4 * 13 + 5).SmallPath;
            imageLoader.displayImage(uri_1, holder3.img_pic3_1);
            holder3.img_pic3_1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 5));
                    mContext.startActivity(it);
                }
            });
        }

        if (position / 4 * 13 + 6 < paths.size()) {
            holder3.img_pic3_2.setVisibility(View.VISIBLE);
            String uri_2 = paths.get(position / 4 * 13 + 6).SmallPath;
            imageLoader.displayImage(uri_2, holder3.img_pic3_2);
            holder3.img_pic3_2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 6));
                    mContext.startActivity(it);
                }
            });

        }
        if (position / 4 * 13 + 7 < paths.size()) {
            holder3.img_pic3_3.setVisibility(View.VISIBLE);
            String uri_3 = paths.get(position / 4 * 13 + 7).SmallPath;
            Log.v("TAG", "uri_3  " + uri_3);
            imageLoader.displayImage(uri_3, holder3.img_pic3_3);
            holder3.img_pic3_3.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 7));
                    mContext.startActivity(it);
                }
            });
        }
        if (position / 4 * 13 + 8 < paths.size()) {
            holder3.img_pic3_4.setVisibility(View.VISIBLE);
            String uri_4 = paths.get(position / 4 * 13 + 8).SmallPath;
            imageLoader.displayImage(uri_4, holder3.img_pic3_4);
            holder3.img_pic3_4.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 8));
                    mContext.startActivity(it);
                }
            });
        }
        if (position / 4 * 13 + 9 < paths.size()) {
            holder3.img_pic3_5.setVisibility(View.VISIBLE);
            String uri_5 = paths.get(position / 4 * 13 + 9).SmallPath;
            imageLoader.displayImage(uri_5, holder3.img_pic3_5);
            holder3.img_pic3_5.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 9));
                    mContext.startActivity(it);
                }
            });
        }
    }

    private void setConvertView4(ViewHolder4 holder4, final int position) {
        Log.v("TAG", "position  " + position);
        holder4.img_pic4_1.setVisibility(View.INVISIBLE);
        holder4.img_pic4_2.setVisibility(View.INVISIBLE);
        holder4.img_pic4_3.setVisibility(View.INVISIBLE);
        if (position / 4 * 13 + 10 < paths.size()) {
            holder4.img_pic4_1.setVisibility(View.VISIBLE);
            String uri_1 = paths.get(position / 4 * 13 + 10).SmallPath;
            imageLoader.displayImage(uri_1, holder4.img_pic4_1);
            holder4.img_pic4_1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 10));
                    mContext.startActivity(it);
                }
            });
        }

        if (position / 4 * 13 + 11 < paths.size()) {
            holder4.img_pic4_2.setVisibility(View.VISIBLE);
            String uri_2 = paths.get(position / 4 * 13 + 11).SmallPath;
            imageLoader.displayImage(uri_2, holder4.img_pic4_2);
            holder4.img_pic4_2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 11));
                    mContext.startActivity(it);
                }
            });
        }
        if (position / 4 * 13 + 12 < paths.size()) {
            holder4.img_pic4_3.setVisibility(View.VISIBLE);
            String uri_3 = paths.get(position / 4 * 13 + 12).SmallPath;
            imageLoader.displayImage(uri_3, holder4.img_pic4_3);
            holder4.img_pic4_3.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, PictureDetailSingleActivity.class);
                    it.putExtra("PhotoShopBean", paths.get(position / 4 * 13 + 12));
                    mContext.startActivity(it);
                }
            });
        }
    }

    class ViewHolder1 {
        public ImageView img_pic1_1, img_pic1_2, img_pic1_3;

        public ViewHolder1(View view) {
            img_pic1_1 = (ImageView) view.findViewById(R.id.img_pic1_1);
            img_pic1_2 = (ImageView) view.findViewById(R.id.img_pic1_2);
            img_pic1_3 = (ImageView) view.findViewById(R.id.img_pic1_3);
        }
    }

    class ViewHolder2 {
        public ImageView img_pic2_1, img_pic2_2;

        public ViewHolder2(View view) {
            img_pic2_1 = (ImageView) view.findViewById(R.id.img_pic2_1);
            img_pic2_2 = (ImageView) view.findViewById(R.id.img_pic2_2);
        }
    }

    class ViewHolder3 {
        public ImageView img_pic3_1, img_pic3_2, img_pic3_3, img_pic3_4, img_pic3_5;

        public ViewHolder3(View view) {
            img_pic3_1 = (ImageView) view.findViewById(R.id.img_pic3_1);
            img_pic3_2 = (ImageView) view.findViewById(R.id.img_pic3_2);
            img_pic3_3 = (ImageView) view.findViewById(R.id.img_pic3_3);
            img_pic3_4 = (ImageView) view.findViewById(R.id.img_pic3_4);
            img_pic3_5 = (ImageView) view.findViewById(R.id.img_pic3_5);
        }
    }

    class ViewHolder4 {
        public ImageView img_pic4_1, img_pic4_2, img_pic4_3;

        public ViewHolder4(View view) {
            img_pic4_1 = (ImageView) view.findViewById(R.id.img_pic4_1);
            img_pic4_2 = (ImageView) view.findViewById(R.id.img_pic4_2);
            img_pic4_3 = (ImageView) view.findViewById(R.id.img_pic4_3);
        }
    }
}
