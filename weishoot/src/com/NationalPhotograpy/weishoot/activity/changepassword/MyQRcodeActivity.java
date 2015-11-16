
package com.NationalPhotograpy.weishoot.activity.changepassword;

import java.io.File;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.BaseActivity;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.storage.SharePreManager;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.NationalPhotograpy.weishoot.view.zxing.decoding.QRCodeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyQRcodeActivity extends BaseActivity implements OnClickListener {

    private LinearLayout layout_back;

    private TextView tv_name, tv_introduction;

    private ImageView iv_qrcode, iv_head;

    private String filePathName;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_my_qrcode);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        this.imageLoader = ImageLoader.getInstance();
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
    }

    private void initData() {
        tv_name.setText(UserInfo.getInstance(this).getUserNickName());
        tv_introduction.setText(UserInfo.getInstance(this).getUserIntroduction());
        imageLoader.displayImage(UserInfo.getInstance(this).getUserHead(), iv_head, mOptionsHead);
        String erweima = UserInfo.getInstance(this).getUserUCode();
        String erweimaFile = SharePreManager.getInstance(MyQRcodeActivity.this).getErWeiMaFile();
        if (SharePreManager.getInstance(MyQRcodeActivity.this).getErWeiMa().equals(erweima)) {
            if (erweimaFile.length() > 0) {
                iv_qrcode.setImageBitmap(BitmapFactory.decodeFile(erweimaFile));
            } else {
                createQRImage("http://a.app.qq.com/o/simple.jsp?pkgname=com.Dailyfood.meirishejian&u="
                        + erweima);
                SharePreManager.getInstance(MyQRcodeActivity.this).setErWeiMa(erweima);
            }
        } else {
            createQRImage("http://a.app.qq.com/o/simple.jsp?pkgname=com.Dailyfood.meirishejian&u="
                    + erweima);
            SharePreManager.getInstance(MyQRcodeActivity.this).setErWeiMa(erweima);
        }
    }

    private void createQRImage(final String string) {
        String filePath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filePathName = Environment.getExternalStorageDirectory() + "/weishoot/cache/" + "qr_"
                    + System.currentTimeMillis() + ".jpg";
            filePath = Environment.getExternalStorageDirectory() + "/weishoot/cache/";
        } else {
            filePathName = "/data/data/weishoot/cache/" + "qr_" + System.currentTimeMillis()
                    + ".jpg";
            filePath = "/data/data/weishoot/cache/";
        }
        File file = new File(filePath);
        if (!file.exists()) {
            checkOrSaveDir(filePath);
        }
        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(string, 500, 500,
                        BitmapFactory.decodeResource(getResources(), R.drawable.icon_app),
                        filePathName);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharePreManager.getInstance(MyQRcodeActivity.this).setErWeiMaFile(
                                    filePathName);
                            iv_qrcode.setImageBitmap(BitmapFactory.decodeFile(filePathName));
                        }
                    });
                }
            }
        }).start();
    }

    private void checkOrSaveDir(String filePath) {
        String[] dir = filePath.split("/");
        String dist = dir[0];
        for (int i = 1; i < dir.length; i++) {
            dist += "/" + dir[i];
            File mkdir = new File(dist);
            if (mkdir.exists()) {
            } else {
                mkdir.mkdir();
            }
        }

    }

    private void setListener() {
        layout_back.setOnClickListener(this);
        iv_head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_head:
                Intent it = new Intent(MyQRcodeActivity.this, UserInfoDetialActivity.class);
                it.putExtra(UserInfoDetialActivity.ARG_UCODE, UserInfo.getInstance(this)
                        .getUserUCode());
                it.putExtra(UserInfoDetialActivity.ARG_NIKENAME, UserInfo.getInstance(this)
                        .getUserNickName());
                it.putExtra(UserInfoDetialActivity.ARG_USERHEAD, UserInfo.getInstance(this)
                        .getUserHead());
                startActivity(it);
                break;
            default:
                break;
        }
    }
}
