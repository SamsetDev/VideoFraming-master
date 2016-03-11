package com.samset.videoframing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.samset.videoframing.fragments.FrameDisplayFragment;
import com.samset.videoframing.utils.PassData;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by weesync on 03/03/16.
 */
public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    //Component
    Button buttonCapture, pick, showFrame;
    VideoView myVideoView;

    //VideoFraming class
    MediaMetadataRetriever mediaMetadataRetriever;
    MediaController myMediaController;


    //String viewSource = "/storage/emulated/0/Video/Afgan.mp4";

    int VideoDuration;
    private String path = null;
    public static ArrayList<Bitmap> rev = new ArrayList<Bitmap>();
    public static final String TAG = "SecondActivity";
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_frame);
        setView();

        mediaMetadataRetriever = new MediaMetadataRetriever();

        pick.setOnClickListener(this);
        showFrame.setOnClickListener(this);
        buttonCapture.setOnClickListener(this);
    }

    private void setView() {
        myVideoView = (VideoView) findViewById(R.id.videoview);
        buttonCapture = (Button) findViewById(R.id.capture);
        showFrame = (Button) findViewById(R.id.shows);
        pick = (Button) findViewById(R.id.getdata);
    }


    private void getDuration() {/*
    int duration = 0;
    String dur = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    if (dur != null) {
        duration = Integer.parseInt(dur);
    }
    mDuration = duration;
    long h = duration / 3600;
    long m = (duration - h * 3600) / 60;
    long s = duration - (h * 3600 + m * 60);*/
    }

    /***********************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri selectedImage = data.getData();
                path = getPath(selectedImage);
                startVideo(path);

            } else {
                Toast.makeText(getApplicationContext(), "Failed to select video", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String getPath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);

        int columnindex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String file_path = cursor.getString(columnindex);
        Log.e(getClass().getName(), "file_path" + file_path);
        Uri fileUri = Uri.parse("file://" + file_path);
        cursor.close();
        return file_path;

    }

    @Override
    public void onClick(View v) {
        if (v == pick) {
            getVideoData();
        } else if (v == buttonCapture) {
            captureFrame();
        } else if (v == showFrame) {
            Log.e(getClass().getName(), "Call0 ");
            Bundle bundle = new Bundle();
            myVideoView.pause();
            //bundle.putSerializable("data", rev);
            PassData.getInstance().setData(rev);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, FrameDisplayFragment.getinstance()).commit();
            //startActivity(new Intent(SecondActivity.this, ShowFrameActivity.class));

            Log.e(getClass().getName(), "Call ");
        }

    }

    private void startVideo(String videoPath) {
        new FramingAsyntask().execute(videoPath);
        myVideoView.setVideoURI(Uri.parse(videoPath));
        myMediaController = new MediaController(this);
        myVideoView.setMediaController(myMediaController);

        myVideoView.setOnCompletionListener(myVideoViewCompletionListener);
        myVideoView.setOnPreparedListener(MyVideoViewPreparedListener);
        myVideoView.setOnErrorListener(myVideoViewErrorListener);

        myVideoView.requestFocus();
        myVideoView.start();

    }


    public class FramingAsyntask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                mediaMetadataRetriever.setDataSource(SecondActivity.this, Uri.parse(params[0]));
                String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                VideoDuration = Integer.parseInt(time);

                //int millis = mp.getDuration();

                for (int i = 1000000; i < VideoDuration * 1000; i += 1000000) {
                    Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(i, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    rev.add(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception " + e.toString());
            }
            mediaMetadataRetriever.release();
            Log.e(TAG, "Video durartion " + VideoDuration);
            Log.e(TAG, "Video frame size " + rev.size());
            return null;
        }
    }

    private void getVideoData() {
        Intent intent;
        /*if (Build.VERSION.SDK_INT < 19) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image*//* video*//*");
            startActivityForResult(photoPickerIntent, REQUEST_TAKE_GALLERY_VIDEO);
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("/*");
            startActivityForResult(photoPickerIntent, REQUEST_TAKE_GALLERY_VIDEO);
        }

*/





       if (Build.VERSION.SDK_INT < 19) {
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            } else {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            }
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
        } else {
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            }
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
        }


    }

    private void captureFrame() {
        int currentPosition = myVideoView.getCurrentPosition(); //in millisecond
        Toast.makeText(SecondActivity.this, "Current Position: " + currentPosition + " (ms)", Toast.LENGTH_LONG).show();

        Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(currentPosition * 1000); //unit in microsecond

        if (bmFrame == null) {
            Toast.makeText(SecondActivity.this, "bmFrame == null!",
                    Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder myCaptureDialog =
                    new AlertDialog.Builder(SecondActivity.this);
            ImageView capturedImageView = new ImageView(SecondActivity.this);
            capturedImageView.setImageBitmap(bmFrame);
            ViewGroup.LayoutParams capturedImageViewLayoutParams =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            capturedImageView.setLayoutParams(capturedImageViewLayoutParams);

            myCaptureDialog.setView(capturedImageView);
            myCaptureDialog.show();
        }

    }

    MediaPlayer.OnCompletionListener myVideoViewCompletionListener =
            new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
                    Toast.makeText(SecondActivity.this, "End of Video",
                            Toast.LENGTH_LONG).show();
                }
            };

    MediaPlayer.OnPreparedListener MyVideoViewPreparedListener =
            new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    long duration = myVideoView.getDuration(); //in millisecond
                    Toast.makeText(SecondActivity.this,
                            "Duration: " + duration + " (ms)",
                            Toast.LENGTH_LONG).show();

                }
            };

    MediaPlayer.OnErrorListener myVideoViewErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            Toast.makeText(SecondActivity.this,
                    "Error!MediaPlayer!!",
                    Toast.LENGTH_LONG).show();
            return true;
        }
    };

}
