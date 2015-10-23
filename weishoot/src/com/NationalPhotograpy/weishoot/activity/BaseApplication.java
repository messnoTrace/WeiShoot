
package com.NationalPhotograpy.weishoot.activity;

import io.rong.imkit.RongIM;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.CrashHandler;
import com.NationalPhotograpy.weishoot.utils.UserInfo.DbCitys;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        SDKInitializer.initialize(this);
        initRongIM();
        initLocation();
        initImageLoader(this);
        initImageLoaderLocat(this);
        if (!DbCitys.isExists()) {
            DbCitys.importCitysInternal(this);
        }
        ;
    }

    private void initRongIM() {
        /*
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * xxx.xxx.xxx 是您的主进程或者使用了 RongIM 的其他进程
         */
        if ("com.NationalPhotograpy.weishoot".equals(getCurProcessName(getApplicationContext()))
                || "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /* IMKit SDK调用第一步 初始化 */
            RongIM.init(this);

            /* 必须在使用 RongIM 的进程注册回调、注册自定义消息等 */
            // if
            // ("xxx.xxx.xxx".equals(getCurProcessName(getApplicationContext())))
            // {
            //
            // RongIM.setUserInfoProvider(this, true);
            // RongIM.setConversationBehaviorListener(this);
            // RongIM.setConversationListBehaviorListener(this);
            //
            // }
        }
    }

    /* 一个获得当前进程的名字的方法 */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void initLocation() {
        final LocationClient mLocationClient = new LocationClient(this.getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000m
        option.setIsNeedAddress(true);// 设置反地理编码
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                StaticInfo.BDLatitude = location.getLatitude();
                StaticInfo.BDLongitude = location.getLongitude();
                mLocationClient.stop();
            }
        });
        mLocationClient.start();
    }

    public static void initImageLoader(Context context) {
        com.NationalPhotograpy.weishoot.utils.imageloader.core.DisplayImageOptions defaultOptions = new com.NationalPhotograpy.weishoot.utils.imageloader.core.DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();
        com.NationalPhotograpy.weishoot.utils.imageloader.core.ImageLoaderConfiguration config = new com.NationalPhotograpy.weishoot.utils.imageloader.core.ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(
                        new com.NationalPhotograpy.weishoot.utils.imageloader.cache.disc.naming.Md5FileNameGenerator())
                .tasksProcessingOrder(
                        com.NationalPhotograpy.weishoot.utils.imageloader.core.assist.QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        com.NationalPhotograpy.weishoot.utils.imageloader.core.ImageLoader.getInstance().init(
                config);
    }

    public static void initImageLoaderLocat(Context context) {
        com.nostra13.universalimageloader.core.DisplayImageOptions defaultOptions = new com.nostra13.universalimageloader.core.DisplayImageOptions.Builder()
                .cacheInMemory(true) // 1.8.6包使用时候，括号里面传入参数true
                .cacheOnDisc(true) // 1.8.6包使用时候，括号里面传入参数true
                .build();
        com.nostra13.universalimageloader.core.ImageLoaderConfiguration config = new com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(
                        new com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator())
                .tasksProcessingOrder(
                        com.nostra13.universalimageloader.core.assist.QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

}
