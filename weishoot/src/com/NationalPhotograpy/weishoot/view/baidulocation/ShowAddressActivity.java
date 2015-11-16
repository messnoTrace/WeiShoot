
package com.NationalPhotograpy.weishoot.view.baidulocation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ZoomControls;

import com.Dailyfood.meirishejian.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class ShowAddressActivity extends Activity {

    private MapView mMapView = null;

    private BaiduMap mBaiduMap = null;

    /**
     * 当前经纬度信息
     */
    private double lat, lon;

    private String addressName;

    /**
     * 缩放比例
     */
    private float zoom = 18f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_show_address);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.mapview_show);
        // 隐藏比例尺控件
        int count = mMapView.getChildCount();
        View scale = null;
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                scale = child;
                break;
            }
        }
        scale.setVisibility(View.GONE);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            lat = Double.parseDouble(getIntent().getStringExtra("lat"));
            lon = Double.parseDouble(getIntent().getStringExtra("lon"));
            addressName = getIntent().getStringExtra("address");
            showMap(lat, lon);
        }
    }

    private void setListener() {
        mBaiduMap.setOnMapStatusChangeListener(mapStatuslistener);
    }

    /**
     * 显示地图位置
     */
    private void showMap(double lat, double lon) {
        // 定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.map_maker);

        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(zoom).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        // 改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 构建MarkerOption，用于在地图上添加Marker
        // OverlayOptions option = new MarkerOptions().position(point);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    private OnMapStatusChangeListener mapStatuslistener = new OnMapStatusChangeListener() {
        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         * 
         * @param status 地图状态改变开始时的地图状态
         */
        public void onMapStatusChangeStart(MapStatus status) {
        }

        /**
         * 地图状态变化中
         * 
         * @param status 当前地图状态
         */
        public void onMapStatusChange(MapStatus status) {
        }

        /**
         * 地图状态改变结束
         * 
         * @param status 地图状态改变结束后的地图状态
         */
        public void onMapStatusChangeFinish(MapStatus status) {
            zoom = status.zoom;
            // 滑动返回新的经纬度,并重新计算页数
            lat = status.target.latitude;
            lon = status.target.longitude;
        }
    };
}
