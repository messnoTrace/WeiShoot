
package com.NationalPhotograpy.weishoot.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.utils.DimensionPixelUtil;

@SuppressLint("DrawAllocation")
public class ClipView extends View {
    private Context mContext;

    public ClipView(Context context) {
        super(context);
        mContext = context;
    }

    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ClipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* 这里就是绘制矩形区域 */
        int rect360 = (int) DimensionPixelUtil.getDimensionPixelSize(1, 280, mContext);
        int tempWidth = (this.getWidth() - rect360) / 2;
        int tempHeight = (this.getHeight() - rect360) / 2;
        Paint paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.transparent_gray));
        Paint paintLine = new Paint();
        paintLine.setColor(mContext.getResources().getColor(R.color.white));
        // top
        canvas.drawRect(0, 0, this.getWidth(), tempHeight, paint);
        canvas.drawLine(tempWidth - 1, tempHeight - 1, tempWidth + rect360, tempHeight - 1,
                paintLine);
        // left
        canvas.drawRect(0, tempHeight, tempWidth, tempHeight + rect360, paint);
        canvas.drawLine(tempWidth - 1, tempHeight - 1, tempWidth - 1, tempHeight + rect360,
                paintLine);
        // right
        canvas.drawRect(tempWidth + rect360, tempHeight, this.getWidth(), tempHeight + rect360,
                paint);
        canvas.drawLine(tempWidth + rect360, tempHeight, tempWidth + rect360, tempHeight + rect360,
                paintLine);
        // bottom
        canvas.drawRect(0, tempHeight + rect360, this.getWidth(), this.getHeight(), paint);
        canvas.drawLine(tempWidth, tempHeight + rect360, tempWidth + rect360, tempHeight + rect360,
                paintLine);
    }
}
