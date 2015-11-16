
package com.NationalPhotograpy.weishoot.view.baidulocation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.storage.StaticInfo;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.view.baidulocation.BaiduMapUtilByRacer.GeoCodePoiListener;
import com.NationalPhotograpy.weishoot.view.baidulocation.BaiduMapUtilByRacer.PoiSearchListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class SelectAddressActivity extends Activity {

    private static Context mContext;

    private LocationBean mLocationBean;

    // 定位poi地名信息数据源
    private List<PoiInfo> aroundPoiList;

    private AroundPoiAdapter mAroundPoiAdapter;

    // 搜索当前城市poi数据源
    private static List<LocationBean> searchPoiList;

    private SearchPoiAdapter mSearchPoiAdapter;

    private BaiduMap mBaiduMap;

    // 控件
    private MapView mMapView;

    private EditText etMLCityPoi;

    private TextView tvShowLocation;

    private LinearLayout llMLMain;

    private ListView lvAroundPoi, lvSearchPoi;

    private ProgressBar progressLoading;

    private Button btMapZoomIn, btMapZoomOut;

    // 標識
    public static final int SHOW_MAP = 0;

    private static final int SHOW_SEARCH_RESULT = 1;

    private LinearLayout layout_back;

    private Button btn_right;

    private String ActivityType;

    private String addressNum, addressName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview_location_poi);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        SelectAddressActivity.mContext = this;
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        etMLCityPoi = (EditText) findViewById(R.id.etMLCityPoi);
        tvShowLocation = (TextView) findViewById(R.id.tvShowLocation);
        lvAroundPoi = (ListView) findViewById(R.id.lvPoiList);
        lvSearchPoi = (ListView) findViewById(R.id.lvMLCityPoi);
        progressLoading = (ProgressBar) findViewById(R.id.progressLoading);
        btMapZoomIn = (Button) findViewById(R.id.btMapZoomIn);
        btMapZoomOut = (Button) findViewById(R.id.btMapZoomOut);
        llMLMain = (LinearLayout) findViewById(R.id.llMLMain);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mMapView);
        BaiduMapUtilByRacer.goneMapViewChild(mMapView, true, true);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        mBaiduMap.setOnMapClickListener(mapOnClickListener);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);// 缩放手势
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }

    private void initData() {
        ActivityType = getIntent().getStringExtra("ActivityType");
        LatLng ptCenter = new LatLng(StaticInfo.BDLatitude, StaticInfo.BDLongitude);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(ptCenter));
        // 反Geo搜索
        reverseGeoCode(ptCenter, true);
        lvAroundPoi.setVisibility(View.GONE);
        progressLoading.setVisibility(View.VISIBLE);
    }

    private void setListener() {
        layout_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (addressNum != null && addressName != null) {
                    Intent it = new Intent();
                    it.putExtra("addressName", addressName);
                    it.putExtra("addressNum", addressNum);
                    if ("UPLOAD".equals(ActivityType)) {
                        setResult(Constant.RESULT_LOCATION_UPLOAD, it);
                    }
                    finish();
                } else {
                    WeiShootToast.makeErrorText(mContext, "请选择一个位置", WeiShootToast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        etMLCityPoi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etMLCityPoi.getText().toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                }
            }
        });
        etMLCityPoi.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (cs.toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                } else {
                    if (searchPoiList != null) {
                        searchPoiList.clear();
                    }
                    showMapOrSearch(SHOW_MAP);
                    hideSoftinput(mContext);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btMapZoomIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.zoomInMapView(mMapView);
            }
        });
        btMapZoomOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.zoomOutMapView(mMapView);
            }
        });
        lvAroundPoi.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.moveToTarget(aroundPoiList.get(arg2).location.latitude,
                        aroundPoiList.get(arg2).location.longitude, mBaiduMap);
                tvShowLocation.setText(aroundPoiList.get(arg2).name);
                addressNum = aroundPoiList.get(arg2).location.latitude + ","
                        + aroundPoiList.get(arg2).location.longitude;
                addressName = aroundPoiList.get(arg2).name;
                mAroundPoiAdapter.setNewList(arg2);
            }
        });
        lvSearchPoi.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // Geo搜索
                // mGeoCoder.geocode(new GeoCodeOption().city(
                // searchPoiList.get(arg2).getCity()).address(
                // searchPoiList.get(arg2).getLocName()));
                hideSoftinput(mContext);
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.moveToTarget(searchPoiList.get(arg2).getLatitude(),
                        searchPoiList.get(arg2).getLongitude(), mBaiduMap);
                tvShowLocation.setText(searchPoiList.get(arg2).getLocName());
                // 反Geo搜索
                reverseGeoCode(
                        new LatLng(searchPoiList.get(arg2).getLatitude(), searchPoiList.get(arg2)
                                .getLongitude()), false);
                lvAroundPoi.setVisibility(View.GONE);
                progressLoading.setVisibility(View.VISIBLE);
                showMapOrSearch(SHOW_MAP);
            }
        });
    }

    // 显示地图界面亦或搜索结果界面
    private void showMapOrSearch(int index) {
        if (index == SHOW_SEARCH_RESULT) {
            llMLMain.setVisibility(View.GONE);
            lvSearchPoi.setVisibility(View.VISIBLE);
        } else {
            lvSearchPoi.setVisibility(View.GONE);
            llMLMain.setVisibility(View.VISIBLE);
            if (searchPoiList != null) {
                searchPoiList.clear();
            }
        }
    }

    public void getPoiByPoiSearch() {
        BaiduMapUtilByRacer.getPoiByPoiSearch(mLocationBean.getCity(), etMLCityPoi.getText()
                .toString().trim(), 0, new PoiSearchListener() {

            @Override
            public void onGetSucceed(List<LocationBean> locationList, PoiResult res) {
                if (etMLCityPoi.getText().toString().trim().length() > 0) {
                    if (searchPoiList == null) {
                        searchPoiList = new ArrayList<LocationBean>();
                    }
                    searchPoiList.clear();
                    searchPoiList.addAll(locationList);
                    updateCityPoiListAdapter();
                }
            }

            @Override
            public void onGetFailed() {
                Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reverseGeoCode(LatLng ll, final boolean isShowTextView) {
        BaiduMapUtilByRacer.getPoisByGeoCode(ll.latitude, ll.longitude, new GeoCodePoiListener() {

            @Override
            public void onGetSucceed(LocationBean locationBean, List<PoiInfo> poiList) {
                mLocationBean = (LocationBean) locationBean.clone();
                Toast.makeText(
                        mContext,
                        mLocationBean.getProvince() + "-" + mLocationBean.getCity() + "-"
                                + mLocationBean.getDistrict() + "-" + mLocationBean.getStreet()
                                + "-" + mLocationBean.getStreetNum(), Toast.LENGTH_SHORT).show();
                // mBaiduMap.setMapStatus(MapStatusUpdateFactory
                // .newLatLng(new LatLng(locationBean
                // .getLatitude(), locationBean
                // .getLongitude())));
                if (isShowTextView) {
                    tvShowLocation.setText(locationBean.getLocName());
                }
                if (aroundPoiList == null) {
                    aroundPoiList = new ArrayList<PoiInfo>();
                }
                aroundPoiList.clear();
                if (poiList != null) {
                    aroundPoiList.addAll(poiList);
                } else {
                    Toast.makeText(mContext, "该周边没有热点", Toast.LENGTH_SHORT).show();
                }
                updatePoiListAdapter(aroundPoiList, -1);
            }

            @Override
            public void onGetFailed() {
                Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (llMLMain.getVisibility() == View.GONE) {
            showMapOrSearch(SHOW_MAP);
        } else {
            this.finish();
        }
    };

    OnMapClickListener mapOnClickListener = new OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         * 
         * @param point 点击的地理坐标
         */
        public void onMapClick(LatLng point) {
            hideSoftinput(mContext);
        }

        /**
         * 地图内 Poi 单击事件回调函数
         * 
         * @param poi 点击的 poi 信息
         */
        public boolean onMapPoiClick(MapPoi poi) {
            return false;
        }
    };

    private boolean isCanUpdateMap = true;

    OnMapStatusChangeListener mapStatusChangeListener = new OnMapStatusChangeListener() {
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
            if (isCanUpdateMap) {
                LatLng ptCenter = new LatLng(status.target.latitude, status.target.longitude);
                // 反Geo搜索
                reverseGeoCode(ptCenter, true);
                lvAroundPoi.setVisibility(View.GONE);
                progressLoading.setVisibility(View.VISIBLE);
            } else {
                isCanUpdateMap = true;
            }
        }
    };

    /**
     * 隐藏软键盘
     * 
     * @param view
     */
    private void hideSoftinput(Context mContext) {
        InputMethodManager manager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(etMLCityPoi.getWindowToken(), 0);
        }
    }

    // 刷新热门地名列表界面的adapter
    private void updatePoiListAdapter(List<PoiInfo> list, int index) {
        progressLoading.setVisibility(View.GONE);
        lvAroundPoi.setVisibility(View.VISIBLE);
        if (mAroundPoiAdapter == null) {
            mAroundPoiAdapter = new AroundPoiAdapter(mContext, list);
            lvAroundPoi.setAdapter(mAroundPoiAdapter);
        } else {
            mAroundPoiAdapter.setNewList(list);
        }
    }

    // 刷新当前城市兴趣地点列表界面的adapter
    private void updateCityPoiListAdapter() {
        if (mSearchPoiAdapter == null) {
            mSearchPoiAdapter = new SearchPoiAdapter(mContext, searchPoiList);
            lvSearchPoi.setAdapter(mSearchPoiAdapter);
        } else {
            mSearchPoiAdapter.notifyDataSetChanged();
        }
        showMapOrSearch(SHOW_SEARCH_RESULT);
    }

    @Override
    protected void onDestroy() {
        mLocationBean = null;
        lvAroundPoi = null;
        lvSearchPoi = null;
        btMapZoomIn.setBackgroundResource(0);
        btMapZoomIn = null;
        btMapZoomOut.setBackgroundResource(0);
        btMapZoomOut = null;
        if (aroundPoiList != null) {
            aroundPoiList.clear();
            aroundPoiList = null;
        }
        mAroundPoiAdapter = null;
        if (searchPoiList != null) {
            searchPoiList.clear();
            searchPoiList = null;
        }
        mSearchPoiAdapter = null;
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
        if (etMLCityPoi != null) {
            etMLCityPoi.setBackgroundResource(0);
            etMLCityPoi = null;
        }
        super.onDestroy();
        System.gc();
    }
}
