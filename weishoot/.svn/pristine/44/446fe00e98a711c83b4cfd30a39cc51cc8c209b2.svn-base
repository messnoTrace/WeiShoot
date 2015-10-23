package com.NationalPhotograpy.weishoot.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;

/**
 * 图片工具类
 */
public class ImageUtil {
	
	// 图片最大、最小宽高比
    private static final float MAX_RAIDO = 10.0f / 3.0f;
    private static final float MIN_RAIDO = 3.0f / 10.0f;

    // 在max,min之外的图片最大宽度，高清上传
    private static final int OVER_LENGTH_MAX_WIDTH = 1600;

    // 在max,min之外的图片最大宽度，普通上传
    private static final int OVER_LENGTH_MAX_WIDTH_DEFINITION = 1280;

    // 在max,min之外的图片最大宽度,高清上传,4.0以上
    private static final int HQ_MAX_WIDTH_ABOVE_FOUR = 1080;
    // 在max,min之外的图片最大宽度,高清上传,4.0以下
    private static final int HQ_MAX_WIDTH_BELOW_FOUR = 1080;
    // 在max,min宽高比之间的图片，非高清上传 4.0以下
    private static final int NORMAL_MAX_WIDTH_BELOW_FOUR = 640;
    // 在max,min宽高比之间的图片,，非高清上传4.0以上
    private static final int NORMAL_MAX_WIDTH_ABOVE_FOUR = 640;
    // 在max,min宽高比之间的图片,，非高清上传4.0以上
    private static final int LONG_IMAGE_MAX_HEIGHT = 10240;
    public static final String TAG = "ImageUtil";
    private static final int MAX_QUALITY = 720;
    private static final Matrix IDENTITY = new Matrix();

