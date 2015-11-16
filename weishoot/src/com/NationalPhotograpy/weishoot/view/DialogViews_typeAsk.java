package com.NationalPhotograpy.weishoot.view;

import com.Dailyfood.meirishejian.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 询问弹出框
 * 
 * @author blchen
 * 
 */
public class DialogViews_typeAsk extends Dialog {

	private Activity mActivity;
	// 编辑标题
	private TextView tv_title_exit, tv_image_tubei, tv_remanent_tubei;
	// 确定
	private Button btn_ok;
	// 取消
	private Button btn_cancle, btn_pay;

	private LinearLayout ll_pay;

	public DialogViews_ask mAction_ask;

	public interface DialogViews_ask {
		public void doOk();

		public void doCancle();

		public void doPay();
	}

	public DialogViews_typeAsk(Activity mActivity, boolean showtwo,
			DialogViews_ask listener) {
		super(mActivity, R.style.dialog_normal);
		this.mActivity = mActivity;
		setContentView(R.layout.dialog_ask);
		this.mAction_ask = listener;
		// 实例化
		initentView_photo();
		if (!showtwo) {
			btn_cancle.setVisibility(View.GONE);
			findViewById(R.id.line).setVisibility(View.GONE);
		}
		// 数据填充
		setDataAndListener();
	}

	public DialogViews_typeAsk(Activity mActivity, boolean showtwo,
			DialogViews_ask listener, boolean showtitle) {
		super(mActivity, R.style.dialog_normal);
		this.mActivity = mActivity;
		setContentView(R.layout.dialog_ask);
		this.mAction_ask = listener;
		// 实例化
		initentView_photo();
		if (!showtwo) {
			btn_cancle.setVisibility(View.GONE);
			findViewById(R.id.line).setVisibility(View.GONE);
		}
		if (showtitle) {
			tv_title_exit.setVisibility(View.VISIBLE);
		}
		// 数据填充
		setDataAndListener();
	}

	// public DialogViews_typeAsk(Activity mActivity, DialogViews_ask listener)
	// {
	// super(mActivity, R.style.dialog_normal);
	// this.mActivity = mActivity;
	// setContentView(R.layout.dialog_first_login);
	// this.mAction_ask = listener;
	// TextView tv_title = (TextView) findViewById(R.id.Tv_title_dialgask);
	// TextView tv_message = (TextView) findViewById(R.id.Tv_message_dialgask);
	// // 确定
	// Button btn_ok = (Button) findViewById(R.id.Btn_okdialog_dialogask);
	// tv_title.setText(mActivity.getString(R.string.login_title));
	// tv_message.setText(mActivity.getString(R.string.login_message));
	// btn_ok.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// close();
	// if (mAction_ask != null) {
	// mAction_ask.doOk();
	// }
	// }
	// });
	// }

	/**
	 * 设置编辑dialog标题
	 * 
	 * @param title
	 */
	public void setTitleText(String title) {
		tv_title_exit.setText(title);
	}

	public void setCancel(String title) {
		btn_ok.setText(title);
	}

	public void setImageTubei(String coin) {
		tv_image_tubei.setText(coin);
	}

	public void setRemanenTubeit(String coin) {
		tv_remanent_tubei.setText(coin);
	}

	public void setIsPayLayout(boolean isVisibility) {
		if (isVisibility) {
			ll_pay.setVisibility(View.VISIBLE);
		} else {
			ll_pay.setVisibility(View.GONE);
		}
	}

	/**
	 * 实例化
	 */
	private void initentView_photo() {
		tv_title_exit = (TextView) findViewById(R.id.Tv_title_exit);
		// 确定
		btn_ok = (Button) findViewById(R.id.Btn_okdialog_dialogask);
		// 取消
		btn_cancle = (Button) findViewById(R.id.Btn_cancledialog_dialogask);
		tv_image_tubei = (TextView) findViewById(R.id.tv_image_tubei);
		tv_remanent_tubei = (TextView) findViewById(R.id.tv_remanent_tubei);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
	}

	/**
	 * 数据填充和添加监听
	 */
	private void setDataAndListener() {
		// 确定
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
				if (mAction_ask != null) {
					mAction_ask.doOk();
				}
			}
		});
		// 取消
		btn_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
				if (mAction_ask != null) {
					mAction_ask.doCancle();
				}
			}
		});
		// 充值
		btn_pay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
				if (mAction_ask != null) {
					mAction_ask.doPay();
				}
			}
		});
	}

	@Override
	public void cancel() {
		super.cancel();
		close();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		close();
	}

	/**
	 * 关闭dialog
	 */
	public void close() {
		if (!mActivity.isFinishing()) {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (isShowing()) {
						dismiss();
					}
				}
			});
		}
	}

	/**
	 * 设置dialog显示位置
	 */
	public void setProperty(int x, int y) {
		Window window = getWindow();
		WindowManager.LayoutParams p = window.getAttributes();
		p.x = x;
		p.y = y;
		window.setAttributes(p);
	}

}
