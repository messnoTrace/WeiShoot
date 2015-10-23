
package com.NationalPhotograpy.weishoot.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.NationalPhotograpy.weishoot.R;
import com.NationalPhotograpy.weishoot.activity.shouye.UserInfoActivity;
import com.NationalPhotograpy.weishoot.adapter.shouye.CityAdapter;
import com.NationalPhotograpy.weishoot.adapter.shouye.CityAdapter.CityItemClickListener;
import com.NationalPhotograpy.weishoot.adapter.shouye.CountyAdapter;
import com.NationalPhotograpy.weishoot.adapter.shouye.CountyAdapter.CountyItemClickListener;
import com.NationalPhotograpy.weishoot.adapter.shouye.OcAdapter;
import com.NationalPhotograpy.weishoot.adapter.shouye.OcAdapter.ItemClickListener;
import com.NationalPhotograpy.weishoot.adapter.shouye.ProvinceAdapter;
import com.NationalPhotograpy.weishoot.adapter.shouye.ProvinceAdapter.ProItemClickListener;
import com.NationalPhotograpy.weishoot.bean.CityBean;
import com.NationalPhotograpy.weishoot.bean.OcBean;
import com.NationalPhotograpy.weishoot.bean.ProvinceBean;
import com.NationalPhotograpy.weishoot.utils.UserInfo.DbCitys;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserInfoListViewDialog extends Dialog implements ProItemClickListener,
        CityItemClickListener, CountyItemClickListener {
    private TextView dialog_title_view;

    private ListView dialog_lv1, dialog_lv2, dialog_lv3;

    private View dialog_lv_divider2, dialog_lv_divider1;

    private Context mContext;

    private ProvinceAdapter pAdapter;

    private CityAdapter cAdapter;

    private CountyAdapter countyAdapter;

    private List<ProvinceBean> provinceBeans;

    private List<CityBean> cityBeans = new ArrayList<CityBean>();

    private TextView textView;

    // private

    public UserInfoListViewDialog(Context context, String flag, String data, TextView tView,
            List<ProvinceBean> provinceBeans) {
        super(context, R.style.EditDialog);
        this.mContext = context;
        this.textView = tView;
        if (provinceBeans != null) {
            this.provinceBeans = provinceBeans;
        }

        initView();
        initData(flag, data, tView);

    }

    private void initView() {

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_listview_userinfo,
                null);
        dialog_title_view = (TextView) mView.findViewById(R.id.dialog_title_view);
        dialog_lv1 = (ListView) mView.findViewById(R.id.dialog_lv1);
        dialog_lv2 = (ListView) mView.findViewById(R.id.dialog_lv2);
        dialog_lv3 = (ListView) mView.findViewById(R.id.dialog_lv3);
        dialog_lv_divider2 = mView.findViewById(R.id.dialog_lv_divider2);
        dialog_lv_divider1 = mView.findViewById(R.id.dialog_lv_divider1);
        super.setContentView(mView);
    }

    private void initData(String flag, String data, final TextView tView) {
        if ("oc".equals(flag)) {
            OcBean ocBean = new Gson().fromJson(data, new TypeToken<OcBean>() {
            }.getType());
            if (ocBean == null) {
                return;
            }
            dialog_title_view.setText("请选择从事行业");
            dialog_lv1.setVisibility(View.VISIBLE);
            OcAdapter ocAdapter = new OcAdapter(mContext, ocBean.data);
            dialog_lv1.setAdapter(ocAdapter);
            ocAdapter.setOnClikListener(new ItemClickListener() {

                @Override
                public void click(String name, String id) {
                    tView.setText(name);
                    if (mContext instanceof UserInfoActivity) {
                        ((UserInfoActivity) mContext).requestUpDataUserInfoExpand("Industry", id);
                    }
                    dismiss();
                }
            });
            show();
        } else if ("city".equals(flag)) {
            dialog_title_view.setText("请选择所在城市");
            dialog_lv1.setVisibility(View.VISIBLE);
            dialog_lv2.setVisibility(View.VISIBLE);
            dialog_lv3.setVisibility(View.VISIBLE);
            dialog_lv_divider1.setVisibility(View.VISIBLE);
            dialog_lv_divider2.setVisibility(View.VISIBLE);

            show();
            initCity();
        }
    }

    private void initCity() {
        pAdapter = new ProvinceAdapter(mContext, provinceBeans);
        pAdapter.setOnClikListener(this);
        dialog_lv1.setAdapter(pAdapter);
        cAdapter = new CityAdapter(mContext);
        cAdapter.setOnClikListener(this);
        dialog_lv2.setAdapter(cAdapter);
        if (provinceBeans.size() > 0) {
            cAdapter.setData(provinceBeans.get(0).CityBean);
        }

        countyAdapter = new CountyAdapter(mContext);
        countyAdapter.setOnClikListener(this);
        dialog_lv3.setAdapter(countyAdapter);
        if (provinceBeans.size() > 0) {
            countyAdapter.setData(provinceBeans.get(0).CityBean.get(0).CountyBean);
        }
    }

    @Override
    public void proClick(int position) {
        cityBeans.clear();
        cityBeans.addAll(provinceBeans.get(position).CityBean);
        cAdapter.setData(cityBeans);

    }

    @Override
    public void cityClick(int position) {
        countyAdapter.setData(cityBeans.get(position).CountyBean);
    }

    @Override
    public void countyClick(String name, String id) {
        textView.setText(name);
        if (mContext instanceof UserInfoActivity) {
            ((UserInfoActivity) mContext).requestUpDataUserInfo("City", id);
        }
        dismiss();
    }

}