    public static byte[] readStream(InputStream stream) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = stream.read(buffer)) >= 0) {
            if (read > 0) {
                bos.write(buffer, 0, read);
            }
        }
        return bos.toByteArray();
    }

    public static Bitmap decodeInputStream(InputStream inputStream,
            int maxWidth, int maxHeight) {
        if (inputStream == null) {
            Log.e(TAG, "decodeInputStream: inputStream is null");
            return null;
        }

        final byte[] data;
        try {
            data = readStream(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "decodeInputStream: read stream", e);
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options = generalOptions(options, maxWidth, maxHeight);
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap decodeInputStream(InputStream inputStream,
            int maxPixels) {
        if (inputStream == null) {
            Log.e(TAG, "decodeInputStream: inputStream is null");
            return null;
        }

        final byte[] data;
        try {
            data = readStream(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "decodeInputStream: read stream", e);
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options = generalOptions(options, maxPixels);
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap processExifTransform(String path, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        if (TextUtils.isEmpty(path)) {
            return bitmap;
        }

        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // log.printd("orientation: %d", orientation);
            Matrix matrix = null;
            int w = bitmap.getWidth(), h = bitmap.getHeight();
            int hw = w >> 1, hh = h >> 1;
            int newW = w, newH = h;
            switch (orientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix = new Matrix();
                matrix.setTranslate(w, 0);
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix = new Matrix();
                matrix.setRotate(180, hw, hh);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix = new Matrix();
                matrix.setTranslate(0, h);
                matrix.setScale(1, -1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix = new Matrix();
                matrix.setTranslate(w, h);
                matrix.setScale(-1, -1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix = new Matrix();
                // swap
                matrix.postRotate(90, hw, hh);
                matrix.postTranslate(hh - hw, hw - hh);
                newW = h;
                newH = w;
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                // WTF?
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix = new Matrix();
                // swap
                matrix.postRotate(270, hw, hh);
                matrix.postTranslate(hh - hw, hw - hh);
                newW = h;
                newH = w;
                break;
            }
            if (matrix != null && !matrix.isIdentity()) {
                final Bitmap processed = Bitmap.createBitmap(newW, newH,
                        bitmap.getConfig());
                final Canvas canvas = new Canvas(processed);
                final Paint paint = new Paint();
                canvas.drawBitmap(bitmap, matrix, paint);
                bitmap.recycle();
                bitmap = processed;
            }
            return bitmap;
        } catch (IOException e) {
            // //log.e("read exif", e);
            return bitmap;
        }
    }
    
    /**
     * 
    * @Description: 1.改用BitmapFactory.decodeFileDescriptor;
    * 				2.retry两次	
    * 				3.内部处理createScaledBitmap异常
    * @param @param path 原图路径
    * @param @param minSidePixel 最小像素值
    * @param @param isHighDpi 是否高清
    * @param @return decode失败返回null
    * @author gaozhen.zhang@renren-inc.com
     * @throws java.io.FileNotFoundException
    * @date 2013-8-19 下午12:10:10
     */
    public static Bitmap DecodeFileDescriptor(String path,int minSidePixel,boolean isHighDpi){
    	return DecodeFileDescriptor(path, minSidePixel, isHighDpi, false);
    }

    /**
     *
    * @Description: 1.改用BitmapFactory.decodeFileDescriptor;
    * 				2.retry两次
    * 				3.内部处理createScaledBitmap异常
    * @param @param path 原图路径
    * @param @param minSidePixel 最小像素值
    * @param @param isHighDpi 是否高清
    * @param @param isProcessExif 是否处理Exif信息
    * @param @return decode失败返回null
    * @author gaozhen.zhang@renren-inc.com
     * @throws java.io.FileNotFoundException
    * @date 2013-8-19 下午12:10:10
     */
    public static Bitmap DecodeFileDescriptor(String path,int minSidePixel,
    		boolean isHighDpi, boolean isProcessExif){
    	if (TextUtils.isEmpty(path)) {
            return null;
        }
    	boolean isNormalPicture = true;
    	final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        final int oldHeight = options.outHeight;
        final int oldWidth = options.outWidth;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        int sampleSize = 1;
        float radio = (float) oldWidth / (float) oldHeight;
        int maxSidePixels = 0;
    	if (isHighDpi) {
            if (radio >= MIN_RAIDO && radio <= MAX_RAIDO) {
                if (fitApiLevel(15)) {
                    maxSidePixels = HQ_MAX_WIDTH_ABOVE_FOUR;
                } else {
                    maxSidePixels = HQ_MAX_WIDTH_BELOW_FOUR;
                }
            } else {
                isNormalPicture = false;
                //超宽图
                maxSidePixels = OVER_LENGTH_MAX_WIDTH;
            }
        } else {
            if (radio >= MIN_RAIDO && radio <= MAX_RAIDO) {
                if (fitApiLevel(15)) {
                    maxSidePixels = NORMAL_MAX_WIDTH_ABOVE_FOUR;
                } else {
                    maxSidePixels = NORMAL_MAX_WIDTH_BELOW_FOUR;
                }
            } else {
                maxSidePixels = OVER_LENGTH_MAX_WIDTH_DEFINITION;
            }
        }
        
    	Log.v(TAG, "ImageUtil::DecodeFileDescriptor   oldWidth is " + oldWidth + ", oldHeight is " + oldHeight);
        Bitmap result = null;
        sampleSize = calculateInSampleSize(options, maxSidePixels, maxSidePixels);
//        Log.d("changxin", "sampleSize:" + sampleSize);
    	options.inSampleSize = sampleSize;
    	FileInputStream fis = null;
    	int retryCount = 0;
    	while (retryCount < 2) {
    		retryCount++;
    		Log.v(TAG, "ImageUtil::DecodeFileDescriptor   retryCount is " + retryCount);
    		try {
    			fis = new FileInputStream(path);
    			result = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
    		} catch (Exception e) {
    			Log.e(TAG, e.getMessage());
    		} catch (OutOfMemoryError e) {
    			sampleSize= sampleSize*2;
	        	options.inSampleSize = sampleSize;
    			Log.e(TAG, e.getMessage());
    			if (retryCount ==1) {
    				Log.e(TAG, "内存空间不足");
    			}
    			continue;
    		}finally{
    			if (fis != null) {
                    try {
                    	fis.close();
                    } catch (IOException e) {}
                }
    		}
    		break;
    	}
    	boolean isGifFile = false;
    	if(!TextUtils.isEmpty(path) && path.toLowerCase().endsWith(".gif")) {
    		isGifFile = true;
    	}
		if (result!=null && isNormalPicture) {
			try {
	            final int width = result.getWidth();
	            final int height = result.getHeight();
//	            Log.d("changxin", "oldWidth:" + width+",oldHeight:"+height);
	            int newWidth = 0;
	            int newHeight = 0;
	            
	            boolean isTransferXY = false;
	            if(isProcessExif) {
	            	 try {
	                     ExifInterface exif = new ExifInterface(path);
	                     int orientation = exif.getAttributeInt(
	                             ExifInterface.TAG_ORIENTATION,
	                             ExifInterface.ORIENTATION_UNDEFINED);
	                     switch (orientation) {
	                     //这两种会交换宽、高
	                     case ExifInterface.ORIENTATION_ROTATE_90:
	                     case ExifInterface.ORIENTATION_ROTATE_270:
	                    	 isTransferXY = true;
	                         break;
	                     }
	            	 } catch (IOException e) {
	                     e.printStackTrace();
	                     return result;
	                 }
	            }
	            
	            if(isTransferXY) {
	            	if (height > maxSidePixels) {
	            		newHeight = maxSidePixels;
						newWidth = width * maxSidePixels / height;
						if(!isGifFile) {
							result = nativeCompress(result, newWidth, newHeight);
						}
	            	}
	            }
	            else {
	            	if(width > maxSidePixels) {
	            		newWidth = maxSidePixels;
						newHeight = height * maxSidePixels / width;
						if(!isGifFile) {
							result = nativeCompress(result, newWidth, newHeight);
						}
	            	}
	            }
//	            Log.d("changxin", "newWidth:" + newWidth+",newHeight:"+newHeight);
    		} catch (Exception e) {
    			Log.e("TAG","ImageUtil::nativeCompress failed,  " + e.getMessage());
    		} catch (OutOfMemoryError e) {
    			Log.e("TAG","ImageUtil::nativeCompress OutOfMemoryError,  " + e.getMessage());
    		}
		}
		else if(result!=null && !isNormalPicture) {
			if (radio < MIN_RAIDO) {//超长图
                maxSidePixels = LONG_IMAGE_MAX_HEIGHT;
    			try {
    	            final int width = result.getWidth();
    	            final int height = result.getHeight();
    	            Log.d("changxin", "oldWidth:" + width+",oldHeight:"+height);
    	            int newWidth = 0;
    	            int newHeight = 0;
	            	if (height > maxSidePixels) {
	            		newHeight = maxSidePixels;
						newWidth = width * maxSidePixels / height;
						if(!isGifFile) {
							result = nativeCompress(result, newWidth, newHeight);
						}
	            	}
        		} catch (Exception e) {
        			Log.e("TAG","ImageUtil::nativeCompress failed,  " + e.getMessage());
        		} catch (OutOfMemoryError e) {
        			Log.e("TAG","ImageUtil::nativeCompress OutOfMemoryError,  " + e.getMessage());
        		}
			} else if(radio > MAX_RAIDO) {//超宽图,等比压缩到宽度1600
    			try {
    	            final int width = result.getWidth();
    	            final int height = result.getHeight();
    	            Log.d("changxin", "oldWidth:" + width+",oldHeight:"+height);
    	            int newWidth = 0;
    	            int newHeight = 0;
	            	if(width > maxSidePixels) {
	            		newWidth = maxSidePixels;
						newHeight = height * maxSidePixels / width;
						if(!isGifFile) {
							result = nativeCompress(result, newWidth, newHeight);
						}
	            	}
        		} catch (Exception e) {
        			Log.e("TAG","ImageUtil::nativeCompress failed,  " + e.getMessage());
        		} catch (OutOfMemoryError e) {
        			Log.e("TAG","ImageUtil::nativeCompress OutOfMemoryError,  " + e.getMessage());
        		}
			}
			
		}
    	return result;
    }
//    
	private static Bitmap nativeCompress(Bitmap sourceBitmap, int newWidth,
			int newHeight) {
		Log.v(TAG, "ImageUtil::nativeCompress   start");
		int[] resultInt= new int[newHeight*newWidth];
		//获取固定位置的像素值然后塑造一个新的图像
		sourceBitmap.getPixels(resultInt, 0, newWidth, 0, 0, newWidth, newHeight);
//		int[] resultInt = ImgCmpLoader.nativeBitmapCompress(sourceBitmap,
//				newWidth, newHeight);
		recycleBitmap(sourceBitmap);
		if(resultInt == null) {
			Log.e(TAG, "ImageUtil::nativeBitmapCompress   failed");
		}
		Bitmap bitmap = Bitmap.createBitmap(newWidth, newHeight,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(resultInt, 0, newWidth, 0, 0, newWidth, newHeight);
		Log.v(TAG, "ImageUtil::nativeCompress   end    bitmap is " + bitmap);
		
		return bitmap;
	}

    public static Bitmap decodeFile(String path, int maxWidth, int maxHeight) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        try {
        	BitmapFactory.decodeFile(path, options);
			FileInputStream fis = new FileInputStream(path);
			bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(),null,
	                generalOptions(options, maxWidth, maxHeight));
		} catch (Exception e) {
			Log.e(TAG, "decodeFile - " +e);
		}
        return bitmap;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
//        recycleBitmap(bitmap);
        return newbmp;
    }

    public static Bitmap getThumbnail(String path, int minSideLength,int maxNumOfPixels) {
    	 if (TextUtils.isEmpty(path)) {
             return null;
         }
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inJustDecodeBounds = true;
         Bitmap bitmap = null;
         try {
         	BitmapFactory.decodeFile(path, options);
 			options.inSampleSize = computeSampleSize(options, minSideLength, maxNumOfPixels);
 	        options.inJustDecodeBounds = false;
 	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
 	        options.inDither = false;
 	        options.inPurgeable = true;
 	        options.inInputShareable = true;
 	        FileInputStream fis = new FileInputStream(path);
 			bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(),null,options );
 		} catch (Exception e) {
 			Log.e(TAG, "decodeFile - " +e);
 		}
        return bitmap;
    }
    
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (Runtime.getRuntime().maxMemory() <= 32 * 1024 * 1024) {
				int heightRatio = (int) Math.ceil((float) height
						/ (float) reqHeight);
				int widthRatio = (int) Math.ceil((float) width
						/ (float) reqWidth);
				inSampleSize = Math.max(heightRatio, widthRatio);
			} else {
				int heightRatio = Math.round((float) height / (float) reqHeight);
				int widthRatio = Math.round((float) width / (float) reqWidth);
				inSampleSize = Math.min(heightRatio, widthRatio);
			}
		}
		return inSampleSize;
	}
    
    
    public static void recycleBitmap(Bitmap bmp){
        if(bmp == null || bmp.isRecycled())
            return;
        
        try{
            bmp.recycle();
        }catch(Exception e){
            
        }
    }

    public static Bitmap decodeFileExact(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 1;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap decodeResource(Resources resources, int resId,
            int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        return BitmapFactory.decodeResource(resources, resId,
                generalOptions(options, maxWidth, maxHeight));
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length,
            int maxWidth, int maxHeight) {
        if (data == null) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        return BitmapFactory.decodeByteArray(data, offset, length,
                generalOptions(options, maxWidth, maxHeight));
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length,
            int maxPixels) {
        if (data == null) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length);
        return BitmapFactory.decodeByteArray(data, offset, length,
                generalOptions(options, maxPixels));
    }

//    public static Bitmap decodeInputStream(InputStream inputStream) {
//        if (inputStream == null) {
//            return null;
//        }
//
//        final DisplayMetrics dm = AppInfo.getAppContext()
//                .getResources().getDisplayMetrics();
//        return decodeInputStream(inputStream, dm.widthPixels, dm.heightPixels);
//    }

//    public static Bitmap decodeFile(String path) {
//        if (TextUtils.isEmpty(path)) {
//            return null;
//        }
//
//        final DisplayMetrics dm = AppInfo.getAppContext()
//                .getResources().getDisplayMetrics();
//        return decodeFile(path, dm.widthPixels, dm.heightPixels);
//    }

    public static Bitmap decodeByteArrayExact(byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            Log.w(TAG, "decode byte array", e);
            return null;
        }
    }

