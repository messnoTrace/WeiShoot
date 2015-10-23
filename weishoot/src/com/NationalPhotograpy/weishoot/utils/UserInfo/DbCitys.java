
package com.NationalPhotograpy.weishoot.utils.UserInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.NationalPhotograpy.weishoot.R.id;
import com.NationalPhotograpy.weishoot.bean.CityBean;
import com.NationalPhotograpy.weishoot.bean.CountyBean;
import com.NationalPhotograpy.weishoot.bean.ProvinceBean;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbCitys {

    public static void importCitysInternal(final Context context) {
        new Thread() {
            public void run() {
                boolean result = copyFile(context, "city.db", "city.db");
            }
        }.start();
    }

    public static boolean copyFile(Context context, String srcFilename, String destFilename) {
        try {
            FileOutputStream fos = context.openFileOutput(destFilename, Context.MODE_PRIVATE);
            AssetManager am = context.getAssets();
            InputStream is = am.open(srcFilename);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isExists() {
        File file = new File("/data/data/com.NationalPhotograpy.weishoot/files/city.db");

        return file.exists();
    }

    public static List<ProvinceBean> getCitys() {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(
                "/data/data/com.NationalPhotograpy.weishoot/files/city.db", null, 0);

        List<ProvinceBean> provinceBeans = new ArrayList<ProvinceBean>();
        // 修改结果集中的id列的列名为_id
        Cursor province = database.rawQuery(
                "select CityID,CityName from TabCity where FatherID = ?", new String[] {
                    "0"
                });
        while (province.moveToNext()) {
            ProvinceBean bean = new ProvinceBean();
            bean.id = province.getInt(province.getColumnIndex("CityID")) + "";
            bean.name = province.getString(province.getColumnIndex("CityName"));
            Cursor city = database.rawQuery(
                    "select CityID,CityName from TabCity where FatherID = ?", new String[] {
                        bean.id
                    });
            while (city.moveToNext()) {
                CityBean cityBean = new CityBean();
                cityBean.id = city.getInt(city.getColumnIndex("CityID")) + "";
                cityBean.name = city.getString(city.getColumnIndex("CityName"));
                bean.CityBean.add(cityBean);
//                Log.v("市级", "-----=====>" + cityBean.id + "   " + cityBean.name);
                Cursor country = database.rawQuery(
                        "select CityID,CityName from TabCity where FatherID = ?", new String[] {
                            cityBean.id
                        });
                while (country.moveToNext()) {
                    CountyBean countyBean = new CountyBean();
                    countyBean.id = country.getInt(country.getColumnIndex("CityID")) + "";
                    countyBean.name = country.getString(country.getColumnIndex("CityName"));
                    cityBean.CountyBean.add(countyBean);
//                    Log.v("县级", "-----=====>" + countyBean.id + "   " + countyBean.name);
                }
                country.close();

            }
            city.close();
//            Log.v("省级", "-----=====>" + bean.id + "   " + bean.name);
            provinceBeans.add(bean);
        }
        province.close();
        database.close();

        return provinceBeans;

    }

}
