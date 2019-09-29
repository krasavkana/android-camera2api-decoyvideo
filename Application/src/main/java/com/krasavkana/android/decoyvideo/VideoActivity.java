package com.krasavkana.android.decoyvideo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class VideoActivity extends AppCompatActivity {

//    private Bitmap image;
//    private boolean isFinished = false;
//    private File[] mFiles;
//    private int mFileNum = 0;
//    private int mFilePos = -1;
//    private ImageView mImageView;

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, DecoyVideoFragment.newInstance())
                    .commit();
        }

        // get file list from the external storage path
//        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
//        if (path == null ){
//            Toast.makeText(this, "Check external file dir.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        mFiles = new File(path).listFiles();
//        mFileNum = mFiles.length;
//        if (mFileNum == 0 ) {
//            Toast.makeText(this, "Camouflage images not found.", Toast.LENGTH_SHORT).show();
//        }
//        Log.d(TAG, "Num of image files: " + mFileNum);
//        if (savedInstanceState != null) {
//            mFilePos = -1;
//        }else{
//            mFilePos = -1;
//        }
//        mImageView = findViewById(R.id.image_view);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
//        isFinished = true;
    }
    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
//        isFinished = true;
    }
    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        // set handler to get image for imageview
//        final Handler handler = new Handler();
//        handler.post( new Runnable() {
//            @Override
//            public void run() {
//                mFilePos++;
//                if (mFilePos >= mFileNum){
//                    mFilePos = 0;
//                }
//
//                Log.d(TAG, "Current Pos in image list: " + mFilePos);
////                Log.d(TAG, mFiles[mFilePos].getPath());
//
//                try (InputStream inputStream0 =
//                             new FileInputStream(mFiles[mFilePos])) {
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream0);
//                    mImageView.setImageBitmap(bitmap);
//                    mImageView.invalidate();
//                    handler.postDelayed(this, 20000);
//                    if (isFinished) {
//                        handler.removeCallbacks(this);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        isFinished = false;
    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
//        isFinished = true;
    }
}
