
package com.NationalPhotograpy.weishoot.adapter.quanzi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class SideBar extends View {
    private List<Character> letterList = new ArrayList<Character>();

    private SectionIndexer sectionIndexter = null;

    private ListView list;

    private float value;

    public SideBar(Context context) {
        super(context);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        value = value = dm.scaledDensity;
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        value = value = dm.scaledDensity;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        value = value = dm.scaledDensity;
    }

    public void setIndex(Set<Character> index_list) {
        letterList = new ArrayList<Character>();
        for (Character str : index_list) {
            letterList.add(str);
        }
        Collections.sort(letterList);
    }

    public void setListView(ListView _list) {
        list = _list;
        sectionIndexter = (SectionIndexer) _list.getAdapter();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int i = (int) event.getY();
        int idx = i / (getMeasuredHeight() / letterList.size());
        if (idx >= letterList.size()) {
            idx = letterList.size() - 1;
        } else if (idx < 0) {
            idx = 0;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (sectionIndexter == null) {
                sectionIndexter = (SectionIndexer) list.getAdapter();
            }
            int position = sectionIndexter.getPositionForSection(letterList.get(idx));
            if (position == -1) {
                return true;
            }
            list.setSelection(position);
        } else {
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(15 * value);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
        float widthCenter = getMeasuredWidth() / 2;
        if (letterList.size() > 0) {
            float height = getMeasuredHeight() / letterList.size();
            for (int i = 0; i < letterList.size(); i++) {
                canvas.drawText(String.valueOf(letterList.get(i)), widthCenter, (i + 1) * height
                        - height / 2, paint);
            }
        }
        this.invalidate();
        super.onDraw(canvas);
    }
}
