package com.krasavkana.android.decoyvideo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class VideoActivity extends AppCompatActivity
        implements VideoUIFragment.VideoUIFragmentCallback {

    private Bitmap image;
    private boolean isFinished = false;
    private File[] mFiles;
    private int mFileNum = 0;
    private int mFilePos = -1;
    private ImageView mImageView;

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "VideoActivity";

    private static final String CAMOUFLAGE_MODE_IMAGEVIEW = "imageview";
    private static final String CAMOUFLAGE_MODE_TRANSLUCENT = "trunslucent";
    private static final String CAMOUFLAGE_MODE_THEME = "theme";

//    private final static String MASTER_MUTE_PACKAGE_NAME = "com.krasavkana.android.mastermute";
//    private final static String MASTER_MUTE_CLASS_NAME = "com.krasavkana.android.mastermute.MainActivity";
//    private static final int REQUEST_CODE_MASTER_MUTE = 1;

    SharedPreferences mPref;
    String mCamouflage;
    boolean mCamouflageImageview;
    //    boolean mMasterMuteOn;
    String mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
        setContentView(R.layout.activity_camera);
        //
        //ActionModeを入れたのでActionBarの実装コードを削除する。
        //menuの設定とLayout定義を変更（原則削除）した
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setTitle(R.string.shortAppName);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, VideoUIFragment.newInstance())
                    .commit();
        }
        mPref = PreferenceManager.getDefaultSharedPreferences(this);

        mCamouflage = mPref.getString("preference_camouflage", "");
        Log.d(TAG, "mCamouflage:" + mCamouflage);
        if(CAMOUFLAGE_MODE_IMAGEVIEW.equals(mCamouflage)) {
            mCamouflageImageview = true;

            // get file list from the external storage path
            String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
            if (path == null) {
                Toast.makeText(this, "Check external file dir.", Toast.LENGTH_SHORT).show();
                finish();
            }
            mFiles = new File(path).listFiles();
            mFileNum = mFiles.length;
            if (mFileNum == 0) {
                Toast.makeText(this, "Camouflage images not found.", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "Num of image files: " + mFileNum);
            mFilePos = -1;
            mImageView = findViewById(R.id.image_view);
        }
        mTheme = mPref.getString("preference_theme", getString(R.string.default_value_preference_theme));
        Log.d(TAG, "mCamouflage:" + mCamouflage);
        FrameLayout root = findViewById(R.id.container);
        if(CAMOUFLAGE_MODE_THEME.equals(mCamouflage)) {
            switch (mTheme) {
                case "light":
                    root.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    break;
                case "dark":
                    root.setBackgroundColor(Color.parseColor("#000000"));
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        isFinished = true;
    }
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        isFinished = true;
    }
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
        isFinished = true;
    }
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        isFinished = true;
    }

//    // メニューをActivity上に設置する
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // 参照するリソースは上でリソースファイルに付けた名前と同じもの
//        getMenuInflater().inflate(R.menu.option, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    // メニューが選択されたときの処理
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menuItem1:
//                startActivity(new Intent(VideoActivity.this, SettingsActivity.class));
//                return true;
//
//            case R.id.menuItem2:
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public void imageClickEvent(boolean increment) {
        if(! mCamouflageImageview) return;

        if(increment) {
            mFilePos++;
            if (mFilePos >= mFileNum) {
                mFilePos = 0;
            }
        }else{
            mFilePos--;
            if (mFilePos < 0) {
                mFilePos = mFileNum - 1;
            }
        }
        Log.d(TAG, "Current Pos in image list: " + mFilePos);
        try (InputStream inputStream0 =
                     new FileInputStream(mFiles[mFilePos])) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream0);
            mImageView.setImageBitmap(bitmap);
            mImageView.invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
