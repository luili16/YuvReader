package com.llx278.yuvreaderforandroid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitMapUtil {
    public static Bitmap getRawResource(Context context, int id) {
        InputStream inputStream = context.getResources().openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static byte[] getYuvRawBuffer(Context context, int id) {
        InputStream inputStream = context.getResources().openRawResource(id);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(temp)) != -1) {
                bos.write(temp, 0, len);
            }
            bos.close();
            inputStream.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
