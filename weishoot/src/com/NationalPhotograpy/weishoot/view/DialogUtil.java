
package com.NationalPhotograpy.weishoot.view;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoActivity;

public class DialogUtil {

    private boolean isSetDate;

    private StringBuffer str_time;

    /**
     * 弹出日期选择器
     */
    @SuppressLint("InlinedApi")
    public void showCalenderDialog(final Context context, final TextView textview) {
        isSetDate = true;
        str_time = new StringBuffer("");
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(context,
                DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (isSetDate) {
                            isSetDate = false;
                            monthOfYear = monthOfYear + 1;
                            str_time.append(year + "-");
                            if (monthOfYear < 10) {
                                str_time.append("0" + monthOfYear + "-");
                            } else {
                                str_time.append("" + monthOfYear + "-");
                            }
                            if (dayOfMonth < 10) {
                                str_time.append("0" + dayOfMonth + " ");
                            } else {
                                str_time.append("" + dayOfMonth + " ");
                            }
                            textview.setText(str_time);
                            if(context instanceof UserInfoActivity){
                                ((UserInfoActivity) context).requestUpDataUserInfo("Birthday", str_time.toString());
                            }
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
        dateDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isSetDate = false;
                        dialog.dismiss();
                    }
                });
        dateDialog.show();
    }
}
