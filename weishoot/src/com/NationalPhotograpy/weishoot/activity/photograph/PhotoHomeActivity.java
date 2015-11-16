
package com.NationalPhotograpy.weishoot.activity.photograph;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.MainActivity;
import com.NationalPhotograpy.weishoot.activity.find.PictureMarketActivity;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.AllTags;

/**
 * 摄影Home
 */
public class PhotoHomeActivity extends BaseActivity implements OnClickListener {

    private Button btn_back, btn_sctp, btn_zzyp, btn_zzhb, btn_td, btn_sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_photo_home);
        initView();
        initData();
        setListener();
        AllTags.getAllTagForNet(this);
    }

    private void initView() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_sctp = (Button) findViewById(R.id.btn_sctp);
        btn_zzyp = (Button) findViewById(R.id.btn_zzyp);
        btn_zzhb = (Button) findViewById(R.id.btn_zzhb);
        btn_td = (Button) findViewById(R.id.btn_td);
        btn_sc = (Button) findViewById(R.id.btn_sc);
    }

    private void initData() {

    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_sctp.setOnClickListener(this);
        btn_zzyp.setOnClickListener(this);
        btn_zzhb.setOnClickListener(this);
        btn_td.setOnClickListener(this);
        btn_sc.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULT_UPLOAD_SUCCESS) {
            Intent it = new Intent(this, MainActivity.class);
            it.putExtra("Result", "RESULT_UPLOAD_SUCCESS");
            it.putExtra("ResultBean", data.getSerializableExtra("ResultBean"));
            startActivity(it);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_sctp:
                it = new Intent(PhotoHomeActivity.this, SelectPicActivity.class);
                it.putExtra("type", "upload");
                startActivityForResult(it, 1);
                break;
            case R.id.btn_zzyp:
                it = new Intent(PhotoHomeActivity.this, SelectPicActivity.class);
                it.putExtra("type", "movie");
                startActivityForResult(it, 1);
                break;
            case R.id.btn_zzhb:
                it = new Intent(PhotoHomeActivity.this, SelectPicActivity.class);
                it.putExtra("type", "pictorial");
                startActivityForResult(it, 1);
                break;
            case R.id.btn_td:
                it = new Intent(PhotoHomeActivity.this, PictureMarketActivity.class);
                startActivity(it);
                break;
            case R.id.btn_sc:
                it = new Intent(this, MainActivity.class);
                it.putExtra("Result", "WEBVIEW");
                startActivity(it);
                finish();
                break;
            default:
                break;
        }
    }
}
