
package com.NationalPhotograpy.weishoot.adapter.shouye;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.registered.LoginActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.CreateCommentActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.bean.TopCommentBean;
import com.NationalPhotograpy.weishoot.bean.TopicBean.TopicData;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class CommentListAdapter extends BaseAdapter {

    private Context mContext;

    private List<TopCommentBean> data = new ArrayList<TopCommentBean>();

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsHead;

    private TopicData topicData;

    private int topIndex;

    public CommentListAdapter(Context context) {
        this.mContext = context;
        this.imageLoader = ImageLoader.getInstance();
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
    }

    public void setIndex(int index) {
        this.topIndex = index;
    }

    public void setData(List<TopCommentBean> data) {
        if (data != null && data.size() > 0) {
            this.data.clear();
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setTopicData(TopicData topicData) {
        this.topicData = topicData;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment_list, null);
            holder = new ViewHolder();
            holder.iv_com_head = (ImageView) convertView.findViewById(R.id.iv_com_head);
            holder.tv_com_name = (TextView) convertView.findViewById(R.id.tv_com_name);
            holder.tv_com_content = (TextView) convertView.findViewById(R.id.tv_com_content);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.btn_comm = (Button) convertView.findViewById(R.id.btn_comm);
            holder.rl_comment = (RelativeLayout) convertView.findViewById(R.id.rl_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int i = position;

        imageLoader.displayImage(data.get(position).UserHead, holder.iv_com_head, mOptionsHead);
        holder.tv_com_name.setText(data.get(position).NickName);
        holder.tv_com_content.setText(StringsUtil.strToSmiley(mContext, data.get(i).Contents));
        holder.tv_time.setText(StringsUtil.getTimeFormat(data.get(i).CreateDate));

        if (data.get(i).UCode.equals(UserInfo.getInstance(mContext).getUserUCode())) {
            holder.btn_comm.setText("删除");
        } else {
            holder.btn_comm.setText("回复");
        }

        holder.iv_com_head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, data.get(i).UCode);
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, data.get(i).NickName);
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, data.get(i).UserHead);
                mContext.startActivity(it);
            }
        });
        holder.btn_comm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (data.get(i).UCode.equals(UserInfo.getInstance(mContext).getUserUCode())) {
                    // 删除
                    requestDeleteComment(data.get(i).CCode, i);
                } else {
                    if (isLogin()
                            && (!(data.get(i).UCode.equals(UserInfo.getInstance(mContext)
                                    .getUserUCode())))) {
                        Intent it = new Intent(mContext, CreateCommentActivity.class);
                        it.putExtra(CreateCommentActivity.ARG_DATA, topicData);
                        it.putExtra(CreateCommentActivity.ARG_TYPE, "child");
                        it.putExtra(CreateCommentActivity.ARG_CHILD_DATA, data.get(i));
                        it.putExtra("position", topIndex);
                        mContext.startActivity(it);
                    }
                }

            }
        });
        holder.rl_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isLogin()
                        && (!(data.get(i).UCode.equals(UserInfo.getInstance(mContext)
                                .getUserUCode())))) {
                    Intent it = new Intent(mContext, CreateCommentActivity.class);
                    it.putExtra(CreateCommentActivity.ARG_DATA, topicData);
                    it.putExtra(CreateCommentActivity.ARG_TYPE, "child");
                    it.putExtra(CreateCommentActivity.ARG_CHILD_DATA, data.get(i));
                    it.putExtra("position", topIndex);
                    mContext.startActivity(it);
                }

            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_com_head;

        TextView tv_com_name, tv_time, tv_com_content;

        Button btn_comm;

        RelativeLayout rl_comment;
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

    private void requestDeleteComment(String cCode, final int i) {
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();

        params.addBodyParameter("CCode", cCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.DeleteComment, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        WeiShootToast.makeErrorText(mContext,
                                mContext.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        String temp = (String) objectResponseInfo.result;
                        Log.v("TAG", "------->" + temp);
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);
                                if ("200".equals(object.optString("ResultCode"))) {
                                    data.remove(i);
                                    notifyDataSetChanged();
                                } else {
                                    WeiShootToast.makeErrorText(mContext,
                                            object.optString("ResultMsg"),
                                            WeiShootToast.LENGTH_SHORT).show();
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
}
