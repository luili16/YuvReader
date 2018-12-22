package com.llx278.yuvreaderforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Button showImage = findViewById(R.id.show_image);
        showImage.setOnClickListener(v -> startActivity(new Intent(SelectActivity.this, ShowImageActivity.class)));

        Button showYuvImage = findViewById(R.id.show_yuv_image);
        showYuvImage.setOnClickListener(v -> startActivity(new Intent(SelectActivity.this, ShowYuvImageActivity.class)));

        Button showYuvToRgb = findViewById(R.id.show_yuv_to_rgb);
        showYuvToRgb.setOnClickListener(v -> startActivity(new Intent(SelectActivity.this, ShowYuvImageToRGBActivity.class)));

        Button drawFrameBuffer = findViewById(R.id.show_frame_buffer_texture);
        drawFrameBuffer.setOnClickListener(v->{
            startActivity(new Intent(SelectActivity.this,DrawFrameBufferActivity.class));
        });

        Button preview = findViewById(R.id.show_camerea_preview);
        preview.setOnClickListener(v -> {
            startActivity(new Intent(SelectActivity.this,CameraPreviewActivity.class));
        });

    }
}
