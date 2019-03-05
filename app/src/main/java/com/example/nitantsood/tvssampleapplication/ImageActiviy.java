package com.example.nitantsood.tvssampleapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.lang.UCharacter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.nitantsood.tvssampleapplication.EmployeeDetailActivity.photo;

public class ImageActiviy extends AppCompatActivity {

    final int mScreenHeight= Resources.getSystem().getDisplayMetrics().heightPixels;
    final int mScreenWidth=Resources.getSystem().getDisplayMetrics().widthPixels;
    Intent intent;
    String URL;
    ImageView imageView;
    Bitmap originalPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_activiy);


        intent=getIntent();
        URL=intent.getStringExtra("imageURL");
//        originalPhoto=intent.getE("originalBitmap");
        imageView=findViewById(R.id.imageView);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(URL,bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap,mScreenWidth,mScreenWidth,true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime());



        Canvas cs = new Canvas(bitmap);
        Paint tPaint = new Paint();
        tPaint.setTextSize(25);
        tPaint.setColor(Color.RED);
        tPaint.setStyle(Paint.Style.FILL);
//        cs.drawBitmap(bitmap, 0f, 0f, null);
        float height = tPaint.measureText("yY");
        float width=tPaint.measureText(dateTime);
        cs.drawText(dateTime, mScreenWidth-300f, mScreenWidth-50f, tPaint);

        imageView.setImageBitmap(bitmap);


    }
}
