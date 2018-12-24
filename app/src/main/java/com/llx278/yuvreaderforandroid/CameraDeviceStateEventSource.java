package com.llx278.yuvreaderforandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class CameraDeviceStateEventSource extends CameraDevice.StateCallback implements ObservableOnSubscribe<CameraDeviceStateEventSource.CameraDeviceEvent> {

    private ObservableEmitter<CameraDeviceEvent> emitter;
    private String cameraId;
    private Handler handler;
    private Context context;

    public CameraDeviceStateEventSource(String cameraId, Handler handler, Context context) {
        this.handler = handler;
        this.cameraId = cameraId;
        this.context = context;
    }

    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        this.emitter.onNext(new CameraDeviceEvent(Event.OPENED, camera));
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {
        this.emitter.onNext(new CameraDeviceEvent(Event.DISCONNECTED, camera));

    }

    @Override
    public void onError(@NonNull CameraDevice camera, int error) {
        this.emitter.onError(new CameraErrorException(camera, error));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void subscribe(ObservableEmitter<CameraDeviceEvent> emitter) throws Exception {
        this.emitter = emitter;

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (manager == null) {
            emitter.onError(new NullPointerException("null manager"));
            return;
        }
        manager.openCamera(this.cameraId, this, this.handler);
    }

    public static final class CameraDeviceEvent {
        public final Event event;
        public final CameraDevice device;

        public CameraDeviceEvent(Event event, CameraDevice device) {
            this.event = event;
            this.device = device;
        }
    }

    public enum Event {
        OPENED, DISCONNECTED
    }
}
