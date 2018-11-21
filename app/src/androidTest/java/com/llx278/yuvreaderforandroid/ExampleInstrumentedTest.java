package com.llx278.yuvreaderforandroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.llx278.yuvreaderforandroid", appContext.getPackageName());

        int width = 100;
        int height = 100;
        int[] yuv = new int[width * height];
        int pixels = width * height;
        int yBegin = 0;
        int uBegin = width * height;
        int vBegin = pixels + pixels / 4;
        int step = 2;
        for (int i = 0; i < height; i += step) {
            for (int j = 0; j < width; j += step) {
                int yIndex = j + i * width;
                int uIndex = (yIndex % width) / 2 + ((i / 2) * width) / 2;
                int vIndex = uIndex;
                //Log.d("main", "yIndex : " + yIndex + " uIndex : " + uIndex + " vIndex : " + vIndex + " bfore : " + ((yIndex % width) / 2) + " after : " + (((i / 2) * width) / 2));

            }
        }
    }
}
