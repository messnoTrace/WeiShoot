
package com.NationalPhotograpy.weishoot.view;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.bean.ShareBean;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;

public class SharePopupWindow extends PopupWindow implements OnClickListener,
        PlatformActionListener {

    private Context context;

    private ShareBean shareBean;

    private Button btn_wechart, btn_friends, btn_sina, btn_qq, btn_qzone;

    private final int MSG_SHARE_COMPLETE = 1;

    private final int MSG_SHARE_ERROR = 2;

    Platform wechart, wechatMoments, sina, qq, qzone;

    private ProgressiveDialog dialog;

    public SharePopupWindow(Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow_share, null);
        initView(view);
        initData();
        setListener();
        setView(view);
    }

    public void setShareData(ShareBean shareBean) {
        this.shareBean = shareBean;
        setListener();
    }

    private void initView(View view) {
        btn_wechart = (Button) view.findViewById(R.id.btn_wechart);
        btn_friends = (Button) view.findViewById(R.id.btn_friends);
        btn_sina = (Button) view.findViewById(R.id.btn_sina);
        btn_qq = (Button) view.findViewById(R.id.btn_qq);
        btn_qzone = (Button) view.findViewById(R.id.btn_qzone);
        dialog = new ProgressiveDialog(context);
    }

    private void initData() {

    }

    private void setListener() {
        btn_wechart.setOnClickListener(this);
        btn_friends.setOnClickListener(this);
        btn_sina.setOnClickListener(this);
        btn_qq.setOnClickListener(this);
        btn_qzone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wechart:
                wechart = ShareSDK.getPlatform(Wechat.NAME);
                ShareParams sp_wechart = new ShareParams();
                sp_wechart.setShareType(Platform.SHARE_WEBPAGE);
                sp_wechart.setTitle(shareBean.shareTitle);
                sp_wechart.setText(shareBean.shareText);
                sp_wechart.setImageUrl(shareBean.shareImagePath);
                wechart.share(sp_wechart);
                wechart.setPlatformActionListener(this);

                dialog.show();
                break;
            case R.id.btn_friends:
                wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                ShareParams sp_wechartMoments = new ShareParams();
                sp_wechartMoments.setShareType(Platform.SHARE_WEBPAGE);
                sp_wechartMoments.setTitle(shareBean.shareTitle);
                sp_wechartMoments.setText(shareBean.shareText);
                sp_wechartMoments.setImageUrl(shareBean.shareImagePath);
                wechatMoments.share(sp_wechartMoments);
                wechatMoments.setPlatformActionListener(this);

                dialog.show();
                break;
            case R.id.btn_sina:
                sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                ShareParams sp_sina = new ShareParams();
                sp_sina.setText(shareBean.shareText);
                sp_sina.setImageUrl(shareBean.shareImagePath);
                sina.share(sp_sina);
                sina.setPlatformActionListener(this);
                dialog.show();
                break;
            case R.id.btn_qq:
                qq = ShareSDK.getPlatform(QQ.NAME);
                ShareParams sp_qq = new ShareParams();
                sp_qq.setTitle(shareBean.shareTitle);
                sp_qq.setTitleUrl(shareBean.shareUrl);
                sp_qq.setText(shareBean.shareText);
                sp_qq.setImageUrl(shareBean.shareImagePath);
                qq.share(sp_qq);
                qq.setPlatformActionListener(this);
                dialog.show();
                break;
            case R.id.btn_qzone:
                qzone = ShareSDK.getPlatform(QZone.NAME);
                ShareParams sp_qzone = new ShareParams();
                sp_qzone.setTitle(shareBean.shareTitle);
                sp_qzone.setTitleUrl(shareBean.shareUrl);
                sp_qzone.setText(shareBean.shareText);
                sp_qzone.setImageUrl(shareBean.shareImagePath);
                sp_qzone.setSite(shareBean.shareTitle);
                sp_qzone.setSiteUrl(shareBean.shareUrl);
                qzone.share(sp_qzone);
                qzone.setPlatformActionListener(this);
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void setView(View view) {
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case MSG_SHARE_COMPLETE:
                    WeiShootToast.makeText(context, "分享成功", WeiShootToast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                case MSG_SHARE_ERROR:
                    WeiShootToast.makeErrorText(context, "重复提交相似信息！", WeiShootToast.LENGTH_SHORT)
                            .show();
                    dismiss();
                    break;
                default:
                    break;
            }

        }

    };

    @Override
    public void onCancel(Platform arg0, int arg1) {
        Message msg = new Message();
        msg.what = MSG_SHARE_ERROR;
        msg.obj = "取消分享";
        handler.sendMessage(msg);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        handler.sendEmptyMessage(MSG_SHARE_COMPLETE);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        Message msg = new Message();
        msg.what = MSG_SHARE_ERROR;
        try {
            JSONObject jsonobj = new JSONObject(arg2.getMessage());
            msg.obj = jsonobj.optString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            msg.obj = "分享失败";
        }
        handler.sendMessage(msg);
    }
}
