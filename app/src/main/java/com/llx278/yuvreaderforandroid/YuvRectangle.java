package com.llx278.yuvreaderforandroid;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.llx278.yuvreaderforandroid.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class YuvRectangle {

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexCoorBuffer;
    private final String mVertexShader;
    private final String mFragmentShader;
    private int mProgram;
    private int maPositionHandler;
    private int maTexCoorHandler;
    private int muMVPMatrixHandler;

    private int mTexYHandler;
    private int mTexUHandler;
    private int mTexVHandler;

    // 最终的矩阵
    private float[] mMVPMatrix = new float[16];
    // 具体物体的移动旋转矩阵
    private float[] mMMatrix = new float[16];

    private boolean isInit = false;

    public YuvRectangle(Context context,float[] vertices, float[] textureCoordinate) {

        // 加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.readFromAssets("shader/yuv_texture/vertex.vert",
                context.getResources());
        // 加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.readFromAssets("shader/yuv_texture/fragment.frag",
                context.getResources());

        // 绘制方向逆时针，
        // 创建顶点坐标缓冲
        FloatBuffer vbb =
                ByteBuffer.
                        allocateDirect(vertices.length * 4).
                        order(ByteOrder.nativeOrder()).
                        asFloatBuffer().
                        put(vertices);
        vbb.position(0);
        mVertexBuffer = vbb;
        // 创建纹理坐标缓冲
        // 方向逆时针，
        float texCoor[] = new float[]{
                0f, 1f,
                1f, 1f,
                0f, 0f,
                1f, 0f
        };
        FloatBuffer cbb =
                ByteBuffer.
                        allocateDirect(texCoor.length * 4).
                        order(ByteOrder.nativeOrder()).
                        asFloatBuffer().put(texCoor);
        cbb.position(0);
        mTexCoorBuffer = cbb;
    }

    private void initShader() {
        // 基于顶点着色器和片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        // 获取程序顶点位置属性引用
        maPositionHandler = GLES30.glGetAttribLocation(mProgram, "aPosition");
        // 获取程序纹理坐标属性引用
        maTexCoorHandler = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        // 获取程序中总变换矩阵属性引用
        muMVPMatrixHandler = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");

        mTexYHandler = GLES30.glGetUniformLocation(mProgram, "tex_y");
        mTexUHandler = GLES30.glGetUniformLocation(mProgram, "tex_u");
        mTexVHandler = GLES30.glGetUniformLocation(mProgram, "tex_v");
    }

    // 0 y 1 u 2 v
    public void draw(int texId[], float[] projectMatrix, float[] mvMatrix) {
        if (!isInit) {
            initShader();
            isInit = true;
        }

        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.setIdentityM(mMMatrix, 0);
        GLES30.glUseProgram(mProgram);
        Matrix.multiplyMM(mMVPMatrix, 0, mvMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, projectMatrix, 0, mMVPMatrix, 0);

        // 将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandler, 1, false, mMVPMatrix, 0);
        // 将顶点位置数据传入渲染管线
        GLES30.glVertexAttribPointer(maPositionHandler, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        // 将纹理坐标位置传送进渲染管线
        GLES30.glVertexAttribPointer(maTexCoorHandler, 2, GLES30.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);

        // 允许顶点位置数据组
        GLES30.glEnableVertexAttribArray(maPositionHandler);
        GLES30.glEnableVertexAttribArray(maTexCoorHandler);

        // 绑定Y纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId[0]);
        GLES30.glUniform1i(mTexYHandler, 0);
        // 绑定U纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,texId[1]);
        GLES30.glUniform1i(mTexUHandler,1);
        // 绑定V纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE2);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,texId[2]);
        GLES30.glUniform1i(mTexVHandler,2);

        // 以三角形方式填充
        // 以三角形的方式填充
        int vCount = 4;
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, vCount);

        GLES30.glDisableVertexAttribArray(maTexCoorHandler);
        GLES30.glDisableVertexAttribArray(maPositionHandler);
        GLES30.glUseProgram(0);
    }
}
