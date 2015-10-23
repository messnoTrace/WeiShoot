
package com.NationalPhotograpy.weishoot.adapter.quanzi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.bean.ZambiaBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.StringsUtil;
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

public class FriendsListAdapter extends BaseSwipeAdapter implements SectionIndexer {
    private Context mContext;

    private LayoutInflater inflater;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsHead;

    private ProgressiveDialog progressDialog;

    private List<ZambiaBean> zamBias = new ArrayList<ZambiaBean>();

    private IAfterDeletedListener onAfterDeleteListener = null;

    private String flag = "";

    private String mLightHeightText = "";

    private String FriendType;

    public FriendsListAdapter(Context con, String FriendType) {
        super();
        this.mContext = con;
        this.inflater = LayoutInflater.from(con);
        this.FriendType = FriendType;
        this.imageLoader = ImageLoader.getInstance();
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
        progressDialog = new ProgressiveDialog(con);
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setData(List<ZambiaBean> zamBias) {
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
        holder.tv_user_name.setText(getSpanableText(zamBias.get(position).NickName,
                mLightHeightText));
        holder.tv_user_signature.setText(zamBias.get(position).Introduction);

        if (!"1".equals(zamBias.get(position).IsAttention)) {
            holder.swipe_ll.setSwipeEnabled(false);
            if ("KEFU1440650315357".equals(zamBias.get(position).UCode)) {
                holder.btn_add_attention.setVisibility(View.INVISIBLE);
            } else {
                holder.btn_add_attention.setVisibility(View.VISIBLE);
            }
        } else {
            holder.btn_add_attention.setVisibility(View.GONE);
        }
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
                requestAttentionOption(i);
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
            if ("通讯录".equals(FriendType)) {
                this.tv_delete.setText("删除好友");
                this.btn_add_attention.setText("添加好友");
            }
        }
    }

    /**
     * 设置需要高亮的字
     */
    public void setLightHeightText(String text) {
        mLightHeightText = text;
    }

    /**
     * 设置需要高亮的字
     * 
     * @param wholeText 原始字符串
     * @param spanableText 需要高亮的字符串
     * @return 高亮后的字符串
     */
    public static SpannableString getSpanableText(String wholeText, String spanableText) {
        if (TextUtils.isEmpty(wholeText))
            wholeText = "";
        SpannableString spannableString = new SpannableString(wholeText);
        if (spanableText.equals(""))
            return spannableString;
        wholeText = wholeText.toLowerCase();
        spanableText = spanableText.toLowerCase();
        int startPos = wholeText.indexOf(spanableText);
        if (startPos == -1) {
            int tmpLength = spanableText.length();
            String tmpResult = "";
            for (int i = 1; i <= tmpLength; i++) {
                tmpResult = spanableText.substring(0, tmpLength - i);
                int tmpPos = wholeText.indexOf(tmpResult);
                if (tmpPos == -1) {
                    tmpResult = spanableText.substring(i, tmpLength);
                    tmpPos = wholeText.indexOf(tmpResult);
                }
                if (tmpPos != -1)
                    break;
                tmpResult = "";
            }
            if (tmpResult.length() != 0) {
                return getSpanableText(wholeText, tmpResult);
            } else {
                return spannableString;
            }
        }
        int endPos = startPos + spanableText.length();
        do {
            endPos = startPos + spanableText.length();
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            startPos = wholeText.indexOf(spanableText, endPos);
        } while (startPos != -1);
        return spannableString;
    }

    private void requestAttentionOption(final int position) {
        progressDialog.show();
        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("FCode", zamBias.get(position).UCode);
        params.addBodyParameter("PCode", UserInfo.getInstance(mContext).getUserUCode());
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        httpUtils.send(HttpMethod.POST, HttpUrl.AttentionOption, params,
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
                        if (!TextUtils.isEmpty(temp)) {
                            try {
                                JSONObject object = new JSONObject(temp);

                                JSONObject obj = object.getJSONObject("result");
                                if ("200".equals(obj.optString("ResultCode"))) {

                                    if ("0".equals(zamBias.get(position).IsAttention)) {
                                        zamBias.get(position).IsAttention = "1";
                                        StaticInfo.mutualConcernList.add(zamBias.get(position));
                                        if (!TextUtils.isEmpty(flag) && "attention".equals(flag)) {
                                            zamBias.remove(position);
                                        }
                                        WeiShootToast.makeText(mContext, "添加成功",
                                                WeiShootToast.LENGTH_SHORT).show();
                                    } else if ("1".equals(zamBias.get(position).IsAttention)) {
                                        zamBias.get(position).IsAttention = "0";
                                        if (!TextUtils.isEmpty(flag) && "attention".equals(flag)) {
                                            zamBias.remove(position);
                                        }
                                        WeiShootToast.makeText(mContext, "删除成功",
                                                WeiShootToast.LENGTH_SHORT).show();
                                    }
                                    notifyDataSetChanged();

                                } else {
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

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < zamBias.size(); i++) {
            String l = StringsUtil.converterToFirstSpell(zamBias.get(i).NickName.charAt(0) + "");
            char firstChar = l.toUpperCase(Locale.ENGLISH).charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
