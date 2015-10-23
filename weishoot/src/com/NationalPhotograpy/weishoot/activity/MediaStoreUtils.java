package com.NationalPhotograpy.weishoot.activity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.util.Log;

public class MediaStoreUtils {
	private static final String DATA = MediaStore.Images.Media.DATA;
	private static final String DATE_TAKEN = MediaStore.Images.Media.DATE_TAKEN;
	private static final String SIZE = MediaStore.Images.Media.SIZE;
	private static final String BUCKET_DISPLAY_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
	private static final String FILE_NAME = MediaStore.Images.Media.TITLE;
	private static final String TAG = "MediaStoreUtils";


	public static ArrayList<String> loadAllImage(Context context) {
	   ArrayList<String> list = null;
		String[] projection = new String[] { DATE_TAKEN, DATA, SIZE, BUCKET_DISPLAY_NAME, FILE_NAME };
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, ImageColumns.DATE_TAKEN + " DESC");
		if (cursor != null) {
			list = new ArrayList<String>();
			if (cursor.moveToFirst()) {
				do {
					String url = cursor.getString(1);
					int size = Integer.parseInt(cursor.getString(2));
//					Log.e(TAG, "url  :: " + picBean.url);
					File file = new File(url);
					if(file != null && !file.exists() && file.length()<1){
						continue;
					}
					if (size>0) {
					    list.add(url);
                    }
				} while (cursor.moveToNext());
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
}
