package com.example.nitantsood.tvssampleapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmployeeDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView employeeNameD, employeeIDD, employeeOccupationD, employeeSalaryD, employeeDOJD, employeeLocD;
    ImageView employeeImage;
    TextView captureImageText;
    OneCustomerDetail oneCustomerDetail;
    String mCurrentPhotoPath;
    public static Bitmap photo;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    boolean isImageLoaded=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        Intent intentFromMain = getIntent();
        oneCustomerDetail = (OneCustomerDetail) intentFromMain.getSerializableExtra("clickedEmployeeDetail");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    protected void onStart() {
        super.onStart();

        employeeNameD = findViewById(R.id.empNameD);
        employeeIDD = findViewById(R.id.empIDD);
        employeeOccupationD = findViewById(R.id.empOccupationD);
        employeeSalaryD = findViewById(R.id.empSalaryD);
        employeeDOJD = findViewById(R.id.empDOJD);
        employeeLocD = findViewById(R.id.empLocD);
        employeeImage = findViewById(R.id.empImage);
        captureImageText = findViewById(R.id.captureImage);

        employeeImage.setTransitionName("EmployeeImageToTransition");

        employeeNameD.setText("Name : " + oneCustomerDetail.getName());
        employeeIDD.setText("ID : " + oneCustomerDetail.getId());
        employeeOccupationD.setText("Position : " + oneCustomerDetail.getOccupation());
        employeeSalaryD.setText("Salary : " + oneCustomerDetail.getPrice());
        employeeDOJD.setText("Date of joining : " + oneCustomerDetail.getDate());
        employeeLocD.setText("Location : " + oneCustomerDetail.getLocation());

        captureImageText.setOnClickListener(this);
        employeeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageLoaded) {
                    Intent intent = new Intent(getApplicationContext(), ImageActiviy.class);
                    intent.putExtra("imageURL", mCurrentPhotoPath);
                    startActivity(intent);
                }
                else
                    Toast.makeText(EmployeeDetailActivity.this, "Please Upload a Image first", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
//            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
//                Intent cameraIntent = new
//                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

//    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
//           if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//               photo = (Bitmap) data.getExtras().get("data");
//               if(photo!=null)
//               employeeImage.setImageBitmap(photo);
//               timestampItAndSave(photo);
//           }
//    }



//    private void timestampItAndSave(Bitmap bitmap){
////        BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
////        Bitmap bitmap = BitmapFactory.decodeFile(getOutputMediaFile().getAbsolutePath());
//
//        //        Bitmap src = BitmapFactory.decodeResource(); // the original file is cuty.jpg i added in resources
//        Bitmap dest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system
//
//        Canvas cs = new Canvas(dest);
//        Paint tPaint = new Paint();
//        tPaint.setTextSize(5);
//        tPaint.setColor(Color.RED);
//        tPaint.setStyle(Paint.Style.FILL);
//        cs.drawBitmap(bitmap, 0f, 0f, null);
//        float height = tPaint.measureText("yY");
//        cs.drawText(dateTime, 15f, height+15f, tPaint);
////        employeeImage.setImageBitmap(dest);
//
//        try {
//            OutputStream outputStream=null;
//            File imageFile=createImageFile();
//            outputStream=new FileOutputStream(imageFile);
//
//            dest.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e) {
//            Toast.makeText(this, "Awww", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
////        try {
////            if(dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/sdcard/timeStampedImage.jpg")))){
////                employeeImage.setImageBitmap(dest);
////            }
////        } catch (FileNotFoundException e) {
////            Toast.makeText(this, "Error1", Toast.LENGTH_SHORT).show();
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//    }








    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.nitantsood.tvssampleapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case 1888: {
                    if (resultCode == RESULT_OK) {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
//                            Toast.makeText(this,mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
                            isImageLoaded=true;
                            employeeImage.setImageBitmap(bitmap);
//                            timestampItAndSave(bitmap);
                        }
                    }
                    break;
                }
            }

        } catch (Exception error) {

            error.printStackTrace();
        }
    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
