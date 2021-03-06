
package com.NationalPhotograpy.weishoot.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.registered.LoginActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.CommentListActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.CreateCommentActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.PictureDetailActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.ReportActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.ZambiaActivity;
import com.NationalPhotograpy.weishoot.bean.ShareBean;
import com.NationalPhotograpy.weishoot.bean.TopCommentBean;
import com.NationalPhotograpy.weishoot.bean.TopGoodBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean.TopicData;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.ImageLoader.Type;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.EditUserInfoDialog;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.SharePopupWindow;
import com.NationalPhotograpy.weishoot.view.baidulocation.ShowAddressActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HomeAdapter extends BaseAdapter {

    private Context mContext;

    private ImageLoader imageLoader;

    private com.NationalPhotograpy.weishoot.utils.ImageLoader imageloaderLocat;

    private DisplayImageOptions mOptionsHead, mOptionsPic;

    public List<TopicData> data = new ArrayList<TopicData>();

    public List<Integer> indexZans = new ArrayList<Integer>();

    public List<Integer> indexShouCangs = new ArrayList<Integer>();

    private int zanNum;

    private ProgressiveDialog progressDialog;

    private ListView lv;

    private SharePopupWindow showShareWindow;

    public HomeAdapter(Context context, List<TopicData> data, ListView lv) {
        this.mContext = context;
        this.lv = lv;
        this.imageLoader = ImageLoader.getInstance();
        this.imageloaderLocat = com.NationalPhotograpy.weishoot.utils.ImageLoader.getInstance(3,
                Type.LIFO);
        if (data != null) {
            this.data.addAll(data);
        }
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
        mOptionsPic = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .showImageForEmptyUri(R.drawable.ic_stub).build();
        progressDialog = new ProgressiveDialog(context);

        showShareWindow = new SharePopupWindow(mContext);
    }

    public void addData(TopicData data) {
        for (int i = 0; i < this.data.size(); i++) {
            if ("-1".equals(this.data.get(i).TCode)) {
                this.data.remove(i);
                break;
            }
        }
        this.data.add(0, data);
        notifyDataSetChanged();
        lv.setSelection(0);
    }

    public void addData(List<TopicData> data, TopicData tempTopicBean, TopicData resultTopicBean,
            boolean flag) {
        if (this.data.size() == 0) {
            boolean isExist = false;
            if (resultTopicBean != null) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).TCode.equals(resultTopicBean.TCode)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    this.data.add(0, resultTopicBean);
                }
            } else if (!flag) {
                this.data.add(tempTopicBean);
            }
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void deleteData() {
        for (int i = 0; i < this.data.size(); i++) {
            if ("-1".equals(this.data.get(i).TCode)) {
                this.data.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home, null);
            holder = new ViewHolder();
            holder.view_gray = (RelativeLayout) convertView.findViewById(R.id.view_gray);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.iv_my_head = (ImageView) convertView.findViewById(R.id.iv_my_head);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_from = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_addressNum = (TextView) convertView.findViewById(R.id.tv_addressNum);
            holder.tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            holder.layout_pic = (LinearLayout) convertView.findViewById(R.id.layout_pic);
            holder.layout_shoucang = (LinearLayout) convertView.findViewById(R.id.layout_shoucang);
            holder.layout_btn_comment = (LinearLayout) convertView
                    .findViewById(R.id.layout_btn_comment);
            holder.layout_headImage = (LinearLayout) convertView
                    .findViewById(R.id.layout_headImage);
            holder.layout_comment = (LinearLayout) convertView.findViewById(R.id.layout_comment);
            holder.btn_allComment = (Button) convertView.findViewById(R.id.btn_allComment);
            holder.layout_address = (RelativeLayout) convertView.findViewById(R.id.layout_address);
            holder.layout_head = (RelativeLayout) convertView.findViewById(R.id.layout_head);
            holder.iv_headNum = (TextView) convertView.findViewById(R.id.iv_headNum);
            holder.layout_zan = (LinearLayout) convertView.findViewById(R.id.layout_zan);
            holder.layout_delete = (LinearLayout) convertView.findViewById(R.id.layout_delete);
            holder.layout_report = (LinearLayout) convertView.findViewById(R.id.layout_report);
            holder.layout_share = (LinearLayout) convertView.findViewById(R.id.layout_share);
            holder.tv_zan = (TextView) convertView.findViewById(R.id.tv_zan);
            holder.cb_zan = (CheckBox) convertView.findViewById(R.id.cb_zan);
            holder.cb_shoucang = (CheckBox) convertView.findViewById(R.id.cb_shoucang);
            holder.tv_shoucang = (TextView) convertView.findViewById(R.id.tv_shoucang);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("-1".equals(data.get(position).TCode)) {
            holder.view_gray.setVisibility(View.VISIBLE);
        } else {
            holder.view_gray.setVisibility(View.GONE);
        }
        imageLoader.displayImage(data.get(position).UserHead, holder.iv_head, mOptionsHead);
        imageLoader.displayImage(UserInfo.getInstance(mContext).getUserHead(), holder.iv_my_head,
                mOptionsHead);
        holder.tv_name.setText(data.get(position).NickName);
        holder.tv_time.setText(StringsUtil.getTimeFormat(data.get(position).CreateDate));
        holder.tv_from.setText("来自于" + data.get(position).CreateFrom);
        if (data.get(position).Title != null && data.get(position).Title.length() > 0) {
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.tv_content.setText(StringsUtil.strToSmiley(mContext, data.get(position).Title));
        } else {
            holder.tv_content.setVisibility(View.GONE);
        }
        if (data.get(position).PicAddress != null && data.get(position).PicAddress.length() > 0) {
            holder.layout_address.setVisibility(View.VISIBLE);
            holder.tv_address.setText(data.get(position).PicAddress);
            holder.tv_addressNum.setText(data.get(position).Coordinate);
        } else {
            holder.layout_address.setVisibility(View.GONE);
        }
        setLayoutPicture(holder.layout_pic, data.get(position));
        if (data.get(position).GoodCount > 0) {
            zanNum = data.get(position).GoodCount;
            holder.layout_head.setVisibility(View.VISIBLE);
            holder.iv_headNum.setText(zanNum + "");
            setLayoutHeadImage(holder.layout_headImage, data.get(position).GoodList);
        } else {
            holder.layout_head.setVisibility(View.GONE);
        }
        if (data.get(position).CommentCount > 0) {
            holder.layout_comment.setVisibility(View.VISIBLE);
            setLayoutComment(holder.layout_comment, data.get(position).CommentList, position);
        } else {
            holder.layout_comment.setVisibility(View.GONE);
        }
        if (data.get(position).CommentCount > 5) {
            holder.btn_allComment.setVisibility(View.VISIBLE);
            holder.btn_allComment.setText("查看全部" + data.get(position).CommentCount + "条评论");
        } else {
            holder.btn_allComment.setVisibility(View.GONE);
        }
        if (data.get(position).Mark != null) {
            holder.tv_mark.setText(data.get(position).Mark.replace(",", " "));
        }
        holder.iv_head.setTag(position);

        holder.iv_my_head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, UserInfo.getInstance(mContext)
                        .getUserUCode());
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, UserInfo.getInstance(mContext)
                        .getUserNickName());
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, UserInfo.getInstance(mContext)
                        .getUserHead());
                mContext.startActivity(it);
            }
        });
        holder.layout_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if ("-1".equals(data.get(position).TCode)) {
                    return;
                }
                String shareUrl = "http://weishoot.com/Content/Images/logo@2x.png";
                if (data.get(position).PhotoList != null && data.get(position).PhotoList.size() > 0) {
                    shareUrl = HttpUrl.basePic_url + data.get(position).PhotoList.get(0).ImgName
                            + "&w=" + StaticInfo.widthPixels / 2 + "&q=50";
                }
                ShareBean sharebean = new ShareBean("图片分享自微摄(http://weishoot.com)", "点击查看更多详情",
                        shareUrl, "http://weishoot.com/SharePage?u=" + data.get(position).UCode
                                + "&t=" + data.get(position).TCode);
                showShareWindow.setShareData(sharebean);
                if (showShareWindow.isShowing()) {
                    showShareWindow.dismiss();
                } else {
                    showShareWindow.showAtLocation(lv, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0, 0);
                }
            }
        });

        if ("1".equals(data.get(position).IsGood) || indexZans.contains(position)) {// 已点赞
            holder.cb_zan.setSelected(true);
            holder.tv_zan.setText("赞");
            holder.tv_zan.setTextColor(Color.parseColor("#dc5e20"));
            holder.iv_my_head.setVisibility(View.VISIBLE);
            if (!("1".equals(data.get(position).IsGood))) {
                holder.iv_headNum.setText(zanNum + 1 + "");

            }

            holder.layout_head.setVisibility(View.VISIBLE);

        } else {
            holder.cb_zan.setSelected(false);
            holder.tv_zan.setText("赞");
            holder.tv_zan.setTextColor(Color.parseColor("#000000"));
            holder.iv_my_head.setVisibility(View.GONE);
            if (zanNum == 0) {
                holder.layout_head.setVisibility(View.GONE);
            }
        }

        if ("1".equals(data.get(position).IsCollection) || indexShouCangs.contains(position)) {
            holder.cb_shoucang.setSelected(true);
            holder.tv_shoucang.setTextColor(Color.parseColor("#dc5e20"));
        } else {
            holder.cb_shoucang.setSelected(false);
            holder.tv_shoucang.setTextColor(Color.parseColor("#000000"));
        }

        holder.layout_report.setVisibility(View.GONE);
        holder.layout_delete.setVisibility(View.GONE);

        if (data.get(position).UCode.equals(UserInfo.getInstance(mContext).getUserUCode())) {
            holder.layout_delete.setVisibility(View.VISIBLE);
        } else {
            holder.layout_report.setVisibility(View.VISIBLE);
        }

        final int i = position;
        holder.iv_head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    Intent it = new Intent(mContext, UserInfoDetialActivity.class);
                    it.putExtra(UserInfoDetialActivity.ARG_UCODE, data.get(i).UCode);
                    it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, data.get(i).NickName);
                    it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, data.get(i).UserHead);
                    mContext.startActivity(it);
                }
            }
        });
        holder.btn_allComment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("-1".equals(data.get(position).TCode)) {
                    return;
                }
                Intent it = new Intent(mContext, CommentListActivity.class);
                it.putExtra("data", data.get(i));
                it.putExtra("position", i);
                mContext.startActivity(it);
            }
        });
        holder.layout_address.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ShowAddressActivity.class);
                String[] arr = data.get(i).Coordinate.split(",");
                it.putExtra("lat", arr[0]);
                it.putExtra("lon", arr[1]);
                mContext.startActivity(it);
            }
        });
        holder.layout_btn_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("-1".equals(data.get(position).TCode)) {
                    return;
                }
                if (isLogin()) {
                    Intent it = new Intent(mContext, CreateCommentActivity.class);
                    it.putExtra(CreateCommentActivity.ARG_DATA, data.get(i));
                    it.putExtra(CreateCommentActivity.ARG_TYPE, "parent");
                    it.putExtra("position", i);
                    mContext.startActivity(it);
                }
            }
        });

        holder.iv_headNum.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("-1".equals(data.get(position).TCode)) {
                    return;
                }
                Intent it = new Intent(mContext, ZambiaActivity.class);
                it.putExtra(ZambiaActivity.ARG_FLAG, "zambia");
                it.putExtra(ZambiaActivity.ARG_TCODE, data.get(i).TCode);
                mContext.startActivity(it);
            }
        });
        holder.layout_zan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("-1".equals(data.get(position).TCode)) {
                    return;
                }
                if (isLogin()) {
                    requestZan(data.get(i).TCode, data.get(i).TType, holder, i);
                }
            }
        });

        holder.layout_shoucang.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("-1".equals(data.get(position).TCode)) {
                    return;
                }
                if (isLogin()) {
                    requestShouCang(data.get(i).TCode, holder, i);
                }

            }
        });
        // 删除
        holder.layout_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("-1".equals(data.get(position).TCode)) {
                    return;
                }
                if (isLogin()) {
                    final EditUserInfoDialog editDialog = new EditUserInfoDialog(mContext);
                    editDialog.setTitle("确定要删除吗？");
                    editDialog.show();
                    editDialog.setOnCancelListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            editDialog.dismiss();
                        }
                    });
                    editDialog.setOnOkListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            requestDeleteTopic(data.get(i).TCode, i);
                            editDialog.dismiss();
                        }
                    });

                }
            }
        });

        // 举报
        holder.layout_report.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("-1".equals(data.get(position).TCode)) {
                    return;
                }
                if (isLogin()) {
                    Intent it = new Intent(mContext, ReportActivity.class);
                    it.putExtra("NickName", data.get(i).NickName);
                    it.putExtra("Title", data.get(i).Title);
                    mContext.startActivity(it);
                }

            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView iv_head, iv_my_head;

        Button btn_allComment;

        LinearLayout layout_pic, layout_headImage, layout_comment, layout_zan, layout_btn_comment,
                layout_shoucang, layout_delete, layout_report, layout_share;

        RelativeLayout layout_address, layout_head;

        TextView iv_headNum, tv_name, tv_time, tv_from, tv_content, tv_address, tv_addressNum,
                tv_mark, tv_zan, tv_shoucang;

        CheckBox cb_zan, cb_shoucang;

        RelativeLayout view_gray;
    }

    private boolean isLogin() {
        if (UserInfo.getInstance(mContext).isLogin()) {
            return true;
        } else {
            Intent it = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(it);
            return false;
        }

    }

    private void setLayoutPicture(LinearLayout layout, TopicData topicData) {
        layout.removeAllViews();
        if (topicData.PhotoList == null || topicData.PhotoList.size() == 0) {
            return;
        }
        String[] strType = topicData.PicSort.split("-");
        int temp = 0;
        if (strType.length == 1 && "1".equals(strType[0])) {
            if ("5".equals(topicData.TType)) {
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.view_1, null);
                ImageView iv_view1_1 = (ImageView) view1.findViewById(R.id.iv_view1_1);
                if ("-1".equals(topicData.TCode)) {
                    imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName, iv_view1_1);
                } else {
//                    imageLoader.displayImage(HttpUrl.basePic_url
//                            + topicData.PhotoList.get(temp).ImgName + "&w="
//                            + StaticInfo.widthPixels / 2 + "&q=80", iv_view1_1, mOptionsPic);
                    
                    imageLoader.displayImage(topicData.PhotoList.get(temp).ImgName, iv_view1_1, mOptionsPic);
                
                    
                
                }
                setPicListener(iv_view1_1, topicData, topicData.PhotoList.get(temp).ImgName);
                layout.addView(view1);
                return;
            } else if ("6".equals(topicData.TType)) {
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.view_1_1, null);
                ImageView iv_view1_1 = (ImageView) view1.findViewById(R.id.iv_view1_1);
                if ("-1".equals(topicData.TCode)) {
                    imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName, iv_view1_1);
                } else {
//                    imageLoader.displayImage(HttpUrl.basePic_url
//                            + topicData.PhotoList.get(temp).ImgName + "&w="
//                            + StaticInfo.widthPixels / 2 + "&q=80", iv_view1_1, mOptionsPic);
               
                    imageLoader.displayImage(topicData.PhotoList.get(temp).ImgName, iv_view1_1, mOptionsPic);
                
                }
                setPicListener(iv_view1_1, topicData, topicData.PhotoList.get(temp).ImgName);
                layout.addView(view1);
                return;
            } else {
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.view_1, null);
                ImageView iv_view1_1 = (ImageView) view1.findViewById(R.id.iv_view1_1);
                if ("-1".equals(topicData.TCode)) {
                    imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName, iv_view1_1);
                } else {
//                    imageLoader.displayImage(HttpUrl.basePic_url
//                            + topicData.PhotoList.get(temp).ImgName + "&w="
//                            + StaticInfo.widthPixels / 2 + "&q=80", iv_view1_1, mOptionsPic);
//                
                    imageLoader.displayImage(topicData.PhotoList.get(temp).ImgName, iv_view1_1, mOptionsPic);
                
                }
                setPicListener(iv_view1_1, topicData, topicData.PhotoList.get(temp).ImgName);
                layout.addView(view1);
                return;
            }

        }
        for (int i = 0; i < strType.length; i++) {
            int intType;
            try {
                intType = Integer.parseInt(strType[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                intType = 1;
            }
            switch (intType) {
                case 1:
                    View view1 = LayoutInflater.from(mContext).inflate(R.layout.view_1, null);
                    ImageView iv_view1_1 = (ImageView) view1.findViewById(R.id.iv_view1_1);
                    if ("-1".equals(topicData.TCode)) {
                        imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName,
                                iv_view1_1);
                    } else {
//                        imageLoader.displayImage(
//                                HttpUrl.basePic_url + topicData.PhotoList.get(temp).ImgName + "&w="
//                                        + StaticInfo.widthPixels / 2 + "&q=80", iv_view1_1,
//                                mOptionsPic);
                    	
                    	 imageLoader.displayImage(
                              topicData.PhotoList.get(temp).ImgName, iv_view1_1,
                               mOptionsPic);
                    }
                    setPicListener(iv_view1_1, topicData, topicData.PhotoList.get(temp).ImgName);
                    temp++;
                    layout.addView(view1);
                    break;
                case 2:
                    View view2 = LayoutInflater.from(mContext).inflate(R.layout.view_2, null);
                    ImageView iv_view2_1 = (ImageView) view2.findViewById(R.id.iv_view2_1);
                    ImageView iv_view2_2 = (ImageView) view2.findViewById(R.id.iv_view2_2);
                    if ("-1".equals(topicData.TCode)) {
                        imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName,
                                iv_view2_1);
                    } else {
//                        imageLoader.displayImage(
//                                HttpUrl.basePic_url + topicData.PhotoList.get(temp).ImgName + "&w="
//                                        + StaticInfo.widthPixels / 2 + "&q=80", iv_view2_1,
//                                mOptionsPic);
                        
                   	 imageLoader.displayImage(
                             topicData.PhotoList.get(temp).ImgName, iv_view2_1,
                              mOptionsPic);
                    }
                    setPicListener(iv_view2_1, topicData, topicData.PhotoList.get(temp).ImgName);
                    temp++;
                    if ("-1".equals(topicData.TCode)) {
                        imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName,
                                iv_view2_2);
                    } else {
                    	
                    	
//                        imageLoader.displayImage(
//                                HttpUrl.basePic_url + topicData.PhotoList.get(temp).ImgName + "&w="
//                                        + StaticInfo.widthPixels / 2 + "&q=80", iv_view2_2,
//                                mOptionsPic);
//                        
//                        
                        imageLoader.displayImage(topicData.PhotoList.get(temp).ImgName, iv_view2_2, mOptionsPic);
                    }
                    setPicListener(iv_view2_2, topicData, topicData.PhotoList.get(temp).ImgName);
                    temp++;
                    layout.addView(view2);
                    break;
                case 3:
                    View view3 = LayoutInflater.from(mContext).inflate(R.layout.view_3, null);
                    ImageView iv_view3_1 = (ImageView) view3.findViewById(R.id.iv_view3_1);
                    ImageView iv_view3_2 = (ImageView) view3.findViewById(R.id.iv_view3_2);
                    ImageView iv_view3_3 = (ImageView) view3.findViewById(R.id.iv_view3_3);
                    if ("-1".equals(topicData.TCode)) {
                        imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName,
                                iv_view3_1);
                    } else {
//                        imageLoader.displayImage(
//                                HttpUrl.basePic_url + topicData.PhotoList.get(temp).ImgName + "&w="
//                                        + StaticInfo.widthPixels / 2 + "&q=80", iv_view3_1,
//                                mOptionsPic);
                        
                        imageLoader.displayImage(topicData.PhotoList.get(temp).ImgName, iv_view3_1, mOptionsPic);
                    }
                    setPicListener(iv_view3_1, topicData, topicData.PhotoList.get(temp).ImgName);
                    temp++;
                    if ("-1".equals(topicData.TCode)) {
                        imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName,
                                iv_view3_2);
                    } else {
//                        imageLoader.displayImage(
//                                HttpUrl.basePic_url + topicData.PhotoList.get(temp).ImgName + "&w="
//                                        + StaticInfo.widthPixels / 2 + "&q=80", iv_view3_2,
//                                mOptionsPic);
                        
                        
                        imageLoader.displayImage(topicData.PhotoList.get(temp).ImgName, iv_view3_2, mOptionsPic);
                    }
                    setPicListener(iv_view3_2, topicData, topicData.PhotoList.get(temp).ImgName);
                    temp++;
                    if ("-1".equals(topicData.TCode)) {
                        imageloaderLocat.loadImage(topicData.PhotoList.get(temp).ImgName,
                                iv_view3_3);
                    } else {
//                        imageLoader.displayImage(
//                                HttpUrl.basePic_url + topicData.PhotoList.get(temp).ImgName + "&w="
//                                        + StaticInfo.widthPixels / 2 + "&q=80", iv_view3_3,
//                                mOptionsPic);
                    	
                    	   imageLoader.displayImage(topicData.PhotoList.get(temp).ImgName, iv_view3_3, mOptionsPic);
                    }
                    setPicListener(iv_view3_3, topicData, topicData.PhotoList.get(temp).ImgName);
                    temp++;
                    layout.addView(view3);
                    break;
                default:
                    break;
            }
        }
    }

    private void setPicListener(View view, final TopicData topicData, final String url) {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("-1".equals(topicData.TCode)) {
                    return;
                }
                Intent it = new Intent(mContext, PictureDetailActivity.class);
                it.putExtra("topicData", topicData);
                it.putExtra("url", url);
                mContext.startActivity(it);
            }
        });
    }

    private void setLayoutHeadImage(LinearLayout layout, final List<TopGoodBean> GoodList) {
        layout.removeAllViews();
        if (GoodList == null) {
            return;
        }
        for (int i = 0; i < GoodList.size(); i++) {
            if (GoodList.get(i).UCode.equals(UserInfo.getInstance(mContext).getUserUCode())) {
                continue;
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_head_image, null);
            ImageView iv_head = (ImageView) view.findViewById(R.id.iv_head);
            imageLoader.displayImage(GoodList.get(i).UserHead, iv_head, mOptionsHead);
            layout.addView(view);
            // layout.removeViewAt(index)
            final int j = i;
            iv_head.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, UserInfoDetialActivity.class);
                    it.putExtra(UserInfoDetialActivity.ARG_UCODE, GoodList.get(j).UCode);
                    it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, GoodList.get(j).NickName);
                    it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, GoodList.get(j).UserHead);
                    mContext.startActivity(it);
                }
            });
        }
    }

    private void setLayoutComment(LinearLayout layout, final List<TopCommentBean> CommentList,
            final int position) {
        layout.removeAllViews();
        if (CommentList == null) {
            return;
        }
        for (int i = 0; i < CommentList.size(); i++) {
            if (i >= 5) {
                return;
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_comment, null);
            ImageView iv_com_head = (ImageView) view.findViewById(R.id.iv_com_head);
            TextView tv_com_name = (TextView) view.findViewById(R.id.tv_com_name);
            TextView tv_com_content = (TextView) view.findViewById(R.id.tv_com_content);
            TextView tv_com_time = (TextView) view.findViewById(R.id.tv_com_time);
            imageLoader.displayImage(CommentList.get(i).UserHead, iv_com_head, mOptionsHead);
            tv_com_name.setText(CommentList.get(i).NickName);
            tv_com_content.setText(StringsUtil.strToSmiley(mContext, CommentList.get(i).Contents));
            tv_com_time.setText(StringsUtil.getTimeFormat(CommentList.get(i).CreateDate));
            layout.addView(view);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if ("-1".equals(data.get(position).TCode)) {
                        return;
                    }
                    Intent it = new Intent(mContext, CommentListActivity.class);
                    it.putExtra("data", data.get(position));
                    it.putExtra("position", position);
                    mContext.startActivity(it);

                }
            });

        }
    }

    private void requestZan(String fCode, String gType, final ViewHolder holder, final int i) {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(mContext).getUserUCode());
        params.addBodyParameter("fCode", fCode);
        params.addBodyParameter("GType", gType);

        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.ManageGood, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                progressDialog.dismiss();
                WeiShootToast.makeErrorText(mContext, mContext.getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                progressDialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                Log.v("TAG", "-------Zan---->" + temp);
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject object = new JSONObject(temp);

                        JSONObject obj = object.getJSONObject("result");
                        if ("1".equals(obj.optString("ResultCode"))) {
                            JSONObject o = object.getJSONObject("data");
                            if (holder.cb_zan.isSelected()) {
                                holder.cb_zan.setSelected(false);
                                holder.tv_zan.setText("赞");
                                holder.tv_zan.setTextColor(Color.parseColor("#000000"));
                                indexZans.remove((Integer) i);
                                holder.iv_my_head.setVisibility(View.GONE);

                                holder.iv_headNum.setText(zanNum + "");
                                if (zanNum == 0) {
                                    holder.layout_head.setVisibility(View.GONE);
                                }

                            } else {
                                holder.cb_zan.setSelected(true);
                                holder.tv_zan.setText("赞");
                                holder.tv_zan.setTextColor(Color.parseColor("#dc5e20"));
                                indexZans.add(i);
                                holder.iv_my_head.setVisibility(View.VISIBLE);
                                holder.iv_headNum.setText(zanNum + 1 + "");
                                holder.layout_head.setVisibility(View.VISIBLE);
                            }

                        } else {
                            WeiShootToast.makeErrorText(mContext, obj.optString("ResultMsg"),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    WeiShootToast.makeErrorText(mContext,
                            mContext.getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    private void requestShouCang(String tCode, final ViewHolder holder, final int i) {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(mContext).getUserUCode());
        params.addBodyParameter("TCode", tCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.ManageCollections, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(mContext,
                                mContext.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        Log.v("TAG", "-------ShouCang---->" + temp);
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                if (Integer.parseInt(object.optString("result")) > 0) {

                                    if (holder.cb_shoucang.isSelected()) {
                                        holder.cb_shoucang.setSelected(false);
                                        holder.tv_shoucang.setTextColor(Color.parseColor("#000000"));
                                        indexShouCangs.remove((Integer) i);
                                    } else {
                                        holder.cb_shoucang.setSelected(true);
                                        holder.tv_shoucang.setTextColor(Color.parseColor("#dc5e20"));

                                        indexShouCangs.add(i);
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(mContext,
                                    mContext.getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void requestDeleteTopic(String tCode, final int i) {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", UserInfo.getInstance(mContext).getUserUCode());
        params.addBodyParameter("TCode", tCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.DeleteTopic, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                progressDialog.dismiss();
                WeiShootToast.makeErrorText(mContext, mContext.getString(R.string.http_timeout),
                        WeiShootToast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                progressDialog.dismiss();
                String temp = (String) objectResponseInfo.result;
                Log.v("TAG", "-------DeleteTopic---->" + temp);
                if (!TextUtils.isEmpty(temp)) {
                    try {
                        JSONObject object = new JSONObject(temp);
                        JSONObject obj = object.getJSONObject("result");

                        if ("200".equals(obj.optString("ResultCode"))) {
                            data.remove(i);
                            notifyDataSetChanged();
                            WeiShootToast.makeText(mContext, "删除成功", WeiShootToast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    WeiShootToast.makeErrorText(mContext,
                            mContext.getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

}
