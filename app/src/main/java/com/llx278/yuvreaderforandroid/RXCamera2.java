package com.llx278.yuvreaderforandroid;

import android.content.Context;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.view.Surface;


import java.util.List;

import io.reactivex.Observable;

import static com.llx278.yuvreaderforandroid.CameraDeviceStateEventSource.*;

public class RXCamera2 {

    public static Observable<CameraDeviceEvent> openCamera(Context context, String cameraId, Handler handler) {
        return Observable.create(new CameraDeviceStateEventSource(cameraId,handler,context));
    }

    public static Observable<CameraCaptureSession> createCaptureSession(CameraDevice camera, List<Surface> surfaces,Handler handler) {
        return Observable.create(new CaptureSessionStateEventSource(camera,surfaces,handler));
    }



}
