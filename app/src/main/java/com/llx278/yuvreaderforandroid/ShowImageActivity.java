package com.llx278.yuvreaderforandroid;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;

import com.llx278.yuvreaderforandroid.util.BitMapUtil;

import java.nio.ByteBuffer;

public class ShowImageActivity extends BaseActivity {

    @Override
    protected void onGLReady(GLSurfaceView view) {

        Bitmap bitmap = BitMapUtil.getRawResource(this, R.raw.aa);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        ByteBuffer b = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(b);
        b.position(0);
        SimpleSceneRender simpleSceneRender = new SimpleSceneRender(getApplicationContext(), b, width, height);
        view.setRenderer(simpleSceneRender);
        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
