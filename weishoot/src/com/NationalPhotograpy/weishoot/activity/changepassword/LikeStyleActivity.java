
package com.NationalPhotograpy.weishoot.activity.changepassword;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.bean.UserInfosBean;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.AllTags;
import com.NationalPhotograpy.weishoot.utils.Methods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LikeStyleActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "LikeStyleActivity";

    private TextView tv_complete;

    private LinearLayout layout_common_label;

    private ArrayList<String> styles = new ArrayList<String>();

    private LinearLayout layout_label, layout_showInput;

    private EditText edText;

    int tvIndex = 0;

    int totalWith = 0;

    LinearLayout layout = null;

    List<TextView> textViews = new ArrayList<TextView>();

    List<LinearLayout> layouts = new ArrayList<LinearLayout>();

    private String ActivityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_like_style);
        initView();
        initData();
        setListener();
        setCommonLable();

    }

    private void initView() {
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        layout_label = (LinearLayout) findViewById(R.id.layout_label);
        layout_showInput = (LinearLayout) findViewById(R.id.layout_showInput);
        edText = (EditText) findViewById(R.id.edText);
        layout_common_label = (LinearLayout) findViewById(R.id.layout_common_label);
        layout_showInput.removeView(edText);
        // edText = new EditText(this);
        // edText.setTextSize(14);
        // // edText.requestFocus();
        // edText.setFocusable(true);
        // // edText.set
        // edText.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        // edText.setTextColor(this.getResources().getColor(R.color.black));
        // edText.setSingleLine(true);
        // edText.setHorizontallyScrolling(true);
        layout = addLayout();
    }

    private void initData() {
        ActivityType = getIntent().getStringExtra("ActivityType");
        onRefresh(null);
    }

    private void setListener() {
        tv_complete.setOnClickListener(this);
        layout_showInput.setOnClickListener(this);
        edText.addTextChangedListener(new EditChangedListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_complete:
                settResult();
                finish();
                break;
            case R.id.layout_showInput:
                edText.requestFocus();
                setBgLabelSelected(null);
                Methods.showInput(this);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (edText.getText().length() > 0) {
                return false;
            } else {
                delLabel();
                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    private void onRefresh(String label) {
    	
      
        if (!TextUtils.isEmpty(label)) {
            styles.add(label);
        } else {
            String jsonString = SharePreManager.getInstance(this).getAllTag();
            passJsonTag(jsonString);

        }
    }

    private void passJsonTag(String json) {
        
        try {
            JSONObject object = new JSONObject(json);
            JSONArray jArray = object.getJSONArray("data");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                styles.add(obj.optString("TName"));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setCommonLable() {
        layout_common_label.removeAllViews();
        for (int i = 0; i < styles.size(); i++) {
            final TextView label = new TextView(this);
            LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    Methods.dip2px(this, 30));
            ps.topMargin = Methods.dip2px(this, 10);
            ps.rightMargin = Methods.dip2px(this, 5);
            ps.leftMargin = Methods.dip2px(this, 5);
            label.setLayoutParams(ps);
            label.setGravity(Gravity.CENTER);
            label.setSingleLine();
            label.setEllipsize(TextUtils.TruncateAt.END);
            label.setText(styles.get(i));
            label.setTextSize(16);
            label.setTextColor(this.getResources().getColor(R.color.white));
            label.setPadding(Methods.dip2px(this, 8), 0, Methods.dip2px(this, 8), 0);
            label.setBackgroundColor(Color.parseColor("#8cc11f"));
            label.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (styles.indexOf(label.getText()) != -1) {
                        addTvLable(label.getText().toString());
                        styles.remove(styles.indexOf(label.getText().toString()));
                        setCommonLable();
                    }

                }
            });
            layout_common_label.addView(label);
        }

    }

    public void addTvLable(String text) {
        edText.setText("");
        if (TextUtils.isEmpty(text)) {
            return;
        }
        final TextView label = new TextView(this);
        textViews.add(label);
        LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                Methods.dip2px(this, 28));

        ps.rightMargin = Methods.dip2px(this, 5);
        ps.leftMargin = Methods.dip2px(this, 5);
        label.setLayoutParams(ps);
        label.setGravity(Gravity.CENTER);
        label.setSingleLine();
        label.setText(text);
        label.setTextSize(14);
        label.setPadding(Methods.dip2px(this, 4), 0, Methods.dip2px(this, 4), 0);
        setBgLabelNormal(label);
        label.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setBgLabelSelected(label);

            }
        });
        totalWith += Methods.getWidth(label) + Methods.dip2px(this, 10);
        if (totalWith > Methods.getWindowWith(this)) {
            layout.removeView(edText);
            layout = addLayout();
            totalWith = Methods.getWidth(label) + Methods.dip2px(this, 10);
            tvIndex = 0;
        }
        layout.addView(label, tvIndex);
        tvIndex++;

    }

    private void setBgLabelNormal(TextView tv) {
        tv.setBackgroundColor(Color.parseColor("#10000000"));
        tv.setTextColor(this.getResources().getColor(R.color.black));
    }

    private void setBgLabelSelected(TextView tv) {
        for (int i = 0; i < textViews.size(); i++) {
            setBgLabelNormal(textViews.get(i));
            textViews.get(i).setSelected(false);

        }
        if (tv != null) {
            tv.setBackgroundColor(Color.parseColor("#8cc11f"));
            tv.setTextColor(this.getResources().getColor(R.color.white));
            tv.setSelected(true);
        }

    }

    private LinearLayout addLayout() {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(ps);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(0, Methods.dip2px(this, 5), 0, 0);
        layout.addView(edText);
        layouts.add(layout);
        layout_label.addView(layout);

        return layout;
    }

    private void delLabel() {
        boolean isHasSel = true;
        for (int i = 0; i < textViews.size(); i++) {
            if (textViews.get(i).isSelected()) {
                textViews.get(i).setVisibility(View.GONE);
                LinearLayout llLayout = (LinearLayout) textViews.get(i).getParent();
                if (layouts.size() - 1 == layouts.indexOf(llLayout)) {
                    tvIndex--;
                    totalWith -= Methods.getWidth(textViews.get(i)) + Methods.dip2px(this, 10);
                    // if (llLayout.getChildCount() == 1 && layouts.size() != 1)
                    // {
                    // llLayout.removeView(edText);
                    // LinearLayout lLayout = (LinearLayout) textViews.get(i -
                    // 1).getParent();
                    // lLayout.addView(edText);
                    // }
                }
                llLayout.removeView(textViews.get(i));
                onRefresh(textViews.get(i).getText().toString());
                textViews.remove(i);
                setCommonLable();
                isHasSel = false;
            }
        }
        if (isHasSel && textViews.size() > 0) {
            setBgLabelSelected(textViews.get(textViews.size() - 1));
        }

        for (int j = 0; j < layouts.size(); j++) {
            int i = layouts.get(j).getChildCount();
            Log.v(TAG, "layouts " + i);
            if (i == 0) {
                // if (j == layouts.size() - 1 && layouts.size() > 1) {
                //
                // layouts.get(j).removeView(edText);
                // layouts.get(j-1).addView(edText);
                // layouts.get(j).setVisibility(View.GONE);
                // layouts.remove(j);
                // return;
                // }
                layouts.get(j).setVisibility(View.GONE);
                layouts.remove(j);

            }
        }

    }

    private void settResult() {
        Intent it = new Intent();
        ArrayList<String> tempList = new ArrayList<String>();
        for (int i = 0; i < textViews.size(); i++) {
            tempList.add(textViews.get(i).getText().toString());
        }
        it.putStringArrayListExtra("label", tempList);
        if ("UPLOAD".equals(ActivityType)) {
            setResult(Constant.RESULT_LABEL_UPLOAD, it);
        }
    }

    class EditChangedListener implements TextWatcher {
        private CharSequence temp;// 监听前的文本

        private int editStart;// 光标开始位置

        private int editEnd;// 光标结束位置

        private final int charMaxNum = 20;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String ss = s.toString().substring(start);
            String text = "";
            if (start >= 0) {
                text = s.toString().substring(0, start);
            }

            if (ss.equals(",") || ss.equals("，")) {

                addTvLable(text);
            }

            Editable editable = edText.getText();
            int len = editable.length();

            if (len > charMaxNum) {
                Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_LONG).show();
                int selEndIndex = Selection.getSelectionEnd(editable);
                String str = editable.toString();
                // 截取新字符串
                String newStr = str.substring(0, charMaxNum);
                edText.setText(newStr);
                editable = edText.getText();

                // 新字符串的长度
                int newLen = editable.length();
                // 旧光标位置超过字符串长度
                if (selEndIndex > newLen) {
                    selEndIndex = editable.length();
                }
                // 设置新光标所在的位置
                Selection.setSelection(editable, selEndIndex);

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
