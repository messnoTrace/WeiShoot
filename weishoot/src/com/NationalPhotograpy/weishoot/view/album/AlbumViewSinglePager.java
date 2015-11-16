
package com.NationalPhotograpy.weishoot.view.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.bean.PhotoShopTypeBean.PhotoShopBean;
import com.NationalPhotograpy.weishoot.utils.imageloader.core.assist.FailReason;
import com.NationalPhotograpy.weishoot.utils.imageloader.core.listener.ImageLoadingListener;
import com.NationalPhotograpy.weishoot.view.album.MatrixImageView.OnMovingListener;
import com.NationalPhotograpy.weishoot.view.album.MatrixImageView.OnSingleTapListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * @ClassName: AlbumViewPager
 * @Description: 自定义viewpager 优化了事件拦截
 * @author LinJ
 * @date 2015-1-9 下午5:33:33
 */
public class AlbumViewSinglePager extends ViewPager implements OnMovingListener {
    public final static String TAG = "AlbumViewPager";

    /** 图片加载器 优化了了缓存 */
    private com.NationalPhotograpy.weishoot.utils.imageloader.core.ImageLoader imageLoader;

    protected com.nostra13.universalimageloader.core.ImageLoader imageLoaderLocat;

    /** 加载图片配置参数 */
    private com.NationalPhotograpy.weishoot.utils.imageloader.core.DisplayImageOptions mOptions;

    private com.nostra13.universalimageloader.core.DisplayImageOptions mOptionsLocat;

    /** 当前子控件是否处理拖动状态 */
    private boolean mChildIsBeingDragged = false;

    /** 界面单击事件 用以显示和隐藏菜单栏 */
    private OnSingleTapListener onSingleTapListener;

    public AlbumViewSinglePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.imageLoader = com.NationalPhotograpy.weishoot.utils.imageloader.core.ImageLoader
                .getInstance();
        this.imageLoaderLocat = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        // 设置网络图片加载参数
        mOptions = new com.NationalPhotograpy.weishoot.utils.imageloader.core.DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).showImageForEmptyUri(R.drawable.ic_stub)
                .build();
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
        private PhotoShopBean photoShopBean;// 大图地址 如果为网络图片 则为大图url

        public ViewPagerAdapter(PhotoShopBean photoShopBean) {
            this.photoShopBean = photoShopBean;
        }

        @Override
        public int getCount() {
            return 1;
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
            final ProgressBar progress = (ProgressBar) imageLayout.findViewById(R.id.loading);
            imageView.setOnMovingListener(AlbumViewSinglePager.this);
            imageView.setOnSingleTapListener(onSingleTapListener);
            String path1 = photoShopBean.SmallPath;
            String path2 = photoShopBean.ImgName;
            imageLayout.setTag(path2);
            imageLoaderLocat.displayImage(path1, imageView, mOptionsLocat);
            imageLoader.displayImage(path2, imageView, mOptions, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progress.setVisibility(View.GONE);
                    imageView.setScaleType(ScaleType.CENTER);
                    imageView.setImageResource(R.drawable.ic_error);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progress.setVisibility(View.GONE);
                    imageView.setScaleType(ScaleType.CENTER);
                    imageView.setImageResource(R.drawable.ic_error);
                }
            });
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
