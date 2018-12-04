package com.llx278.yuvreaderforandroid;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 将纹理渲染到屏幕上
 */
public class SimpleSceneRender implements GLSurfaceView.Renderer {

    private float[] mProjectionMatrix = new float[16];
    private float[] mMVMatrix = new float[16];
    private Rectangle rectangle;
    private Texture texture;
    private int width;
    private int height;
    private Context context;
    private ByteBuffer data;
    private int textureWidth;
    private int textureHeight;

    public SimpleSceneRender(Context context, ByteBuffer data, int textureWidth, int textureHeight) {
        this.context = context;
        this.data = data;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        android.opengl.Matrix.setIdentityM(mMVMatrix, 0);
        android.opengl.Matrix.setIdentityM(mProjectionMatrix, 0);
        // 设置屏幕背景颜色RGBA
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        texture = new Texture(data, textureWidth, textureHeight);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        // 计算GLSurfaceView的宽高比
        float ratio = (float) width / height;
        // 计算投影矩阵
        android.opengl.Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1,
                2);
        // 设置摄像头参数
        android.opengl.Matrix.setLookAtM(mMVMatrix, 0, 0, 0, 0, 0, 0,
                -1, 0f, 1.0f, 0.0f);

        float[] vertices = VertexHelper.calculateVertices3D(ratio, texture.getWidth(), texture.getHeight(), VertexHelper.ScaleToFit.CENTER);
        float[] textureCoordinate = VertexHelper.getTextureCoordinate(VertexHelper.DIRECTION.ANGLE_0);
        rectangle = new Rectangle(this.context, vertices, textureCoordinate);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除深度缓冲与颜色缓冲
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        GLES30.glViewport(0, 0, width, height);
        // 绘制三角形纹理
        rectangle.draw(texture.getTextureId(), mProjectionMatrix, mMVMatrix);
    }
}
