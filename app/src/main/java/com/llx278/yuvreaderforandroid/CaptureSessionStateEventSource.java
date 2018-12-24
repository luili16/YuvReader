package com.llx278.yuvreaderforandroid;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Surface;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class CaptureSessionStateEventSource extends CameraCaptureSession.StateCallback
        implements ObservableOnSubscribe<CameraCaptureSession> {

    private ObservableEmitter<CameraCaptureSession> emitter;

    private CameraDevice camera;
    private List<Surface> surfaces;
    private Handler handler;

    public CaptureSessionStateEventSource(CameraDevice camera, List<Surface> surfaces, Handler handler) {
        this.camera = camera;
        this.surfaces = surfaces;
        this.handler = handler;
    }

    @Override
    public void onConfigured(@NonNull CameraCaptureSession session) {
        emitter.onNext(session);
    }

    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        emitter.onError(new ConfigureFailedException(session));
    }

    @Override
    public void subscribe(ObservableEmitter<CameraCaptureSession> emitter) throws Exception {
        this.emitter = emitter;
        camera.createCaptureSession(surfaces, this, handler);
    }

    public static final class ConfigureFailedException extends Exception {
        public final CameraCaptureSession session;

        public ConfigureFailedException(CameraCaptureSession session) {
            this.session = session;
        }
    }
}