//    public static Bitmap decodeByteArray(byte[] data) {
//        if (data == null) {
//            return null;
//        }
//
//        final DisplayMetrics dm = AppInfo.getAppContext()
//                .getResources().getDisplayMetrics();
//        return decodeByteArray(data, 0, data.length, dm.widthPixels,
//                dm.heightPixels);
//    }

    public static BitmapFactory.Options generalOptions(BitmapFactory.Options options) {
    	if (options == null) {
            options = new BitmapFactory.Options();
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 1;
        return options;
    }

    public static BitmapFactory.Options generalOptions(
            BitmapFactory.Options options, int maxWidth, int maxHeight) {
        if (options == null) {
            options = new BitmapFactory.Options();
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 1;

        if (options.outHeight > maxHeight) {
            options.inSampleSize = (int) Math.ceil((float) options.outHeight
                    / (float) maxHeight);
        }
        if (options.outWidth > maxWidth) {
            options.inSampleSize = Math.max((int) Math
                    .ceil((float) options.outWidth / (float) maxWidth),
                    options.inSampleSize);
        }
        return options;
    }
    /**
     * 
     * @param options
     * @param minSideLength 设置最小图片宽高，压缩图片尺寸会尽量向该值靠拢，不考虑则设置为-1
     * @param maxNumOfPixels 最大像素值,如 :512*512，刚性条件
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, 
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
     
    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : 
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : 
                (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));
 
        if (upperBound < lowerBound) {
            return lowerBound;
        }
 
        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
    
    public static BitmapFactory.Options generalOptions(
            BitmapFactory.Options options, int maxPixels) {
        if (options == null) {
            options = new BitmapFactory.Options();
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 1;

        final int currentPixels = options.outWidth * options.outHeight;
        if (currentPixels > maxPixels) {
            options.inSampleSize = (int) Math.ceil((float) currentPixels
                    / (float) maxPixels);
        }
        return options;
    }

    public static Bitmap CreateOvalBitmap(Bitmap input) {
        if (input == null) {
            return null;
        }

        final int w = input.getWidth();
        final int h = input.getHeight();

        final Bitmap result = Bitmap
                .createBitmap(w, h, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.TRANSPARENT);

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(new BitmapShader(input, Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT));
        canvas.drawOval(new RectF(0, 0, w, h), paint);
        return result;
    }

    /**
     * @param filePath
     *            存储路径
     * @param maxNumOfPixels
     * @return 长宽最大的位图
     *         <p/>
     *         description 目前本地选取图片、转发功能在使用
     */
    public static Bitmap image_Compression(String filePath, float maxNumOfPixels) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        /* update by yuchao.zhang 更新上传图片的分辨率压缩策略，最大长宽为720 */
        // opts.inSampleSize = ImageUtil.computeSampleSize(opts, -1,
        // maxNumOfPixels);
        int widthRatio = (int) Math.ceil(opts.outWidth / MAX_QUALITY);
        int heightRatio = (int) Math.ceil(opts.outHeight / MAX_QUALITY);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        opts.inJustDecodeBounds = false;

        // opts.inPreferredConfig = Bitmap.Config.ARGB_4444;

        Bitmap bmp = BitmapFactory.decodeFile(filePath, opts);

        bmp = scaleImg(bmp, MAX_QUALITY);

        return bmp;
    }
    
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        return bitmap2Bytes(bitmap, 100);
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap, int quality) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            bytes = outputStream.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{
                if(outputStream != null){
                    outputStream.flush();
                    outputStream.close();
                }
            }catch(Exception e){
                
            }
        }
        return bytes;
    }

    //bitmap转byte[]
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        try {
            int i;
            int j;
            if (bmp.getHeight() > bmp.getWidth()) {
                i = bmp.getWidth();
                j = bmp.getWidth();
            } else {
                i = bmp.getHeight();
                j = bmp.getHeight();
            }

            Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
            Canvas localCanvas = new Canvas(localBitmap);

            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle) {
                bmp.recycle();
            }
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();

            localByteArrayOutputStream.close();
            return arrayOfByte;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param filePath
     *            存储路径
     * @param dm
     *            DisplayMetrics
     * @return 长宽最大为720的位图
     *         <p/>
     *         description 目前拍照功能在使用
     * @author yuchao.zhang(更新为新方法)
     */
    public static Bitmap image_Compression(String filePath, DisplayMetrics dm) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        /* update by yuchao.zhang 更新上传图片的分辨率压缩策略，最大长宽为720 */
        // int intScreenX=dm.widthPixels;
        // int intScreenY=dm.heightPixels;
        // int intWidth=intScreenX-40;
        // int intHeight=intScreenY-80;
        //
        // int bmHeight=opts.outHeight;
        // int bmWidth=opts.outWidth;
        //
        // int scaleW=bmWidth/intWidth;
        // int scaleH=bmHeight/intHeight;
        // int scale=scaleW>scaleH?scaleW:scaleH;
        // if(scale<=0)
        // scale=1;
        // opts.inSampleSize = scale;
        int widthRatio = (int) Math.ceil(opts.outWidth / MAX_QUALITY);
        int heightRatio = (int) Math.ceil(opts.outHeight / MAX_QUALITY);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }

        opts.inJustDecodeBounds = false;

        // opts.inPreferredConfig = Bitmap.Config.ARGB_4444;

        Bitmap bmp = BitmapFactory.decodeFile(filePath, opts);

        bmp = scaleImg(bmp, MAX_QUALITY);

        return bmp;
    }

    private static Bitmap scaleImg(Bitmap img, float quality) {

        if (img == null) {
            return null;
        }
        float sHeight = 0;
        float sWidth = 0;

        Bitmap result = img;

        if (img.getWidth() >= img.getHeight() && img.getWidth() > quality) {

            sWidth = quality;
            sHeight = (quality * img.getHeight()) / img.getWidth();
            result = Bitmap.createScaledBitmap(img, (int) sWidth,
                    (int) sHeight, true);

            if (null != img && !img.isRecycled() && !img.equals(result)) {
                img.recycle();
            }
        } else if (img.getHeight() > img.getWidth()
                && img.getHeight() > quality) {

            sHeight = quality;
            sWidth = (quality * img.getWidth()) / img.getHeight();
            result = Bitmap.createScaledBitmap(img, (int) sWidth,
                    (int) sHeight, true);

            if (null != img && !img.isRecycled() && !img.equals(result)) {
                img.recycle();
            }
        }

        return result;
    }
    
    /**
	 * 从指定路径加载制定宽高的图片
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap loadScaledBitmap(String path, int width, int height) {
		if (width <= 0 || height <= 0) {
			return null;
		}
		if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
			return null;
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bmp = BitmapFactory.decodeFile(path, opts);
		if (bmp != null) {
			Bitmap destBmp = Bitmap.createScaledBitmap(bmp, width, height, false);
			if (!(bmp.getWidth() == destBmp.getWidth() && bmp.getHeight() == destBmp.getHeight())) {
				bmp.recycle();
			}
			return destBmp;
		}

		return null;
	}

    public static boolean saveBitmapAsPng(Bitmap bmp, String destFileName,  boolean recycleBitmapAfterSaved){
        try {
            if (bmp == null || bmp.isRecycled() || TextUtils.isEmpty(destFileName)) {
                return false;
            }
            File file = new File(destFileName);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();

            if (file.exists())
                file.delete();
            file.createNewFile();

            FileOutputStream outStream = new FileOutputStream(file);
            int quality = 80;           
            
            bmp.compress(Bitmap.CompressFormat.PNG, quality, outStream);
            outStream.flush();
            outStream.close();

            if(recycleBitmapAfterSaved){
                bmp.recycle();
            }
            
            // 操作成功, 直接返回
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public static boolean saveBitmap(Bitmap bmp, String desFilename, boolean recycleBitmapAfterSaved){
        return saveBitmapAsJPG(bmp, desFilename, 80, recycleBitmapAfterSaved);
    }
    
    public static boolean saveBitmapAsJPG(Bitmap bmp, String desFilename, int quality, boolean recycleBitmapAfterSaved){
        try {
            if (bmp == null || bmp.isRecycled() || TextUtils.isEmpty(desFilename)) {
                return false;
            }
            File file = new File(desFilename);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();

            if (file.exists())
                file.delete();
            file.createNewFile();

            FileOutputStream outStream = new FileOutputStream(file);
            
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, outStream);
            outStream.flush();
            outStream.close();

            if(recycleBitmapAfterSaved){
                bmp.recycle();
            }
            
            // 操作成功, 直接返回
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return false;
    }
    
	public static Pair<Integer, Integer> getLocalImageSize(String path) {
		if (path == null)
			return Pair.create(0, 0);
		if (!new File(path).exists())
			return Pair.create(0, 0);
		int w = 0;
		int h = 0;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		w = options.outWidth;
		h = options.outHeight;
		return Pair.create(w, h);
	}
    
    public static void scanMedia(Context ctx, String path) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.parse("file://" + path)); // path-新保存的文件地址
        ctx.sendBroadcast(scanIntent);
    }

//    public static int getIndexInListById(int id, ArrayList<PhotoInfoModel> mPhotoInfoList) {
//        return getIndexInListById(String.valueOf(id), mPhotoInfoList);
//    }
//
//    public static int getIndexInListById(String id, ArrayList<PhotoInfoModel> mPhotoInfoList) {
//        int index = -1;
//        if(mPhotoInfoList == null) {
//            mPhotoInfoList = new ArrayList<PhotoInfoModel>();
//        }
//        for(int i = 0; i < mPhotoInfoList.size(); i++) {
//            PhotoInfoModel photoInfo = mPhotoInfoList.get(i);
//            if(photoInfo != null && photoInfo.mPhotoId != null && photoInfo.mPhotoId.equals(id)) {
//                index = i;
//                break;
//            }
//        }
//        return index;
//    }

    public static BitmapFactory.Options getTransformOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        if (TextUtils.isEmpty(path)) {
            return options;
        }

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int width = options.outWidth;
        int height = options.outHeight;

        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                case ExifInterface.ORIENTATION_ROTATE_270:
                    width = options.outHeight;
                    height = options.outWidth;
                    break;
            }

        } catch (IOException e) {
            Log.e("getTransformOptions", e.toString());
        } finally {
            options.outWidth = width;
            options.outHeight = height;
        }

        return options;
    }
    public static boolean fitApiLevel(int var0) {
        try {
            int var1 = Integer.parseInt(VERSION.SDK);
            if(var1 >= var0) {
                return true;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return false;
    }

}
