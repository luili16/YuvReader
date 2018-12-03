package com.llx278.yuvreaderforandroid;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BufferSceneRender implements GLSurfaceView.Renderer {

    private float[] mProjectionMatrix = new float[16];
    private float[] mMVMatrix = new float[16];

    private IRectangle rectangleTemp;
    private IRectangle rectangle;
    private ITexture texture;
    private int frameBufferId;
    private int renderDepthBufferId;
    private int textureBufferId;
    private int textureId;

    public BufferSceneRender(ITexture texture, IRectangle rectangleTemp,IRectangle rectangle) {
        this.rectangleTemp = rectangleTemp;
        this.rectangle = rectangle;
        this.texture = texture;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        android.opengl.Matrix.setIdentityM(mMVMatrix, 0);
        android.opengl.Matrix.setIdentityM(mProjectionMatrix, 0);
        // 设置屏幕背景颜色RGBA
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        rectangleTemp.initShader();
        rectangle.initShader();
        textureId = this.texture.initTexture()[0];
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        // 计算GLSurfaceView的宽高比
        float ratio = (float) width / height;
        // 计算投影矩阵
        android.opengl.Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1,
                2);
        // 设置摄像头参数
        android.opengl.Matrix.setLookAtM(mMVMatrix, 0, 0, 0, 0, 0, 0,
                -1, 0f, 1.0f, 0.0f);
        float[] vertices = VertexHelper.calculateVertices3D(ratio, 720, 1280, VertexHelper.ScaleToFit.CENTER);
        this.rectangleTemp.onVerticesDecided(vertices);
        this.rectangle.onVerticesDecided(vertices);
        // 初始化帧缓冲区
        int status = initFRBuffer();
        if (status == GLES30.GL_FRAMEBUFFER_COMPLETE) {
            Log.d("main", "open gl generate frame buffer success!!");
            // 先绘制到缓冲区
        } else {
            Log.d("main", "open gl gen framebuffer failed");
        }
    }

    private int initFRBuffer() {
        int[] frameBufferId = new int[1];
        GLES30.glGenFramebuffers(1, frameBufferId, 0);
        this.frameBufferId = frameBufferId[0];
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId[0]);
        int[] renderBufferId = new int[1];
        GLES30.glGenRenderbuffers(1, renderBufferId, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderBufferId[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16, 720, 1280);
        this.renderDepthBufferId = renderBufferId[0];
        int[] textureId = new int[1];
        GLES30.glGenTextures(1, textureId, 0);
        this.textureBufferId = textureId[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId[0]);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, 720, 1280,
                // pixels为null，需要由外部来写入
                0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);
        // frameBuffer绑定纹理
        GLES30.glFramebufferTexture2D(
                GLES30.GL_FRAMEBUFFER,
                GLES30.GL_COLOR_ATTACHMENT0,
                GLES30.GL_TEXTURE_2D,
                textureId[0],
                0);
        // frameBuffer绑定深度
        GLES30.glFramebufferRenderbuffer(
                GLES30.GL_FRAMEBUFFER,
                GLES30.GL_DEPTH_ATTACHMENT,
                GLES30.GL_RENDERBUFFER,
                renderBufferId[0]);
        // check for framebuffer complete
        return GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除深度缓冲与颜色缓冲
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        int[] is = new int[1];
        GLES30.glGetIntegerv(GLES30.GL_MAX_RENDERBUFFER_SIZE,is,0);
        Log.d("main","glMaxbufferSize : " + is[0]);

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, this.frameBufferId);
        this.rectangleTemp.draw(new int[]{textureId}, mProjectionMatrix, mMVMatrix);
        // 再绘制rectangle
        // 0 指绘制到当前的窗体上
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        this.rectangle.draw(new int[]{textureBufferId}, mProjectionMatrix, mMVMatrix);

    }
}
