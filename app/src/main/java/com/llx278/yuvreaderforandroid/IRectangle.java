package com.llx278.yuvreaderforandroid;

public interface IRectangle {
    void initShader();
    void onVerticesDecided(float[] vertices);
    void draw(int[] texIds,float[] projectMatrix,float[] mvMatrix);
}
