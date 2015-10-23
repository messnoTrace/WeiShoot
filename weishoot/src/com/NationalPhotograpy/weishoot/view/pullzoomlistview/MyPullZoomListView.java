
package com.NationalPhotograpy.weishoot.view.pullzoomlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.view.pullrefresh.PullToRefreshBase;

public class MyPullZoomListView extends PullToRefreshBase<PullToZoomListView> {

    private static final String TAG = "MyPullListView";

    public MyPullZoomListView(Context context) {
        super(context);
    }

    public MyPullZoomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPullZoomListView(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected PullToZoomListView createRefreshableView(Context context, AttributeSet attrs) {
        // TODO Auto-generated method stub
        PullToZoomListView listView = new PullToZoomListView(context, attrs);
        // listView.getHeaderView().setImageResource(R.drawable.splash01);
        //
        // listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        listView.setId(R.id.mylist);
        return listView;
    }

    @Override
    protected boolean isReadyForPullDown() {

        return false;
    }

    @Override
    protected boolean isReadyForPullUp() {
        View last = getRefreshableView().getChildAt(getRefreshableView().getChildCount() - 1);
        ImageView tView = (ImageView) last.findViewById(R.id.iv_head);
        Adapter adapter = getRefreshableView().getAdapter();

        int position = -1;
        if (last != null) {
            try {
                position = (Integer) tView.getTag();
            } catch (Exception e) {
                e.printStackTrace();
                position = -1;
            }
        }

        Log.v(TAG, "kkkkkkkkkkk:" + "position " + (position));
        if (position == adapter.getCount() - 2) {
            return last.getBottom() == getRefreshableView().getHeight();
        }
        return false;
    }

}
