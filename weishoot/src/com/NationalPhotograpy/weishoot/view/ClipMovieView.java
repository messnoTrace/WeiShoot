
package com.NationalPhotograpy.weishoot.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.storage.Constant;

@SuppressLint("DrawAllocation")
public class ClipMovieView extends View {
    private Context mContext;

    public ClipMovieView(Context context) {
        super(context);
        mContext = context;
    }

    public ClipMovieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ClipMovieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* 这里就是绘制矩形区域 */
        int rectMovieWidth = this.getWidth();
        int rectMocieHeight = (int) (rectMovieWidth * Constant.MOVIE_SIZE);
        int tempHeight = (this.getHeight() - rectMocieHeight) / 2;
        Paint paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.transparent_gray));
        Paint paintLine = new Paint();
        paintLine.setColor(mContext.getResources().getColor(R.color.white));
        // top
        canvas.drawRect(0, 0, this.getWidth(), tempHeight, paint);
        canvas.drawLine(0, tempHeight - 1, rectMovieWidth, tempHeight - 1, paintLine);
        // left
        // canvas.drawRect(0, tempHeight, tempWidth, tempHeight + rect360,
        // paint);
        canvas.drawLine(0, tempHeight - 1, 0, tempHeight + rectMocieHeight, paintLine);
        // right
        // canvas.drawRect(tempWidth + rect360, tempHeight, this.getWidth(),
        // tempHeight + rect360,paint);
        canvas.drawLine(rectMovieWidth - 1, tempHeight, rectMovieWidth - 1, tempHeight
                + rectMocieHeight, paintLine);
        // bottom
        canvas.drawRect(0, tempHeight + rectMocieHeight, this.getWidth(), this.getHeight(), paint);
        canvas.drawLine(0, tempHeight + rectMocieHeight, rectMovieWidth, tempHeight
                + rectMocieHeight, paintLine);
    }
}
