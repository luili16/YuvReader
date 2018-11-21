//
// Created by 刘立新 on 2018/10/29.
//
#include <jni.h>
#include "libyuv.h"

extern "C" JNIEXPORT jint
JNICALL
Java_com_llx278_yuvreaderforandroid_util_YuvToRGB_I420ToARGB(JNIEnv *env,
                                                             jobject,
                                                             jbyteArray src_y,
                                                             jint src_stride_y,
                                                             jbyteArray src_u,
                                                             jint src_stride_u,
                                                             jbyteArray src_v,
                                                             jint src_stride_v,
                                                             jbyteArray dst_argb,
                                                             jint dst_stride_argb,
                                                             jint width,
                                                             jint height
) {
    jbyte *yData = env->GetByteArrayElements(src_y, NULL);
    jbyte *uData = env->GetByteArrayElements(src_u, NULL);
    jbyte *vData = env->GetByteArrayElements(src_v, NULL);
    jbyte *argbData = env->GetByteArrayElements(dst_argb, NULL);
    int ret = -1;
    if (yData != NULL && uData != NULL && vData != NULL && argbData != NULL) {

        ret = libyuv::I420ToARGB(reinterpret_cast<const uint8 *>(yData), src_stride_y,
                                 reinterpret_cast<const uint8 *>(uData), src_stride_u,
                                 reinterpret_cast<const uint8 *>(vData), src_stride_v,
                                 (uint8 *) argbData, dst_stride_argb, width, height);
        env->ReleaseByteArrayElements(src_y, yData, JNI_ABORT);
        env->ReleaseByteArrayElements(src_u, uData, JNI_ABORT);
        env->ReleaseByteArrayElements(src_v, vData, JNI_ABORT);
    }
    return ret;
}


extern "C" JNIEXPORT jint
JNICALL
Java_com_llx278_yuvreaderforandroid_util_YuvToRGB_ARGBToI420(JNIEnv *env,
                                                             jobject,
                                                             jbyteArray src_argb,
                                                             jint src_stride_argb,
                                                             jbyteArray dst_y,
                                                             jint dst_stride_y,
                                                             jbyteArray dst_u,
                                                             jint dst_stride_u,
                                                             jbyteArray dst_v,
                                                             jint dst_stride_v,
                                                             jint width,
                                                             jint height
) {
    jbyte *argbData = env->GetByteArrayElements(src_argb, NULL);
    jbyte *yData = env->GetByteArrayElements(dst_y, NULL);
    jbyte *uData = env->GetByteArrayElements(dst_u, NULL);
    jbyte *vData = env->GetByteArrayElements(dst_v, NULL);
    int ret = -1;
    if (argbData != NULL && yData != NULL) {
        ret = libyuv::ARGBToI420(reinterpret_cast<const uint8 *>(argbData), src_stride_argb,
                                 reinterpret_cast<uint8 *>(yData), dst_stride_y,
                                 reinterpret_cast<uint8 *>(uData), dst_stride_u,
                                 reinterpret_cast<uint8 *>(vData), dst_stride_v,
                                 width, height);
        env->ReleaseByteArrayElements(src_argb, argbData, JNI_ABORT);
    }
    return ret;
}

extern "C" JNIEXPORT jint
JNICALL
Java_com_llx278_yuvreaderforandroid_util_YuvToRGB_I420ToARGBWithPackage(JNIEnv *env,
                                                                        jobject,
                                                                        jbyteArray src_yuv,
                                                                        jint point_y,
                                                                        jint src_stride_y,
                                                                        jint point_u,
                                                                        jint src_stride_u,
                                                                        jint point_v,
                                                                        jint src_stride_v,
                                                                        jbyteArray dst_argb,
                                                                        jint dst_stride_argb,
                                                                        jint with, jint height) {
    jbyte *src_yuv_point = env->GetByteArrayElements(src_yuv, NULL);
    jbyte *dst_argb_point = env->GetByteArrayElements(dst_argb, NULL);
    int ret = -1;
    if (src_yuv_point != NULL && dst_argb_point != NULL) {
        jbyte *src_y = src_yuv_point + point_y;
        jbyte *src_u = src_yuv_point + point_u;
        jbyte *src_v = src_yuv_point + point_v;

        ret = libyuv::I420ToARGB(reinterpret_cast<const uint8 *>(src_y), src_stride_y,
                                 reinterpret_cast<const uint8 *>(src_u), src_stride_u,
                                 reinterpret_cast<const uint8 *>(src_v), src_stride_v,
                                 reinterpret_cast<uint8 *>(dst_argb_point), dst_stride_argb,
                                 with, height);
        env->ReleaseByteArrayElements(src_yuv, src_yuv_point, JNI_ABORT);
    }
    return ret;
}







