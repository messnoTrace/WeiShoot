
package com.NationalPhotograpy.weishoot.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.utils.UserInfo.UserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ProgressiveDialog extends Dialog {

    private ImageView iv_head;

    private ImageLoader imageLoader;

    private DisplayImageOptions mOptionsHead;

    public ProgressiveDialog(Context context) {
        super(context, R.style.ProgressiveDialog);
        View mContentView = getLayoutInflater().inflate(R.layout.progress_view, null);
        imageLoader = ImageLoader.getInstance();
        mOptionsHead = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageForEmptyUri(R.drawable.default_head).build();
        iv_head = (ImageView) mContentView.findViewById(R.id.iv_head);
        if (UserInfo.getInstance(context).isLogin()) {
            imageLoader.displayImage(UserInfo.getInstance(context).getUserHead(), iv_head,
                    mOptionsHead);
        }
        setContentView(mContentView);
        setCanceledOnTouchOutside(false);
        this.setCancelable(true);
    }
}
