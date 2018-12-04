package com.llx278.yuvreaderforandroid;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 将一个Texture先画到一个FrameBuffer上去，然后在画到系统的FrameBuffer上去
 * 没有什么实际的意义,仅仅是演示frameBuffer的使用
 */
public class FrameBufferSceneRenderDemo implements GLSurfaceView.Renderer {

    private ITexture texture;
    private Rectangle rectangle;
    private FrameBufferTexture frameBufferTexture;

    private int width;
    private int height;

    private float[] projectionMatrix = new float[16];
    private float[] mVMatrix = new float[16];

    private int textureWidth;
    private int textureHeight;
    private ByteBuffer data;
    private Context context;

    public FrameBufferSceneRenderDemo(Context context, FrameBufferTexture frameBufferTexture, ByteBuffer data, int textureWidth, int textureHeight) {
        this.frameBufferTexture = frameBufferTexture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.data = data;
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        android.opengl.Matrix.setIdentityM(mVMatrix, 0);
        android.opengl.Matrix.setIdentityM(projectionMatrix, 0);
        // 设置屏幕背景颜色RGBA
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;

        // 计算GLSurfaceView的宽高比
        float ratio = (float) width / height;
        // 计算投影矩阵
        android.opengl.Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1,
                2);
        // 设置摄像头参数
        android.opengl.Matrix.setLookAtM(mVMatrix, 0, 0, 0, 0, 0, 0,
                -1, 0f, 1.0f, 0.0f);
        float[] vertices = VertexHelper.calculateVertices3D(ratio, textureWidth, textureHeight, VertexHelper.ScaleToFit.CENTER);
        float[] tex = VertexHelper.getTextureCoordinate(VertexHelper.DIRECTION.ANGLE_180);
        rectangle = new Rectangle(context, vertices, tex);
        texture = new Texture(data, textureWidth, textureHeight);
        this.frameBufferTexture.init(this.texture.getWidth(), this.texture.getHeight());
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 先画到frameBuffer
        int bufferTextureId = this.frameBufferTexture.draw(this.texture.getTextureId());
        // 设置系统frameBuffer的viewPort
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glViewport(0, 0, this.width, this.height);
        this.rectangle.draw(bufferTextureId, projectionMatrix, mVMatrix);
    }
}
