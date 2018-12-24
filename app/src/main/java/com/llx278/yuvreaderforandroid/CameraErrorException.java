package com.llx278.yuvreaderforandroid;

import android.hardware.camera2.CameraDevice;

public class CameraErrorException extends Exception {

    public final int error;
    public final CameraDevice device;

    public CameraErrorException( CameraDevice device,int error) {
        this.error = error;
        this.device = device;
    }
}
