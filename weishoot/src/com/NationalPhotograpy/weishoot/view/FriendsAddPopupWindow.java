
package com.NationalPhotograpy.weishoot.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.quanzi.NewFriendsActivity;
import com.NationalPhotograpy.weishoot.view.zxing.MipcaCaptureActivity;

public class FriendsAddPopupWindow extends PopupWindow implements OnClickListener {

    private Context mContext;

    private LinearLayout layout_newFriend, layout_SaoyiSao;

    public FriendsAddPopupWindow(Activity context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow_friends_add, null);
        initView(view);
        initData();
        setListener();
        setView(view);
    }

    private void initView(View view) {
        layout_newFriend = (LinearLayout) view.findViewById(R.id.layout_newFriend);
        layout_SaoyiSao = (LinearLayout) view.findViewById(R.id.layout_SaoyiSao);
    }

    private void initData() {

    }

    private void setListener() {
        layout_newFriend.setOnClickListener(this);
        layout_SaoyiSao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.layout_newFriend:
                it = new Intent(mContext, NewFriendsActivity.class);
                mContext.startActivity(it);
                FriendsAddPopupWindow.this.dismiss();
                break;
            case R.id.layout_SaoyiSao:
                it = new Intent(mContext, MipcaCaptureActivity.class);
                mContext.startActivity(it);
                FriendsAddPopupWindow.this.dismiss();
                break;
            default:
                break;
        }
    }

    private void setView(View view) {
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }
}
