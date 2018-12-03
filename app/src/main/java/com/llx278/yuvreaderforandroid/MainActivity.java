package com.llx278.yuvreaderforandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.llx278.yuvreaderforandroid.util.MyGLUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {


    GLSurfaceView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = findViewById(R.id.activity_main_gl_surface_view);
        if (MyGLUtils.detectOpenGLES30(this)) {
            mView.setEGLContextClientVersion(3);
            Bitmap bitmap = getBitmap();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            ByteBuffer b = ByteBuffer.allocate(bitmap.getByteCount());
            bitmap.copyPixelsToBuffer(b);
            b.position(0);
            /*int width ;
            int height;
            width = 256;
            height = 256;
            byte[] rawYuv = new byte[0];
            try {
                rawYuv = getYuvRawBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            int srcStrideY = width;
            int srcStrideU = width / 2;
            int srcStrideV = width / 2;*/
            ITexture texture;
            IRectangle rectangle;
            /*byte[] dstArgb = new byte[width * height * 4];
            int dstStrideArgb = width * 4;
            //YuvToRGB.I420ToARGB(dstY, srcStrideY, dstU, srcStrideU, dstV, srcStrideV, dstArgb, dstStrideArgb, width, height);
            YuvToRGB.I420ToARGBWithPackage(rawYuv, pointY, srcStrideY, pointU, srcStrideU, pointV,
                    srcStrideV, dstArgb, dstStrideArgb, width, height);
            rectangle = new Rectangle(this);
            ByteBuffer dst = ByteBuffer.allocate(dstArgb.length);
            dst.put(dstArgb);
            dst.position(0);
            texture = new Texture(dst, width, height);*/
            //texture = new I420Texture(dstY,width,height,dstV,width/2,height/2,dstU,width/2,height/2);
            //rectangle = new I420Rectangle(this);
            texture = new Texture(b, width, height);
            rectangle = new Rectangle(this);
            //SceneRender sr = new SceneRender(texture, rectangle);
            BufferSceneRender sr = new BufferSceneRender(texture, rectangle,new Rectangle(this));
            mView.setRenderer(sr);
            mView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        } else {
            throw new RuntimeException("not support GLES3.0");
        }
    }

    private byte[] getYuvRawBuffer() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.lena_256x256_yuv420p);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(temp)) != -1) {
            bos.write(temp, 0, len);
        }
        bos.close();
        inputStream.close();
        return bos.toByteArray();
    }

    private Bitmap getBitmap() {
        InputStream inputStream = getResources().openRawResource(R.raw.aa);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }
}
