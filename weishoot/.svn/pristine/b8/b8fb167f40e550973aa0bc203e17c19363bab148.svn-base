
package com.NationalPhotograpy.weishoot.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.NationalPhotograpy.weishoot.storage.StaticInfo;

public class BitmapUtils1 {

    /**
     * 以最省内存的方式读取本地资源的图片
     * 
     * @see imageButton_fav.setImageResource(R.drawable.guide_fav_1)
     *      </br>修改为</br>
     *      imageButton_fav.setImageBitmap(BitmapUtils.readBitMap(this,
     *      R.drawable.guide_fav_1));
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        try {
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is, null, opt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * 保存bitmap到本地
     */
    public static String saveBitmap(Bitmap bm) {
        String picName = getPhotoFileName();
        String fileStr = "";
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fileStr = Environment.getExternalStorageDirectory() + "/weishoot/picture/";
            file = new File(Environment.getExternalStorageDirectory() + "/weishoot/picture/",
                    picName);
        } else {
            fileStr = "/data/data/weishoot/picture/";
            file = new File("/data/data/weishoot/picture/", picName);
        }
        if (!file.exists()) {
            checkOrSaveDir(fileStr);
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bm.recycle();
            return file.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 多层目录创建
     * 
     * @param path
     */
    public static void checkOrSaveDir(String path) {
        String[] dir = path.split("/");
        String dist = dir[0];
        for (int i = 1; i < dir.length; i++) {
            dist += "/" + dir[i];
            File mkdir = new File(dist);
            if (mkdir.exists()) {
            } else {
                mkdir.mkdir();
            }
        }

    }

    /**
     * @param folder 例："/weishoot/cache/"
     * @param picName 例："yingpian1.png"
     */
    public static File createCameraPath(String folder, String picName) {
        String fileStr = "";
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fileStr = Environment.getExternalStorageDirectory() + folder;
            file = new File(Environment.getExternalStorageDirectory() + folder, picName);
        } else {
            fileStr = "/data/data" + folder;
            file = new File("/data/data" + folder, picName);
        }
        if (!file.exists()) {
            checkOrSaveDir(fileStr);
        }
        if (file.exists()) {
            file.delete();
        }
        return file;
    }

    /**
     * 图片反转
     * 
     * @param img
     * @return
     */
    public Bitmap toturn(Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90); /* 翻转90度 */
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    // 使用系统当前日期加以调整作为照片的名称
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpeg";
    }

    public static Bitmap decodebitmap(Context context, Uri picUri) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 如果值设为true，那么将不返回实际的bitmap，也不给其分配内存空间，这样就避免了内存溢出。
        Bitmap bitmap = BitmapFactory.decodeFile(picUri.getPath(), options);
        int realwidth = options.outWidth;
        int scal = realwidth / StaticInfo.widthPixels;
        if (scal <= 0) {
            scal = 1;
        }
        options.inSampleSize = scal;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(picUri.getPath(), options);
        ExifInterface exifInterface;
        int rotate = 0;
        try {
            exifInterface = new ExifInterface(picUri.getPath());
            int result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rotate > 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(rotate);
            Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            bitmap = null;
            return rotateBitmap;
        }
        return bitmap;

    }

    private String setImage(File file) {
        ExifInterface exifInterface;
        int rotate = 0;
        try {
            exifInterface = new ExifInterface(file.getPath());
            int result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rotate > 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(rotate);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            bitmap = null;
            if (rotateBitmap != null) {
                String string = BitmapUtils1.saveBitmap(rotateBitmap);
                rotateBitmap.recycle();
                rotateBitmap = null;
                return string;
            }
        }
        return file.getPath();
    }
}
