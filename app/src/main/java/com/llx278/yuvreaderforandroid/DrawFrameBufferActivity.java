package com.llx278.yuvreaderforandroid;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;

import com.llx278.yuvreaderforandroid.util.BitMapUtil;

import java.nio.ByteBuffer;

public class DrawFrameBufferActivity extends BaseActivity {

    @Override
    protected void onGLReady(GLSurfaceView view) {

        Bitmap bitmap = BitMapUtil.getRawResource(this, R.raw.aa);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        ByteBuffer b = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(b);
        b.position(0);

        ITexture texture = new Texture(b, width, height);
        FrameBufferTexture frameBufferTexture = new FrameBufferTexture(this, texture.getWidth(), texture.getHeight());
        FrameBufferSceneRenderDemo demo = new FrameBufferSceneRenderDemo(this, frameBufferTexture, b, width, height);
        //BufferSceneRender demo = new BufferSceneRender(texture, rectangle, rectangle1);
        view.setRenderer(demo);
        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
