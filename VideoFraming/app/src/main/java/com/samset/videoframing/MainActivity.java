package com.samset.videoframing;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 101;
    private String path = null;
    MediaMetadataRetriever mediaMetadataRetriever;
    ImageView capturedImageView;
    private Animation fab_open;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        capturedImageView = (ImageView) findViewById(R.id.imageview);

        mediaMetadataRetriever = new MediaMetadataRetriever();
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);

        Button next = (Button) findViewById(R.id.next);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.startAnimation(fab_open);
                Intent intent;


                if (Build.VERSION.SDK_INT < 19) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/* video/*");
                    startActivityForResult(photoPickerIntent,REQUEST_TAKE_GALLERY_VIDEO);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    photoPickerIntent.setType("*/*");
                    startActivityForResult(photoPickerIntent, REQUEST_TAKE_GALLERY_VIDEO);
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK) {
            if (data.getData() != null) {


                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri);
                path = getPath(selectedImageUri);

                if (path!=null)
                {
                    Log.e("Path"," Pathh 0 "+path);
                    try {
                        Log.e("Path"," Pathh 1 "+path);
                        mediaMetadataRetriever.setDataSource(getBaseContext(),selectedImageUri);
                        Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
                        capturedImageView.setImageBitmap(bmFrame);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Main", "Exception " + e.toString());
                    }
                }

                Log.e("Path"," Pathh "+path);

            } else {
                Toast.makeText(getApplicationContext(), "Failed to select video", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
    private void framing(String newpath) {


        if (newpath != null) {

            try {
                mediaMetadataRetriever.setDataSource(newpath);
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
                capturedImageView.setImageBitmap(bmFrame);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Main", "Exception " + e.toString());
            }
        }


        mediaMetadataRetriever.release();
    }


}
