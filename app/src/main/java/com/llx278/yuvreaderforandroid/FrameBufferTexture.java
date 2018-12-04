package com.llx278.yuvreaderforandroid;

import android.content.Context;
import android.opengl.GLES30;

/**
 * 代表一个缓冲纹理
 */
public class FrameBufferTexture {

    private int frameBufferId;
    private int textureBufferId;

    private int frameWidth;
    private int frameHeight;

    private Rectangle rectangle;

    private float[] projectionMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private Context context;

    public FrameBufferTexture(Context context, int frameWidth, int frameHeight) {
        this.context = context;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    /**
     * @param textureWidth  这个FrameBuffer的宽
     * @param textureHeight 这个FrameBuffer的高
     */
    public void init(int textureWidth, int textureHeight) {
        android.opengl.Matrix.setIdentityM(mVMatrix, 0);
        android.opengl.Matrix.setIdentityM(projectionMatrix, 0);
        int[] frameBufferId = new int[1];
        GLES30.glGenFramebuffers(1, frameBufferId, 0);
        this.frameBufferId = frameBufferId[0];
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, this.frameBufferId);
        int[] renderBufferId = new int[1];
        GLES30.glGenRenderbuffers(1, renderBufferId, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderBufferId[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16, textureWidth, textureHeight);
        int[] textureId = new int[1];
        GLES30.glGenTextures(1, textureId, 0);
        this.textureBufferId = textureId[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId[0]);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, textureWidth, textureHeight,
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
        int status = GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);
        if (status != GLES30.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("check framebuffer status failed, current status : " + status);
        }
        // 计算FrameBuffer的宽高比
        float ratio = (float) this.frameWidth / this.frameHeight;
        // 计算投影矩阵
        android.opengl.Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1,
                2);
        // 设置摄像头参数
        android.opengl.Matrix.setLookAtM(mVMatrix, 0, 0, 0, 0, 0, 0,
                -1, 0f, 1.0f, 0.0f);
        float[] vertices = VertexHelper.calculateVertices3D(ratio, textureWidth, textureHeight, VertexHelper.ScaleToFit.CENTER);
        float[] textureCoordinate = VertexHelper.getTextureCoordinate(VertexHelper.DIRECTION.ANGLE_0);
        //rectangle.onVerticesDecided(vertices);
        this.rectangle = new Rectangle(context, vertices, textureCoordinate);
        // 切换回系统frameBuffer
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
    }

    /**
     * 绘制缓冲区结束以后，会调用GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)切换回系统的frameBuffer
     *
     * @param texIds 需要绘制到缓冲区的纹理id
     * @return 此缓冲区所绑定的纹理Id
     */
    public int draw(int texIds) {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, this.frameBufferId);
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        // 设置屏幕背景色
        //GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        // 重新设置viewPort
        GLES30.glViewport(0, 0, this.frameWidth, this.frameHeight);
        this.rectangle.draw(texIds, projectionMatrix, mVMatrix);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        return this.textureBufferId;
    }
}
