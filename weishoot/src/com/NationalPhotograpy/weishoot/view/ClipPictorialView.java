
package com.NationalPhotograpy.weishoot.view;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;

/**
 * @author Administrator
 */
@SuppressLint("DrawAllocation")
public class ClipPictorialView extends ImageView {
    private Context mContext;

    private Bitmap clipImage;

    public ClipPictorialView(Context context) {
        super(context);
        mContext = context;
    }

    public ClipPictorialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ClipPictorialView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        this.clipImage = bm;
        invalidate();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        this.clipImage = bd.getBitmap();
        invalidate();
    }

    @Override
    public void setImageURI(Uri uri) {
        try {
            this.clipImage = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /* 这里就是绘制矩形区域 */
        int tempHeigntTop = (int) DimensionPixelUtil.getDimensionPixelSize(1, 50, mContext);
        int tempWidth = (int) DimensionPixelUtil.getDimensionPixelSize(1, 25, mContext);
        int rectWidth = this.getWidth() - tempWidth * 2;
        int rectHeight = (int) (rectWidth * Constant.PICTORIAL_SIZE);
        Paint paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.transparent_gray));
        Paint paintLine = new Paint();
        paintLine.setColor(mContext.getResources().getColor(R.color.white));
        // top
        canvas.drawRect(0, 0, this.getWidth(), tempHeigntTop, paint);
        canvas.drawLine(tempWidth - 1, tempHeigntTop - 1, tempWidth + rectWidth + 2,
                tempHeigntTop - 1, paintLine);
        // left
        canvas.drawRect(0, tempHeigntTop, tempWidth, tempHeigntTop + rectHeight, paint);
        canvas.drawLine(tempWidth - 1, tempHeigntTop - 1, tempWidth - 1, tempHeigntTop + rectHeight
                + 2, paintLine);
        if (clipImage != null) {
            RectF dst = new RectF(tempWidth, tempHeigntTop, tempWidth + rectWidth, tempHeigntTop
                    + rectHeight);
            canvas.drawBitmap(clipImage, null, dst, paint);
        }
        // right
        canvas.drawRect(tempWidth + rectWidth, tempHeigntTop, this.getWidth(), tempHeigntTop
                + rectHeight, paint);
        canvas.drawLine(tempWidth + rectWidth + 1, tempHeigntTop, tempWidth + rectWidth + 1,
                tempHeigntTop + rectHeight, paintLine);
        // bottom
        canvas.drawRect(0, tempHeigntTop + rectHeight, this.getWidth(), this.getHeight(), paint);
        canvas.drawLine(tempWidth, tempHeigntTop + rectHeight + 1, tempWidth + rectWidth,
                tempHeigntTop + rectHeight + 1, paintLine);
    }
}
