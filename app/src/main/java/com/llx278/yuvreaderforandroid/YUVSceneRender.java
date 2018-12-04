package com.llx278.yuvreaderforandroid;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.llx278.yuvreaderforandroid.util.BitMapUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class YUVSceneRender implements GLSurfaceView.Renderer {

    private float[] mProjectionMatrix = new float[16];
    private float[] mMVMatrix = new float[16];

    private Context context;
    private ITexture[] textures;
    private YuvRectangle rectangle;
    private int width;
    private int height;

    // textures[0] Y textures[1] U textures[2] V
    public YUVSceneRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        android.opengl.Matrix.setIdentityM(mMVMatrix, 0);
        android.opengl.Matrix.setIdentityM(mProjectionMatrix, 0);
        // 设置屏幕背景颜色RGBA
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        initTexture();
    }

    private void initTexture() {
        int width = 256;
        int height = 256;
        byte[] rawYuv = BitMapUtil.getYuvRawBuffer(context, R.raw.lena_256x256_yuv420p);
        int pixels = width * height;
        byte[] dstY = new byte[pixels];
        byte[] dstU = new byte[pixels / 4];
        byte[] dstV = new byte[pixels / 4];
        int pointY = 0;
        int pointU = pixels + pixels / 4;
        int pointV = pixels;
        System.arraycopy(rawYuv, pointY, dstY, 0, pixels);
        System.arraycopy(rawYuv, pointV, dstV, 0, pixels / 4);
        System.arraycopy(rawYuv, pointU, dstU, 0, pixels / 4);
        //ITexture texture = new I420Texture(dstY, width, height, dstV, width / 2, height / 2, dstU, width / 2, height / 2);
        ITexture yTexture = new YUVTexture(dstY, width, height);
        ITexture uTexture = new YUVTexture(dstU, width / 2, height / 2);
        ITexture vTexture = new YUVTexture(dstV, width / 2, height / 2);
        // 这里面先v后u的原因是，fragmentShader里面写反了，暂时先不改
        this.textures = new ITexture[]{yTexture, vTexture, uTexture};
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

        float[] vertices = VertexHelper.calculateVertices3D(ratio, textures[0].getWidth(), textures[0].getHeight(), VertexHelper.ScaleToFit.CENTER);
        float[] textureCoordinate = VertexHelper.getTextureCoordinate(VertexHelper.DIRECTION.ANGLE_0);
        rectangle = new YuvRectangle(context, vertices, textureCoordinate);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
// 清除深度缓冲与颜色缓冲
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        GLES30.glViewport(0, 0, width, height);
        // 绘制三角形纹理
        rectangle.draw(new int[]{textures[0].getTextureId(), textures[1].getTextureId(), textures[2].getTextureId()},
                mProjectionMatrix,
                mMVMatrix);
    }
}
