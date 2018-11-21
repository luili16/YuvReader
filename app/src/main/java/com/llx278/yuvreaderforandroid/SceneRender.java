package com.llx278.yuvreaderforandroid;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SceneRender implements GLSurfaceView.Renderer {

    private float[] mProjectionMatrix = new float[16];
    private float[] mMVMatrix = new float[16];
    private int[] mIds;
    private IRectangle mRectangle;
    private ITexture mTexture;

    public SceneRender(ITexture texture, IRectangle rectangle) {
        mRectangle = rectangle;
        mTexture = texture;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        android.opengl.Matrix.setIdentityM(mMVMatrix, 0);
        android.opengl.Matrix.setIdentityM(mProjectionMatrix, 0);
        // 设置屏幕背景颜色RGBA
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        mRectangle.initShader();
        mIds = mTexture.initTexture();
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
        float[] vertices = VertexHelper.calculateVertices3D(ratio, mTexture.getWidth(), mTexture.getHeight(), VertexHelper.ScaleToFit.CENTER);
        mRectangle.onVerticesDecided(vertices);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除深度缓冲与颜色缓冲
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        // 绘制三角形纹理
        mRectangle.draw(mIds, mProjectionMatrix, mMVMatrix);
    }
}
