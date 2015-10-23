
package com.NationalPhotograpy.weishoot.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;

public class StyleAlertDialog extends Dialog {
    Context context;

    private TextView msg;

    private Button cancel;

    private Button ok;

    CharSequence okText = "";

    CharSequence cancelText = "";

    CharSequence titleText = "";

    CharSequence contentText;

    View.OnClickListener okListener;

    View.OnClickListener cancelListener;

    private LinearLayout cancelLinear;

    private LinearLayout okLinear;

    private TextView content;

    private LinearLayout contetnLinear;

    public StyleAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public StyleAlertDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public StyleAlertDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        msg = (TextView) findViewById(R.id.dialog_title_msg);
        content = (TextView) findViewById(R.id.dialog_content);
        cancel = (Button) findViewById(R.id.dialog_button_cancel);
        ok = (Button) findViewById(R.id.dialog_button_ok);
        cancelLinear = (LinearLayout) findViewById(R.id.dialog_button_cancel_linear);
        okLinear = (LinearLayout) findViewById(R.id.dialog_button_ok_linear);
        contetnLinear = (LinearLayout) findViewById(R.id.dialog_content_linear);
        msg.setText(titleText);
        if (!TextUtils.isEmpty(okText)) {
            okLinear.setVisibility(View.VISIBLE);
            ok.setText(okText);
            ok.setOnClickListener(okListener);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            cancelLinear.setVisibility(View.VISIBLE);
            cancel.setText(cancelText);
            cancel.setOnClickListener(cancelListener);
        } else {
            cancelLinear.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(contentText)) {
            contetnLinear.setVisibility(View.VISIBLE);
            content.setText(contentText);
        }
    }

    public void setPositiveButton(CharSequence text, View.OnClickListener listener) {
        okText = text;
        okListener = listener;
    }

    public void setNegativeButton(CharSequence text, View.OnClickListener listener) {
        cancelText = text;
        cancelListener = listener;
    }

    public void setTitleMsg(CharSequence title) {
        titleText = title;
    }

    public void setContent(CharSequence content) {
        contentText = content;
    }

}
