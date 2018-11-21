package com.llx278.yuvreaderforandroid;

public class VertexHelper {
    //copy from android.graphics.Matrix.ScaleToFit
    public enum ScaleToFit {
        /**
         * Scale in X and Y independently, so that src matches dst exactly. This may change the
         * aspect ratio of the src.
         */
        FILL(0),
        /**
         * Compute a scale that will maintain the original src aspect ratio, but will also ensure
         * that src fits entirely inside dst. At least one axis (X or Y) will fit exactly. START
         * aligns the result to the left and top edges of dst.
         */
        START(1),
        /**
         * Compute a scale that will maintain the original src aspect ratio, but will also ensure
         * that src fits entirely inside dst. At least one axis (X or Y) will fit exactly. The
         * result is centered inside dst.
         */
        CENTER(2),
        /**
         * Compute a scale that will maintain the original src aspect ratio, but will also ensure
         * that src fits entirely inside dst. At least one axis (X or Y) will fit exactly. END
         * aligns the result to the right and bottom edges of dst.
         */
        END(3);

        // the native values must match those in SkMatrix.h
        ScaleToFit(int nativeInt) {
            this.nativeInt = nativeInt;
        }

        final int nativeInt;
    }

    /**
     * 根据纹理的宽和高计算纹理图片在opengl世界中的位置
     *
     * @param ratio  屏幕的宽与高的比 ratio = W / H
     * @param width  纹理图片的宽
     * @param height 纹理图片的高
     * @param fit    缩放类型
     * @return opengl世界中的4个顶点的坐标值
     */
    public static float[] calculateVertices3D(float ratio, int width, int height, ScaleToFit fit) {
        float[] points;
        float srcRatio = (float) width / (float) height;
        float srcHeight;
        float srcWidth;
        if (ratio > srcRatio) { // 固定高
            srcHeight = 2f;
            srcWidth = srcHeight * srcRatio;
        } else { // 固定宽
            srcWidth = 2f * ratio;
            srcHeight = srcWidth / srcRatio;
        }

        switch (fit) {
            case END:
                points = new float[]{ // 4 * 3
                        ratio - srcWidth, -srcHeight / 2, -1,   // 左下
                        ratio, -1, -1,   // 右下
                        ratio - srcWidth, srcHeight / 2, -1,  // 左上
                        ratio, 1, -1   // 右上
                };
                break;
            case FILL:
                points = new float[]{
                        -ratio, -1, -1,
                        ratio, -1, -1,
                        -ratio, 1, -1,
                        ratio, 1, -1
                };
                break;
            case START:
                points = new float[]{ // 4 * 3
                        -ratio, -srcHeight / 2, -1,   // 左下
                        -ratio + srcWidth, -srcHeight / 2, -1,   // 右下
                        -ratio, 1, -1,  // 左上
                        -ratio + srcWidth, srcHeight / 2, -1   // 右上
                };
                break;
            case CENTER:
                points = new float[]{ // 4 * 3
                        -srcWidth / 2, -srcHeight / 2, -1,   // 左下
                        srcWidth / 2, -srcHeight / 2, -1,   // 右下
                        -srcWidth / 2, srcHeight / 2, -1,  // 左上
                        srcWidth / 2, srcHeight / 2, -1   // 右上
                };
                break;
            default:
                throw new IllegalArgumentException("unsupported scale param");
        }
        return points;
    }
}
