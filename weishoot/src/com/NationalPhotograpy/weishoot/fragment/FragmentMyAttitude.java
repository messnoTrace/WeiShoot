
package com.NationalPhotograpy.weishoot.fragment;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Dailyfood.meirishejian.R;
import com.NationalPhotograpy.weishoot.activity.shouye.MyAttitudeActivity;
import com.NationalPhotograpy.weishoot.bean.CommonBean;
import com.NationalPhotograpy.weishoot.net.HttpUrl;
import com.NationalPhotograpy.weishoot.utils.WeiShootToast;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class FragmentMyAttitude extends Fragment {
    private ImageView img_head;

    private PieChart mChart;

    private TextView tv_received, tv_received_num, tv_send, tv_send_num;

    private String uCode = "";

    private String sex = "";

    private static final String TAG = "FragmentMyAttitude";

    protected String[] mParties = new String[] {
            "", ""
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View createView = inflater.inflate(R.layout.fragment_my_attitude, null, false);

        initView(createView);
        getData();

        return createView;
    }

    private void initView(View createView) {
        img_head = (ImageView) createView.findViewById(R.id.img_head);
        mChart = (PieChart) createView.findViewById(R.id.chart);

        tv_received = (TextView) createView.findViewById(R.id.tv_received);
        tv_received_num = (TextView) createView.findViewById(R.id.tv_received_num);
        tv_send = (TextView) createView.findViewById(R.id.tv_send);
        tv_send_num = (TextView) createView.findViewById(R.id.tv_send_num);

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(false);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(false);

    }

    private void getData() {
        Bundle bundle = getArguments();
        int page = bundle.getInt(MyAttitudeActivity.ARG_PAGE, -1);
        uCode = bundle.getString(MyAttitudeActivity.ARG_UCODE);
        sex = bundle.getString(MyAttitudeActivity.ARG_SEX);
        getDataForNet(page);

    }

    private void getDataForNet(final int page) {

        HttpUtils httpUtils = new HttpUtils(1000 * 20);
        RequestParams params = new RequestParams();
        params.addBodyParameter("UCode", uCode);
        Log.v("TAG", "--->" + uCode);
        params.addBodyParameter("TokenKey", HttpUrl.tokenkey);
        String url = "";
        switch (page) {
            case 0:
                url = HttpUrl.GetGoodNum;
                break;
            case 1:
                url = HttpUrl.GetCommentNum;
                break;

            default:
                break;
        }

        httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<Object>() {

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                if (getActivity() == null) {
                    return;
                }
                WeiShootToast.makeErrorText(getActivity(),
                        getActivity().getString(R.string.http_timeout), WeiShootToast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                if (getActivity() == null) {
                    return;
                }
                String temp = (String) objectResponseInfo.result;
                Log.v(TAG, "rrrrrrr" + temp);
                CommonBean commonBean = new CommonBean().toJson(temp);

                if (commonBean != null) {
                    if ("1".equals(commonBean.resultCode)) {

                        setData(page, Integer.parseInt(commonBean.OtherCount),
                                Integer.parseInt(commonBean.PersonCount), "男".equals(sex) ? 1 : 2);
                    } else {
                        WeiShootToast.makeErrorText(getActivity(), commonBean.resultMsg,
                                WeiShootToast.LENGTH_SHORT).show();
                    }
                } else {
                    WeiShootToast.makeErrorText(getActivity(),
                            getActivity().getString(R.string.http_timeout),
                            WeiShootToast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // page 页数 receivedMum收到赞或评论个数 sendNum点赞或评论个数 sex性别1男2女
    private void setData(int page, int receivedMum, int sendNum, int sex) {

        switch (page) {
            case 0:
                tv_received.setText("收到的赞");
                tv_send.setText("发出的赞");
                break;
            case 1:
                tv_received.setText("收到评论");
                tv_send.setText("发出评论");
                break;

            default:
                break;
        }

        switch (sex) {
            case 1:// 男
                switch (page) {
                    case 0:
                        if (receivedMum >= sendNum) {
                            img_head.setImageResource(R.drawable.praise_man);
                        } else {
                            img_head.setImageResource(R.drawable.thumb_up_man);
                        }
                        break;
                    case 1:
                        if (receivedMum >= sendNum) {
                            img_head.setImageResource(R.drawable.good_comments_man);
                        } else {
                            img_head.setImageResource(R.drawable.comments_man);
                        }
                        break;

                    default:
                        break;
                }

                break;

            case 2:// 女
                switch (page) {
                    case 0:
                        if (receivedMum >= sendNum) {
                            img_head.setImageResource(R.drawable.praise_women);
                        } else {
                            img_head.setImageResource(R.drawable.thumb_up_women);
                        }
                        break;
                    case 1:
                        if (receivedMum >= sendNum) {
                            img_head.setImageResource(R.drawable.good_comments_women);
                        } else {
                            img_head.setImageResource(R.drawable.comments_women);
                        }
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }

        tv_received_num.setText(receivedMum + "");
        tv_send_num.setText(sendNum + "");

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yVals1.add(new Entry((float) receivedMum / (receivedMum + sendNum), 0));
        yVals1.add(new Entry((float) sendNum / (receivedMum + sendNum), 1));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < 2; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.parseColor("#81c31c"));
        colors.add(Color.parseColor("#3eaedb"));
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();

        mChart.animateY(2000, Easing.EasingOption.EaseInOutQuad);
        mChart.getLegend().setEnabled(false);

    }

}
