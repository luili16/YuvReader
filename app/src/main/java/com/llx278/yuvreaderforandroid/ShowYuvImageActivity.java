package com.llx278.yuvreaderforandroid;

import android.opengl.GLSurfaceView;

public class ShowYuvImageActivity extends BaseActivity {

    @Override
    protected void onGLReady(GLSurfaceView view) {
        YUVSceneRender yuvSceneRender = new YUVSceneRender(this);
        view.setRenderer(yuvSceneRender);
        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
