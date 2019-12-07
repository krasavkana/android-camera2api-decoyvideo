/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.krasavkana.android.decoyvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class VideoUIFragment extends Camera2VideoFragment {

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "VideoUIFragment";

    SharedPreferences mPref;

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
//    protected AutoFitTextureView mTextureView;

    /**
     * Button to record video
     */
//    protected Button mButtonVideo;

    /**
     * LENS Facing button is visible
     * Button to lens facing
     */
//    private boolean mButtonLensFacingOn;
    private ImageButton mButtonLensFacing;

    /**
     * Whether the app will record on start
     */
//    private boolean mIsRecordOnStart;

    /**
     * Whether CONTROL_AF_MODE is ON
     */
//    private boolean mIsAutoFocus;

    /**
     * Capture Intent Mode
     */
//    private String mCaptureIntent;

    public static VideoUIFragment newInstance() {
        return new VideoUIFragment();
    }

    // SWIPEイベントを検出するための定数とメンバ変数
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    // コンテキストメニュの表示方法をアクションモードにする
    private ActionMode mActionMode;

    /**
     * View.OnKeyListenerを設定する
     * http://outofmem.hatenablog.com/entry/2014/04/20/090047
     * https://stackoverflow.com/questions/7992216/android-fragment-handle-back-button-press
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        final View v =inflater.inflate(R.layout.fragment_camera2_video, container, false);

        // View#setFocusableInTouchModeでtrueをセットしておくこと
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // KeyEvent.ACTION_DOWN以外のイベントを無視する
                // （これがないとKeyEvent.ACTION_UPもフックしてしまう）
                Log.d(TAG, "onKey()");
                if(event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                switch(keyCode) {
//                    case KeyEvent.KEYCODE_VOLUME_UP:
//                        // TODO:音量増加キーが押された時のイベント
//                        return true;
                    case KeyEvent.KEYCODE_VOLUME_DOWN:
                        // TODO:音量減少キーが押された時のイベント
                        if (mIsRecordingVideo) {
                            stopRecordingVideo();
                        } else {
                            startRecordingVideo();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        Log.d(TAG, "GestureDetected: onDown()");
//                return false;
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        Log.d(TAG, "GestureDetected: onLongPress()");
                        super.onLongPress(e);
//                takePicture();
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        Log.d(TAG, "GestureDetected: onSingleTapUp()");
//                takePicture();
                        return false;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Log.d(TAG, "GestureDetected: onDoubleTap()");
                        if (mIsRecordingVideo) {
                            stopRecordingVideo();
                        } else {
                            startRecordingVideo();
                        }
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                        try {

                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                                // 縦の移動距離が大きすぎる場合は無視
                                return false;
                            }

                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                // 開始位置から終了位置の移動距離が指定値より大きい
                                // X軸の移動速度が指定値より大きい
                                Log.d(TAG, "GestureDetected: onFling() Swipe(Right to Left)");
                                mCallback.imageClickEvent(true);
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                // 終了位置から開始位置の移動距離が指定値より大きい
                                // X軸の移動速度が指定値より大きい
                                Log.d(TAG, "GestureDetected: onFling() Swipe(Left to Right)");
                                mCallback.imageClickEvent(false);
                            }

                        } catch (Exception e) {
                            // nothing
                        }
//                return false;
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        v.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
//                Log.d(TAG, "onTouch(): fragment");
                return gesture.onTouchEvent(event);
            }
        });


        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        mHandler = new Handler();
    }
    @Override
    public void onPause(){
        super.onPause();
        mHandler = null;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated()");
        Activity activity = getActivity();
        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture);

        mButtonVideo = (Button) view.findViewById(R.id.video);
        mButtonVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                        if (mIsRecordingVideo) {
                            stopRecordingVideo();
                        } else {
                            startRecordingVideo();
                        }
            }

        });
//        ImageView imageView = view.findViewById(R.id.image_view);
        mButtonLensFacing = view.findViewById(R.id.info);
        mButtonLensFacing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mIsLensFacingFront){
                    mIsLensFacingFront = false;
                }else{
                    mIsLensFacingFront = true;
                }
                // 録画中にLens向きを変更した場合、一旦録画を完了して、起動時自動録画する
                if (mIsRecordingVideo) {
                    stopRecordingVideo();
                    mIsRecordOnStart = true;
                    Toast.makeText(getActivity(), "Flipped", Toast.LENGTH_SHORT).show();
                }
                onPause();
                onResume();
            }

        });

        mButtonSave = view.findViewById(R.id.save);
        mButtonSave.setVisibility(View.INVISIBLE);

        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture);
        mTextureView.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) {
                if (mActionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = getActivity().startActionMode(actionModeCallback);
//                view.setSelected(true);
                return true;
            }
        });

    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.option, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuItem1:
                    Log.i(TAG, "onActionItemClicked(): menuItem1 was chosen");
                    startActivity(new Intent(getContext(), SettingsActivity.class));
//                    shareCurrentItem();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String aaa = mPref.getString("preference_theme", "");
        Log.d(TAG, "preference_theme:" + aaa);
        mIsLensFacingFront = mPref.getBoolean("preference_front_lens_facing", false);
        Log.d(TAG, "mLensFacingFront:" + mIsLensFacingFront);
        mPrefix = mPref.getString("preference_save_prefix", "");
        Log.d(TAG, "mPrefix:" + mPrefix);
        mIsRecordOnStart = mPref.getBoolean("preference_shoot_on_start", false);
        Log.d(TAG, "mIsRecordOnStart:" + mIsRecordOnStart);

        // ファインダの表示場所と大きさを変更する
        String finderLocation = mPref.getString("preference_finder_location", "ML");
        Log.d(TAG, "finderLocation:" + finderLocation);
        String finderSize = mPref.getString("preference_finder_size", "40x60");
        Log.d(TAG, "finderSize:" + finderSize);
        // ファインダの大きさを設定する
        int finderWidth = Integer.parseInt(finderSize.substring(0,finderSize.indexOf('x')));
        Log.d(TAG, "finderWidth:" + finderWidth);
        int finderHeight = Integer.parseInt(finderSize.substring(finderSize.indexOf('x')+1,finderSize.length()));
        Log.d(TAG, "finderHeight:" + finderHeight);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                getValueInDP(getContext(),40),getValueInDP(getContext(),60)
                getValueInDP(getContext(),finderWidth),getValueInDP(getContext(),finderHeight)
        );
        // ファインダの場所を設定する
        int M10DP = getValueInDP(getContext(),10);
        switch(finderLocation){
            case "TL":
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                lp.setMargins(M10DP,M10DP,0,0);
                break;
            case "TR":
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                lp.setMargins(0,M10DP,M10DP,0);
                break;
            case "ML":
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                lp.setMarginStart(M10DP);
                break;
            case "BL":
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                lp.setMargins(M10DP,0,0,M10DP);
                break;
            case "BR":
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                lp.setMargins(0,0,M10DP,M10DP);
                break;
        }
        mTextureView.setLayoutParams(lp);

        // 撮影ボタンの表示ONOFF
        boolean buttonVideoOn = mPref.getBoolean("preference_video_button_on", true);
        Log.d(TAG, "buttonVideoOn:" + buttonVideoOn);
        if(buttonVideoOn) {
            mButtonVideo.setVisibility(View.VISIBLE);
        }else{
            mButtonVideo.setVisibility(View.INVISIBLE);
        }

        // カメラ切り替えボタンの表示ONOFF
        boolean buttonLensFacingOn = mPref.getBoolean("preference_lens_facing_button_on", true);
        Log.d(TAG, "buttonLensFacingOn:" + buttonLensFacingOn);
        if(buttonLensFacingOn) {
            mButtonLensFacing.setVisibility(View.VISIBLE);
        }else{
            mButtonLensFacing.setVisibility(View.INVISIBLE);
        }

        // AF modeを変更する
        mIsAutoFocus = mPref.getBoolean("preference_af_mode_on", true);
        Log.d(TAG, "mIsAutoFocus:" + mIsAutoFocus);

        // Capture Intent modeを変更する
        mCaptureIntent = mPref.getString("preference_capture_intent", "VIDEO_RECORD");
        Log.d(TAG, "mCaptureIntent:" + mCaptureIntent);

    }

    // Does setWidth(int pixels) use dip or px?
    // https://stackoverflow.com/questions/2406449/does-setwidthint-pixels-use-dip-or-px
    // value in DP
    public static int getValueInDP(Context context, int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static float getValueInDP(Context context, float value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    // value in PX
    public static int getValueInPixel(Context context, int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, context.getResources().getDisplayMetrics());
    }

    public static float getValueInPixel(Context context, float value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, context.getResources().getDisplayMetrics());
    }

    public interface VideoUIFragmentCallback{
        public void imageClickEvent(boolean increment);
    }

    private VideoUIFragmentCallback mCallback;

    // ガイドにはこうは書かれていないようだが、これで動作するので一旦よしとする
    public void onAttach(Activity activity){
        mCallback = (VideoUIFragmentCallback) activity;
        super.onAttach(activity);
    }

}
