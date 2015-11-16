
package com.NationalPhotograpy.weishoot.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;

public class EditUserInfoDialog extends Dialog {
    private TextView dialog_title_view;

    private EditText et_dialog;

    private ListView dialog_list_view;

    private Button dialog_single_btn, dialog_cancel_btn, dialog_ok_btn;

    private View dialog_btn_divider,dialog_btn_divider_margin;

    public EditUserInfoDialog(Context context) {
        super(context, R.style.EditDialog);
        initView();
    }

    private void initView() {

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_userinfo, null);
        dialog_title_view = (TextView) mView.findViewById(R.id.dialog_title_view);
        et_dialog = (EditText) mView.findViewById(R.id.et_dialog);
        dialog_list_view = (ListView) mView.findViewById(R.id.dialog_list_view);
        dialog_single_btn = (Button) mView.findViewById(R.id.dialog_single_btn);
        dialog_cancel_btn = (Button) mView.findViewById(R.id.dialog_cancel_btn);
        dialog_ok_btn = (Button) mView.findViewById(R.id.dialog_ok_btn);
        dialog_btn_divider = mView.findViewById(R.id.dialog_btn_divider);
        dialog_btn_divider_margin = mView.findViewById(R.id.dialog_btn_divider_margin);
        super.setContentView(mView);
    }

    public void setTitle(String title) {
        dialog_title_view.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            dialog_title_view.setText(title);
        }

    }

    public void setEdHint(String hint) {
        et_dialog.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(hint)) {
            et_dialog.setHint(hint);
        }
    }

    public Button getDialogSingleBtn() {
        return dialog_single_btn;
    }

    public Button getDialogCancelBtn() {
        return dialog_cancel_btn;
    }

    public Button getDialogOkBtn() {
        return dialog_ok_btn;
    }

    public EditText getEtDialog() {
        return et_dialog;
    }

    public ListView getDialogListView() {
        dialog_list_view.setVisibility(View.VISIBLE);
        return dialog_list_view;
    }

    public View getDialogBtnDivider() {
        return dialog_btn_divider;
    }
    
    public View getDialogBtnDividerMargin() {
        return dialog_btn_divider_margin;
    }

    /**
     * * 单独取消键监听器 *
     * 
     * @param listener
     */
    public void setOnSingleCancelListener(View.OnClickListener listener) {
        dialog_single_btn.setVisibility(View.VISIBLE);
        dialog_single_btn.setOnClickListener(listener);
    }

    /**
     * * 取消键监听器 *
     * 
     * @param listener
     */
    public void setOnCancelListener(View.OnClickListener listener) {
        dialog_btn_divider.setVisibility(View.VISIBLE);
        dialog_cancel_btn.setVisibility(View.VISIBLE);
        dialog_cancel_btn.setOnClickListener(listener);
    }

    /**
     * * 确定键监听器 * @param listener
     */
    public void setOnOkListener(View.OnClickListener listener) {
        dialog_ok_btn.setVisibility(View.VISIBLE);
        dialog_ok_btn.setOnClickListener(listener);
    }

}
