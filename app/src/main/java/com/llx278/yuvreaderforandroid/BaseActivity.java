package com.llx278.yuvreaderforandroid;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.llx278.yuvreaderforandroid.util.MyGLUtils;

public abstract class BaseActivity extends AppCompatActivity {

    private GLSurfaceView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.view = findViewById(R.id.activity_main_gl_surface_view);

        if (MyGLUtils.detectOpenGLES30(this)) {
            this.view.setEGLContextClientVersion(3);
            onGLReady(this.view);
        } else {
            throw new RuntimeException("not support GLES3.0");
        }
    }

    protected abstract void onGLReady(GLSurfaceView view);

    @Override
    protected void onResume() {
        super.onResume();
        this.view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.view.onPause();
    }
}
