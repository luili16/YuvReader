package com.llx278.yuvreaderforandroid.util;

import android.util.Log;

public class YuvToRGB {
    private static int R = 0;
    private static int G = 1;
    private static int B = 2;

    static {
        System.loadLibrary("yuv-util");
    }

    public static byte[] yuv420pToARGB(byte[] src, int width, int height) {

        // 小端存储
        byte[] bgra = new byte[width * height * 4];
        int pixels = width * height;
        int uBegin = pixels;
        int vBegin = pixels / 4 + pixels;
        int step = 2;
        for (int i = 0; i < height; i += step) {
            for (int j = 0; j < width; j += step) {
                int yIndex = j + i * width;
                int uIndex = (yIndex % width) / 2 + ((i / 2) * width) / 2;
                int vIndex = uIndex;
                byte y0 = src[yIndex];
                byte y1 = src[yIndex + 1];
                byte y2 = src[yIndex + width];
                byte y3 = src[yIndex + width + 1];
                byte u = src[uBegin + uIndex];
                byte v = src[vBegin + vIndex];
                int bgraBegin = yIndex * 4;
                RGB tmp = yuvTorgb(y0, u, v);
                bgra[bgraBegin] = (byte) tmp.r;   // r
                bgra[bgraBegin + 1] = (byte) tmp.g; // g
                bgra[bgraBegin + 2] = (byte) tmp.b;// b
                bgra[bgraBegin + 3] = (byte) 0xff; // a
                tmp = yuvTorgb(y1, u, v);
                bgra[bgraBegin + 4] = (byte) tmp.r;   // r
                bgra[bgraBegin + 5] = (byte) tmp.g; // g
                bgra[bgraBegin + 6] = (byte) tmp.b;// b
                bgra[bgraBegin + 7] = (byte) 0xff; // a
                tmp = yuvTorgb(y2, u, v);
                bgra[bgraBegin + width + width / 3] = (byte) tmp.r;   // r
                bgra[bgraBegin + width + width / 3 + 1] = (byte) tmp.g; // g
                bgra[bgraBegin + width + width / 3 + 2] = (byte) tmp.b; // b
                bgra[bgraBegin + width + width / 3 + 3] = (byte) 0xff; // a
                tmp = yuvTorgb(y3, u, v);
                bgra[bgraBegin + width + width / 3 + 4] = (byte) tmp.r;   // r
                bgra[bgraBegin + width + width / 3 + 5] = (byte) tmp.g; // g
                bgra[bgraBegin + width + width / 3 + 6] = (byte) tmp.b; // b
                bgra[bgraBegin + width + width / 3 + 7] = (byte) 0xff;
            }
        }

        return bgra;
    }


    private static class RGB {
        public int r, g, b;
    }

    private static RGB yuvTorgb(byte Y, byte U, byte V) {
        RGB rgb = new RGB();
        rgb.r = (int) ((Y & 0xff) + 1.4075 * ((V & 0xff) - 128));
        rgb.g = (int) ((Y & 0xff) - 0.3455 * ((U & 0xff) - 128) - 0.7169 * ((V & 0xff) - 128));
        rgb.b = (int) ((Y & 0xff) + 1.779 * ((U & 0xff) - 128));
        rgb.r = (rgb.r < 0 ? 0 : rgb.r > 255 ? 255 : rgb.r);
        rgb.g = (rgb.g < 0 ? 0 : rgb.g > 255 ? 255 : rgb.g);
        rgb.b = (rgb.b < 0 ? 0 : rgb.b > 255 ? 255 : rgb.b);
        return rgb;
    }

    public static native int I420ToARGB(byte[] srcY, int srcStrideY, byte[] srcU, int srcStrideU, byte[] srcV, int srcStrideV, byte[] dstArgb, int dstStrideArgb, int width, int height);

    public static native int I420ToARGBWithPackage(byte[] srcYuv, int pointY, int srcStrideY, int pointU, int srcStrideU, int pointV, int srcStrideV, byte[] dstArgb, int dstStrideArgb, int width, int height);

    public static native int ARGBToI420(byte[] srcArgb, int srcStrideArgb, byte[] dstY, int dstStrideY, byte[] dstU, int dstStrideU, byte[] dstV, int dstStrideV, int width, int height);
}
