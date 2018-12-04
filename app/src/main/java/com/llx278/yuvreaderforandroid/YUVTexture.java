package com.llx278.yuvreaderforandroid;

import android.opengl.GLES30;

import java.nio.ByteBuffer;

public class YUVTexture implements ITexture {
    private int width;
    private int height;
    private int textureId;

    public YUVTexture(byte[] data, int width, int height) {
        this.width = width;
        this.height = height;
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.position(0);
        initTexTure(buffer);
    }

    public YUVTexture(ByteBuffer data, int width, int height) {
        this.width = width;
        this.height = height;
        initTexTure(data);
    }

    private void initTexTure(ByteBuffer data) {
        int[] textures = new int[1];
        GLES30.glGenTextures(
                1, // 产生纹理Id的数量
                textures, // 纹理id数组
                0); // 偏移

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_LUMINANCE, this.width, this.height,
                0, GLES30.GL_LUMINANCE, GLES30.GL_UNSIGNED_BYTE, data);
        this.textureId = textures[0];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getTextureId() {
        return this.textureId;
    }
}
