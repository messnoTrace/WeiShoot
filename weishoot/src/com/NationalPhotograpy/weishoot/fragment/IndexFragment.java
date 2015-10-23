package com.NationalPhotograpy.weishoot.fragment;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoDetialActivity;
import com.NationalPhotograpy.weishoot.adapter.CommomAdapter;
import com.NationalPhotograpy.weishoot.adapter.CommomViewHolder;
import com.NationalPhotograpy.weishoot.bean.BannerBean;
import com.NationalPhotograpy.weishoot.bean.BannerBean.Banner;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.storage.Constant;
import com.NationalPhotograpy.weishoot.utils.ViewUtil;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.NationalPhotograpy.weishoot.view.AutoScrollViewPager;
import com.NationalPhotograpy.weishoot.view.FinalScrollView;
import com.NationalPhotograpy.weishoot.view.FinalScrollView.OnScrollChangedListener;
import com.NationalPhotograpy.weishoot.view.LazyScrollView;
import com.NationalPhotograpy.weishoot.view.LazyScrollView.OnScrollListener;
import com.NationalPhotograpy.weishoot.view.UnScrollableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class IndexFragment  extends Fragment{
	

    private SwipeRefreshLayout sfl_index;
    private FrameLayout fl_index_content;



    private UnScrollableListView uslv_user,uslv_photo;

    private RelativeLayout bannerView;

    private AutoScrollViewPager autoViewPager;

    private Context mContext;

    private boolean isPrepared;

    private List<Banner>list_banners;
    private List<Banner>list_user;
    private List<Banner>list_photo;
    
    
    private FinalScrollView scrollview;


    private CommomAdapter<Banner>adapter_user;
    private CommomAdapter<Banner>adapter_photo;
    
    private RelativeLayout rl_loading;

    private int currentPage=1;
    
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.fragment_index, null);
		initView(view);
		bindListener();
		return view;
	}

	
	private void initView(View parentView){
        mContext=getActivity();
        rl_loading=(RelativeLayout) parentView.findViewById(R.id.rl_loading);
        
        scrollview=(FinalScrollView) parentView.findViewById(R.id.scrollview);
        
        sfl_index=(SwipeRefreshLayout) parentView.findViewById(R.id.sfl_index);
        
        fl_index_content=(FrameLayout) parentView.findViewById(R.id.fl_index_bannercontent);

        uslv_user=(UnScrollableListView) parentView.findViewById(R.id.uslv_index_user);
        uslv_photo=(UnScrollableListView) parentView.findViewById(R.id.uslv_index_photo);

        init();

        loadData(currentPage);
	}
	
	public void bindListener() {

		
		scrollview.setOnScrollChangedListener(new OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
				
			}
			
			@Override
			public void onScrollBottom() {
				rl_loading.setVisibility(View.VISIBLE);
				
				currentPage++;
				loadData(currentPage);
			}
		});
        sfl_index.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	currentPage=1;
                loadData(currentPage);
            }
        });

        uslv_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(mContext, UserInfoDetialActivity.class).putExtra("arg_ucode",list_user.get(position).Ucode).putExtra("arg_nikeName",list_user.get(position).Title).putExtra("arg_userHead", list_user.get(position).ImgUrl));
            }
        });

        uslv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
	
	
	 private void init(){
	        list_banners=new ArrayList<Banner>();
	        
	        
	        list_user=new ArrayList<Banner>();
	        list_photo=new ArrayList<Banner>();


	        adapter_photo=new CommomAdapter<Banner>(mContext,list_photo,R.layout.item_index_user) {
	            @Override
	            public void convert(final CommomViewHolder mHolder, Banner item, int position) {

	            mHolder.setVisiblility(R.id.tv_item_indexuser_name, View.INVISIBLE);
	               final ImageView imageView= (ImageView) mHolder.getConvertView().findViewById(R.id.iv_item_indexuser_pic);
	               final RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) imageView.getLayoutParams();

	                int width=Constant.SCREEN_WIDTH/2-20;
	                params.width=width;


	                 com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(item.ImgUrl, new ImageLoadingListener() {
	                     @Override
	                     public void onLoadingStarted(String s, View view) {

	                     }

	                     @Override
	                     public void onLoadingFailed(String s, View view, FailReason failReason) {

	                     }

	                     @Override
	                     public void onLoadingComplete(String s, View view, Bitmap bitmap) {

	                         params.height = bitmap.getHeight();
	                         imageView.setImageBitmap(bitmap);
	                         mHolder.setVisiblility(R.id.tv_item_indexuser_name, View.GONE);
	                     }

	                     @Override
	                     public void onLoadingCancelled(String s, View view) {

	                     }
	                 });

	                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(item.ImgUrl, imageView);


	            }
	        };

	        adapter_user=new CommomAdapter<Banner>(mContext,list_user,R.layout.item_index_user) {
	            @Override
	            public void convert(CommomViewHolder mHolder, Banner item, int position) {
	                ImageView imageView= (ImageView) mHolder.getConvertView().findViewById(R.id.iv_item_indexuser_pic);
	                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) imageView.getLayoutParams();
	                int width=Constant.SCREEN_WIDTH/2-20;
	                params.width=width;
	                params.height=width;
	                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(item.ImgUrl, imageView);
	                mHolder.setText(R.id.tv_item_indexuser_name, item.Title);
	            }
	        };

	        uslv_user.setAdapter(adapter_user);
	        uslv_photo.setAdapter(adapter_photo);

	    }
	    private void loadData(int page)
	    {

	        loadBanner();
	        loadUser(page);
	        loadPhoto(page);
	    }

	    private void loadBanner()
	    {
	    	
	    	  HttpUtils httpUtils = new HttpUtils(1000 * 20);
	          RequestParams params = new RequestParams();
	          params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
	          httpUtils.send(HttpMethod.POST, HttpUrl.GetBanner, params, new RequestCallBack<Object>() {

	              @Override
	              public void onFailure(HttpException e, String s) {
	                  e.printStackTrace();
	                  if (getActivity() == null) {
	                      return;
	                  }
	                  sfl_index.setRefreshing(false);
	                  WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout),
	                          WeiShootToast.LENGTH_SHORT).show();
	              }

	              @Override
	              public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
	                  if (getActivity() == null) {
	                      return;
	                  }
	                  sfl_index.setRefreshing(false);
	                  String temp = (String) objectResponseInfo.result;
	                  BannerBean bannerBean = new Gson().fromJson(temp, new TypeToken<BannerBean>() {
	                  }.getType());
	                  
	                  if(bannerBean!=null){
	                	  List<Banner> data=bannerBean.data;
	                	  initBanner(data);
	                  }

	              }
	          });
	    	
	    	

	    }

	    private void loadUser(final int page){
	    	 HttpUtils httpUtils = new HttpUtils(1000 * 20);
	          RequestParams params = new RequestParams();
	          params.addBodyParameter("pageSize","10");
	          params.addBodyParameter("page",page+"");
	          params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
	          httpUtils.send(HttpMethod.POST, HttpUrl.GetRCMDUserInfo, params, new RequestCallBack<Object>() {

	              @Override
	              public void onFailure(HttpException e, String s) {
	                  e.printStackTrace();
	                  if (getActivity() == null) {
	                      return;
	                  }
	                  sfl_index.setRefreshing(false);
	                  rl_loading.setVisibility(View.GONE);
	                  WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout),
	                          WeiShootToast.LENGTH_SHORT).show();
	              }

	              @Override
	              public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
	                  if (getActivity() == null) {
	                      return;
	                  }
	                  rl_loading.setVisibility(View.GONE);
	                  sfl_index.setRefreshing(false);
	                  String temp = (String) objectResponseInfo.result;
	                  BannerBean bannerBean = new Gson().fromJson(temp, new TypeToken<BannerBean>() {
	                  }.getType());
	                  
	     
	                  if(bannerBean!=null){
	                	  
	                	  if(list_user!=null&&list_user.size()>0&&currentPage!=1){
	                		  list_user.addAll(bannerBean.data);
	                	  }else {
	                		  
	                		  list_user=bannerBean.data;
						}
	                	  adapter_user.setData(list_user);
	                  }

	              }
	          });

	    }
	    private void loadPhoto(final int page){
	    	 HttpUtils httpUtils = new HttpUtils(1000 * 20);
	          RequestParams params = new RequestParams();
	          params.addBodyParameter("pageSize","10");
	          params.addBodyParameter("page",page+"");
	          params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
	          httpUtils.send(HttpMethod.POST, HttpUrl.GetRCMDPhotoInfo, params, new RequestCallBack<Object>() {

	              @Override
	              public void onFailure(HttpException e, String s) {
	                  e.printStackTrace();
	                  if (getActivity() == null) {
	                      return;
	                  }
	                  rl_loading.setVisibility(View.GONE);
	                  sfl_index.setRefreshing(false);
	                  WeiShootToast.makeErrorText(getActivity(), getString(R.string.http_timeout),
	                          WeiShootToast.LENGTH_SHORT).show();
	              }

	              @Override
	              public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
	                  if (getActivity() == null) {
	                      return;
	                  }
	                  rl_loading.setVisibility(View.GONE);
	                  sfl_index.setRefreshing(false);
	                  String temp = (String) objectResponseInfo.result;
	                  BannerBean bannerBean = new Gson().fromJson(temp, new TypeToken<BannerBean>() {
	                  }.getType());
	                  if(bannerBean!=null){
	                	  
	                	  if(list_photo!=null&&list_photo.size()>0&&currentPage!=1){
	                		  list_photo.addAll(bannerBean.data);
	                	  }else {
	                		  
	                		  list_photo=bannerBean.data;
						}
	                	  adapter_photo.setData(list_photo);
	                  }
	              }
	          });

	    }

	    /**
	     * init banner
	     */
	    private void initBanner(List<Banner> list){

	        if(bannerView!=null){
	            bannerView.removeAllViews();
	        }else {
	            bannerView=new RelativeLayout(mContext);
	        }


	        double width = Constant.SCREEN_WIDTH;

	        double height=Constant.SCREEN_HEIGHT/8*2;
	        LinearLayout ll = new LinearLayout(mContext);
	        ll.setOrientation(LinearLayout.HORIZONTAL);
	        ll.setPadding(ViewUtil.dip2px(mContext,10), 0, ViewUtil.dip2px(mContext,10), 0);

	        // 初始化点
	        ArrayList<View> dots = new ArrayList<View>();
	        ArrayList<String> pics = new ArrayList<String>();

	        //TODO 测试，加载本地图片
	        int[]respic=new int[list.size()];
	        String[] titles = new String[list.size()];

	        // 初始化标题
	        TextView tv = new TextView(mContext);
	        tv.setTextColor(Color.BLACK);
	        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
	        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
	        tv.setLines(1);
	        tv.setEllipsize(TextUtils.TruncateAt.END);
	        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
	        lp2.weight = 1;
	        ll.addView(tv, lp2);

	        lp2 = new LinearLayout.LayoutParams(ViewUtil.dip2px(mContext, 6), ViewUtil.dip2px(mContext,6));
	        lp2.gravity = Gravity.CENTER_VERTICAL;
	        lp2.leftMargin = ViewUtil.dip2px(mContext,4);
	        for (int i = 0; i < list.size(); i++) {
	            pics.add(list.get(i).ImgUrl);
//	            respic[i]=R.drawable.ic_launcher;
	            //TODO 将标题注释掉
//				titles[i] = list.get(i).getTitle();
	            titles[i]="";
	            View view = new View(mContext);
	            view.setBackgroundResource(R.drawable.ic_focus_select);
	            ll.addView(view, lp2);
	            dots.add(view);
	        }

	        autoViewPager = new AutoScrollViewPager(mContext, dots, R.drawable.ic_focus_select, R.drawable.ic_focus, new AutoScrollViewPager.OnPagerClickCallback() {
	            @Override
	            public void onPagerClick(int position) {

	                // item点击事件
	            }
	        });

	        //TODO
	        autoViewPager.setUriList(pics);
//	        autoViewPager.setResImageIds(respic);
	        autoViewPager.setTitle(tv, titles);

	        autoViewPager.startRoll();
	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) height);
	        bannerView.addView(autoViewPager, lp);

	        // 添加标题和点
	        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams((int) width, ViewUtil.dip2px(mContext,30));
	        lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	        bannerView.addView(ll, lp3);
	        bannerView.setGravity(Gravity.CENTER_HORIZONTAL);
	        fl_index_content.removeAllViews();
	        fl_index_content.addView(bannerView);


	    }
}
