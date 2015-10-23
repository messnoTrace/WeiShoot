
package com.NationalPhotograpy.weishoot.adapter.find;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.registered.LoginActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.bean.UserInfosBean.UserInfos;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.ProgressiveDialog;
import com.NationalPhotograpy.weishoot.view.swipelayout.SwipeLayout;
import com.NationalPhotograpy.weishoot.view.swipelayout.adapters.BaseSwipeAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class RecommendedListAdapter extends BaseSwipeAdapter {
    private Context con;

    private LayoutInflater inflater;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsHead;

    private ProgressiveDialog progressDialog;

    private List<UserInfos> zamBias = new ArrayList<UserInfos>();

    private IAfterDeletedListener onAfterDeleteListener = null;

    private String flag = "";

    public RecommendedListAdapter(Context con) {
        super();
        this.con = con;
        this.inflater = LayoutInflater.from(con);
        this.imageLoader = ImageLoader.getInstance();
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
        progressDialog = new ProgressiveDialog(con);
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setData(List<UserInfos> zamBias) {
        if (zamBias.size() > 0) {
            this.zamBias.clear();
            this.zamBias.addAll(zamBias);
        }

        notifyDataSetChanged();
    }

    public void setOnAfterDeleteListener(IAfterDeletedListener onAfterDeleteListener) {
        this.onAfterDeleteListener = onAfterDeleteListener;
    }

    public static interface IAfterDeletedListener {
        void afterDelete(int position);
    }

    @Override
    public int getCount() {
        return zamBias.size();
    }

    @Override
    public Object getItem(int position) {
        return zamBias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_ll;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return inflater.inflate(R.layout.item_zambia, parent, false);
    }

    @Override
    public void fillValues(int position, View convertView) {

        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        setConvertView(holder, position);
    }

    private void setConvertView(ViewHolder holder, int position) {
        final int i = position;
        holder.swipe_ll.setSwipeEnabled(true);
        holder.btn_add_attention.setVisibility(View.VISIBLE);
        closeAllExcept(null);

        imageLoader.displayImage(zamBias.get(position).UserHead, holder.img_head, mOptionsHead);
        holder.tv_user_name.setText(zamBias.get(position).NickName);
        holder.tv_user_signature.setText(zamBias.get(position).Introduction);

        if ("1".equals(zamBias.get(position).IsAttention)) {
            holder.btn_add_attention.setVisibility(View.GONE);
        } else {
            holder.swipe_ll.setSwipeEnabled(false);
        }
        holder.img_head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(con, UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, zamBias.get(i).UCode);
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, zamBias.get(i).NickName);
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, zamBias.get(i).UserHead);
                con.startActivity(it);
            }
        });
        holder.tv_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (onAfterDeleteListener != null) {
                    onAfterDeleteListener.afterDelete(i);
                }
                requestAttentionOption(i);

            }
        });

        holder.btn_add_attention.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!UserInfo.getInstance(con).isLogin()) {
                    Intent intent = new Intent(con, LoginActivity.class);
                    con.startActivity(intent);
                    return;
                }

                requestAttentionOption(i);
            }
        });

        holder.content_fl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(con, UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, zamBias.get(i).UCode);
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, zamBias.get(i).NickName);
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, zamBias.get(i).UserHead);
                con.startActivity(it);
            }
        });

    }

    class ViewHolder {
        public TextView tv_delete, tv_user_name, tv_user_signature;

        public LinearLayout swipe_btn_ll;

        public ImageView img_head;

        public Button btn_add_attention;

        public FrameLayout content_fl;

        public SwipeLayout swipe_ll;

        public ViewHolder(View view) {
            this.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            this.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            this.tv_user_signature = (TextView) view.findViewById(R.id.tv_user_signature);
            this.swipe_btn_ll = (LinearLayout) view.findViewById(R.id.swipe_btn_ll);
            this.img_head = (ImageView) view.findViewById(R.id.img_head);
            this.btn_add_attention = (Button) view.findViewById(R.id.btn_add_attention);
            this.content_fl = (FrameLayout) view.findViewById(R.id.content_fl);
            this.swipe_ll = (SwipeLayout) view.findViewById(R.id.swipe_ll);
        }

    }

    private void requestAttentionOption(final int position) {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("FCode", zamBias.get(position).UCode);
        params.addBodyParameter("PCode", UserInfo.getInstance(con).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AttentionOption, params,
                new RequestCallBack<Object>() {

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        WeiShootToast.makeErrorText(con, con.getString(R.string.http_timeout),
                                WeiShootToast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        progressDialog.dismiss();
                        String temp = (String) objectResponseInfo.result;
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {

                                    if ("0".equals(zamBias.get(position).IsAttention)) {
                                        zamBias.get(position).IsAttention = "1";
                                        WeiShootToast.makeText(con, "关注成功",
                                                WeiShootToast.LENGTH_SHORT).show();
                                    } else if ("1".equals(zamBias.get(position).IsAttention)) {
                                        zamBias.get(position).IsAttention = "0";
                                        if (!TextUtils.isEmpty(flag) && "attention".equals(flag)) {
                                            zamBias.remove(position);
                                        }
                                        WeiShootToast.makeText(con, "取消关注",
                                                WeiShootToast.LENGTH_SHORT).show();
                                    }
                                    notifyDataSetChanged();

                                } else {
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            WeiShootToast.makeErrorText(con, con.getString(R.string.http_timeout),
                                    WeiShootToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
