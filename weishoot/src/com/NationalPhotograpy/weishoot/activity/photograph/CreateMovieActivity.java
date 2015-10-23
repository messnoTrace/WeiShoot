
package com.NationalPhotograpy.weishoot.activity.photograph;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.MainActivity;
import com.NationalPhotograpy.weishoot.activity.changepassword.LikeStyleActivity;
import com.NationalPhotograpy.weishoot.bean.PicSortBean;
import com.NationalPhotograpy.weishoot.bean.ReqUploadBean;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;
import com.NationalPhotograpy.weishoot.utils.ImageLoader;
import com.NationalPhotograpy.weishoot.utils.ImageLoader.Type;
import com.NationalPhotograpy.weishoot.utils.Methods;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.baidulocation.SelectAddressActivity;

public class CreateMovieActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back, layout_caption;

    private TextView tv_userName;

    private RelativeLayout layout_movie, rl_label;

    private ImageView iv_movie;

    private View view_top, view_bottom;

    private TextView tv_caption, tv_translation;

    private Button click_on_off, btn_complete;

    private TextView tv_label, tv_locat_hint, tv_location;

    private ImageView iv_label, iv_location;

    private LinearLayout layout_label;

    private StringBuffer reqLabel = new StringBuffer("");

    private String reqAddressName = "", reqAddressNum = "";

    private Uri picUri;

    private int movieHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_create_movie);
        initView();
        initData();
        setListener();
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_locat_hint = (TextView) findViewById(R.id.tv_locat_hint);
        tv_location = (TextView) findViewById(R.id.tv_location);
        iv_label = (ImageView) findViewById(R.id.iv_label);
        iv_location = (ImageView) findViewById(R.id.iv_location);
        layout_label = (LinearLayout) findViewById(R.id.layout_label);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_caption = (LinearLayout) findViewById(R.id.layout_caption);
        tv_userName = (TextView) findViewById(R.id.tv_userName);
        layout_movie = (RelativeLayout) findViewById(R.id.layout_movie);
        rl_label = (RelativeLayout) findViewById(R.id.rl_label);
        iv_movie = (ImageView) findViewById(R.id.iv_movie);
        view_top = (View) findViewById(R.id.view_top);
        view_bottom = (View) findViewById(R.id.view_bottom);
        tv_caption = (TextView) findViewById(R.id.tv_caption);
        tv_translation = (TextView) findViewById(R.id.tv_translation);
        click_on_off = (Button) findViewById(R.id.click_on_off);
        btn_complete = (Button) findViewById(R.id.btn_complete);
        click_on_off.setSelected(true);
        showView(view_top, view_bottom);
        if (StaticInfo.widthPixels == 0) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            StaticInfo.widthPixels = mDisplayMetrics.widthPixels;
        }
        movieHeight = (int) (StaticInfo.widthPixels * Constant.MOVIE_SIZE);
        LinearLayout.LayoutParams lyp1 = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, movieHeight);
        layout_movie.setLayoutParams(lyp1);
    }

    private void initData() {
        picUri = getIntent().getParcelableExtra("picUri");
        ImageLoader.getInstance(3, Type.LIFO).loadImage(picUri.getPath(), iv_movie);
        tv_userName.setText(UserInfo.getInstance(this).getUserNickName());
    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        click_on_off.setOnClickListener(this);
        layout_caption.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        rl_label.setOnClickListener(this);
        iv_location.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        layout_label.setOnClickListener(this);
    }

    @SuppressWarnings("deprecation")
    private void showView(View view1, View view2) {
        int buttonH = (int) DimensionPixelUtil.getDimensionPixelSize(1, 20, this);
        RelativeLayout.LayoutParams lyp1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, buttonH);
        lyp1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        view1.setLayoutParams(lyp1);
        RelativeLayout.LayoutParams lyp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, buttonH);
        lyp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        view2.setLayoutParams(lyp2);
    }

    @SuppressWarnings("deprecation")
    private void dissView(View view1, View view2) {
        int buttonH = (int) DimensionPixelUtil.getDimensionPixelSize(1, 0, this);
        RelativeLayout.LayoutParams lyp1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, buttonH);
        lyp1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        view1.setLayoutParams(lyp1);
        RelativeLayout.LayoutParams lyp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, buttonH);
        lyp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        view2.setLayoutParams(lyp2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.click_on_off:
                if (click_on_off.isSelected()) {
                    click_on_off.setSelected(false);
                    dissView(view_top, view_bottom);
                } else {
                    click_on_off.setSelected(true);
                    showView(view_top, view_bottom);
                }
                break;
            case R.id.layout_caption:
                Intent it = new Intent(CreateMovieActivity.this, AddCaptionActivity.class);
                startActivityForResult(it, 1);
                break;
            case R.id.rl_label:
            case R.id.layout_label:
                it = new Intent(CreateMovieActivity.this, LikeStyleActivity.class);
                it.putExtra("ActivityType", "UPLOAD");
                startActivityForResult(it, 1);
                break;
            case R.id.iv_location:
                it = new Intent(CreateMovieActivity.this, SelectAddressActivity.class);
                it.putExtra("ActivityType", "UPLOAD");
                startActivityForResult(it, 1);
                break;
            case R.id.tv_location:
                it = new Intent(CreateMovieActivity.this, SelectAddressActivity.class);
                it.putExtra("ActivityType", "UPLOAD");
                startActivityForResult(it, 1);
                break;
            case R.id.btn_complete:
                String[] tempArr = reqLabel.toString().split(",");
                if (tempArr.length < 2) {
                    WeiShootToast.makeErrorText(CreateMovieActivity.this, "至少选择2个标签",
                            WeiShootToast.LENGTH_SHORT).show();
                    return;
                }
                List<String> uploadPic = new ArrayList<String>();
                uploadPic.add(picUri.getPath());
                String str_Cropping = "0";
                if (click_on_off.isSelected()) {
                    str_Cropping = "1";
                } else {
                    str_Cropping = "0";
                }
                ReqUploadBean uploadBean = new ReqUploadBean("5", reqLabel.toString(),
                        new PicSortBean("1"), reqAddressName, reqAddressNum, uploadPic,
                        str_Cropping, tv_caption.getText().toString(), tv_translation.getText()
                                .toString());
                it = new Intent(this, MainActivity.class);
                it.putExtra("Result", "RESULT_UPLOAD_HOME");
                it.putExtra("Request", "movie");
                it.putExtra("ReqUploadBean", uploadBean);
                startActivity(it);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constant.RESULT_CAPTION:
                tv_caption.setText(data.getStringExtra("src"));
                tv_translation.setText(data.getStringExtra("dst"));
                break;
            case Constant.RESULT_LABEL_UPLOAD:

                ArrayList<String> labelList = data.getStringArrayListExtra("label");
                if (labelList.size() > 0) {
                    tv_label.setVisibility(View.GONE);
                } else {
                    tv_label.setVisibility(View.VISIBLE);
                }
                setLayoutLabel(labelList);
                for (int i = 0; i < labelList.size(); i++) {
                    reqLabel.append(labelList.get(i) + ",");
                }
                break;
            case Constant.RESULT_LOCATION_UPLOAD:
                tv_locat_hint.setVisibility(View.GONE);
                reqAddressName = data.getStringExtra("addressName");
                reqAddressNum = data.getStringExtra("addressNum");
                tv_location.setText(reqAddressName);
                break;
            default:
                break;
        }
    }

    private void setLayoutLabel(ArrayList<String> labelList) {
        layout_label.removeAllViews();
        for (int i = 0; i < labelList.size(); i++) {
            final TextView label = new TextView(this);
            LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    (int) DimensionPixelUtil.getDimensionPixelSize(1, 25, this));
            ps.rightMargin = Methods.dip2px(this, 2);
            ps.leftMargin = Methods.dip2px(this, 2);
            label.setLayoutParams(ps);
            label.setGravity(Gravity.CENTER);
            label.setSingleLine();
            label.setEllipsize(TextUtils.TruncateAt.END);
            label.setText(labelList.get(i));
            label.setTextSize(16);
            label.setTextColor(this.getResources().getColor(R.color.white));
            label.setPadding(Methods.dip2px(this, 8), 0, Methods.dip2px(this, 8), 0);
            label.setBackgroundColor(Color.parseColor("#8cc11f"));
            layout_label.addView(label);
        }

    }
}
