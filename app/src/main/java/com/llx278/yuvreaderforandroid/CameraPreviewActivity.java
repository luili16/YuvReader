package com.llx278.yuvreaderforandroid;

import android.Manifest;
import android.content.Context;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.llx278.yuvreaderforandroid.util.MyGLUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;

public class CameraPreviewActivity extends AppCompatActivity {

    GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = findViewById(R.id.activity_camera_preview_gl_surface);

        if (!MyGLUtils.detectOpenGLES30(this)) {
            throw new RuntimeException("not support GLES3.0");
        }

        glSurfaceView.setEGLContextClientVersion(3);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        /*RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA).
                flatMap(grand -> {
                    if (grand) {
                        CameraManager cm = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        String cameraId = cm.getCameraIdList()[0];
                        return RXCamera2.openCamera(getApplicationContext(), cameraId, null);
                    } else {
                        return Observable.create(emitter -> {
                            emitter.onError(new IllegalStateException(""));
                        });
                    }
                }).flatMap(
        })*/



    }
}
