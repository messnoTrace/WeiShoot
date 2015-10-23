
package com.NationalPhotograpy.weishoot.adapter.photograph;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.view.album.MatrixImageView;
import com.NationalPhotograpy.weishoot.view.album.MatrixImageView.OnMovingListener;
import com.NationalPhotograpy.weishoot.view.album.MatrixImageView.OnSingleTapListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * @ClassName: AlbumViewPager
 * @Description: 自定义viewpager 优化了事件拦截
 * @author LinJ
 * @date 2015-1-9 下午5:33:33
 */
public class AlbumViewPagerLocat extends ViewPager implements OnMovingListener {
    public final static String TAG = "AlbumViewPager";

    protected com.nostra13.universalimageloader.core.ImageLoader imageLoaderLocat;

    private com.nostra13.universalimageloader.core.DisplayImageOptions mOptionsLocat;

    /** 当前子控件是否处理拖动状态 */
    private boolean mChildIsBeingDragged = false;

    /** 界面单击事件 用以显示和隐藏菜单栏 */
    private OnSingleTapListener onSingleTapListener;

    public AlbumViewPagerLocat(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.imageLoaderLocat = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        // 设置网络图片加载参数
        mOptionsLocat = new com.nostra13.universalimageloader.core.DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (mChildIsBeingDragged)
            return false;
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void startDrag() {
        mChildIsBeingDragged = true;
    }

    @Override
    public void stopDrag() {
        mChildIsBeingDragged = false;
    }

    public void setOnSingleTapListener(OnSingleTapListener onSingleTapListener) {
        this.onSingleTapListener = onSingleTapListener;
    }

    public interface OnPlayVideoListener {
        void onPlay(String path);
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private List<String> paths;// 大图地址 如果为网络图片 则为大图url

        public ViewPagerAdapter(List<String> PhotoList) {
            this.paths = PhotoList;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            // 注意，这里不可以加inflate的时候直接添加到viewGroup下，而需要用addView重新添加
            // 因为直接加到viewGroup下会导致返回的view为viewGroup
            View imageLayout = inflate(getContext(), R.layout.item_album_pager, null);
            viewGroup.addView(imageLayout);
            assert imageLayout != null;
            final MatrixImageView imageView = (MatrixImageView) imageLayout
                    .findViewById(R.id.image);
            imageView.setOnMovingListener(AlbumViewPagerLocat.this);
            imageView.setOnSingleTapListener(onSingleTapListener);
            imageLoaderLocat
                    .displayImage("file://" + paths.get(position), imageView, mOptionsLocat);
            return imageLayout;
        }

        @Override
        public int getItemPosition(Object object) {
            // 在notifyDataSetChanged时返回None，重新绘制
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int arg1, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}
