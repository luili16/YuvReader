package com.llx278.yuvreaderforandroid;

import android.opengl.GLES30;

import java.nio.ByteBuffer;

public class I420Texture implements ITexture {

    private ByteBuffer mYData;
    private int myWidth;
    private int myHeight;
    private ByteBuffer mUData;
    private int mUWidth;
    private int mUHeight;
    private ByteBuffer mVData;
    private int mVWidth;
    private int mVHeight;


    public I420Texture(byte[] srcY, int yWidth, int yHeight,
                       byte[] srcU, int uWidth, int uHeight,
                       byte[] srcV, int vWidth, int vHeight
    ) {
        mYData = ByteBuffer.allocate(srcY.length);
        mYData.put(srcY);
        mYData.position(0);

        mUData = ByteBuffer.allocate(srcU.length);
        mUData.put(srcU);
        mUData.position(0);

        mVData = ByteBuffer.allocate(srcV.length);
        mVData.put(srcV);
        mVData.position(0);

        myWidth = yWidth;
        myHeight = yHeight;
        mUWidth = uWidth;
        mUHeight = uHeight;
        mVWidth = vWidth;
        mVHeight = vHeight;
    }

    /**
     * @return int[0] y, int[1] u, int[2] v
     */
    public int[] initTexture() {
        int[] textureIds = new int[3];
        GLES30.glGenTextures(3, textureIds, 0);

        int yTextureId = textureIds[0];
        int uTextureId = textureIds[1];
        int vTextureId = textureIds[2];

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, yTextureId);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_LUMINANCE, myWidth, myHeight, 0,
                GLES30.GL_LUMINANCE, GLES30.GL_UNSIGNED_BYTE, mYData);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, uTextureId);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_LUMINANCE, mUWidth, mUHeight, 0,
                GLES30.GL_LUMINANCE, GLES30.GL_UNSIGNED_BYTE, mUData);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, vTextureId);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_LUMINANCE, mVWidth, mVHeight, 0,
                GLES30.GL_LUMINANCE, GLES30.GL_UNSIGNED_BYTE, mVData);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        return textureIds;
    }

    @Override
    public int getWidth() {
        return myWidth;
    }

    @Override
    public int getHeight() {
        return myHeight;
    }

}
