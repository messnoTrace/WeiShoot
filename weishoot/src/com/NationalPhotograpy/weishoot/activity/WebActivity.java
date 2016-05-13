package com.NationalPhotograpy.weishoot.activity;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.view.WebView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.widget.ImageView;
import android.widget.TextView;

public class WebActivity extends BaseActivity implements WebView.onWebViewClient {

	private WebView mWebView;
	private ImageView back;
	private TextView tv_title;

	private String title;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		findViews();
	
		bindListener();
		initData();


	}

	private void bindListener(){
		mWebView.setOnWebViewClient(this);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	private void findViews() {
		mWebView = (WebView) findViewById(R.id.wv_web);
		back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	private void initData(){
		if (!TextUtils.isEmpty(url)) {
			if (url.toLowerCase().startsWith("www.")) {
				url = "http://" + url;
			}
			mWebView.loadUrl(url);
			
		}
		if (!TextUtils.isEmpty(title)) {
			tv_title.setText(title);
		}
	}
	@Override
	public void onReceivedTitle(String title) {

	}

	@Override
	public boolean shouldOverrideUrlLoading(String url) {
		return false;
	}

	@Override
	public void onPageFinished(String url) {

	}

	@Override
	public void onPageStarted(String url, Bitmap favicon) {

	}

	@Override
	public boolean onJsAlert(String url, String message, JsResult result) {
		return false;
	}

	@Override
	public boolean onJsConfirm(String url, String message, JsResult result) {
		return false;
	}

}
