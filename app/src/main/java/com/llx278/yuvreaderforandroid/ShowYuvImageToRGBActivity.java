package com.llx278.yuvreaderforandroid;

import android.opengl.GLSurfaceView;

import com.llx278.yuvreaderforandroid.util.BitMapUtil;
import com.llx278.yuvreaderforandroid.util.YuvToRGB;

import java.nio.ByteBuffer;

public class ShowYuvImageToRGBActivity extends BaseActivity {


    @Override
    protected void onGLReady(GLSurfaceView view) {
        int width = 256;
        int height = 256;
        byte[] rawYuv = BitMapUtil.getYuvRawBuffer(this, R.raw.lena_256x256_yuv420p);
        int pixels = width * height;
        byte[] dstY = new byte[pixels];
        byte[] dstU = new byte[pixels / 4];
        byte[] dstV = new byte[pixels / 4];
        int pointY = 0;
        int pointU = pixels + pixels / 4;
        int pointV = pixels;
        System.arraycopy(rawYuv, pointY, dstY, 0, pixels);
        System.arraycopy(rawYuv, pointV, dstV, 0, pixels / 4);
        System.arraycopy(rawYuv, pointU, dstU, 0, pixels / 4);
        int srcStrideY = width;
        int srcStrideU = width / 2;
        int srcStrideV = width / 2;
        byte[] dstArgb = new byte[width * height * 4];
        int dstStrideArgb = width * 4;
        YuvToRGB.I420ToARGB(dstY, srcStrideY, dstU, srcStrideU, dstV, srcStrideV, dstArgb, dstStrideArgb, width, height);
        ByteBuffer dst = ByteBuffer.allocate(dstArgb.length);
        dst.put(dstArgb);
        dst.position(0);
        Texture texture = new Texture(dst, width, height);
        SimpleSceneRender simpleSceneRender = new SimpleSceneRender(this, dst,width,height);
        view.setRenderer(simpleSceneRender);
        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
