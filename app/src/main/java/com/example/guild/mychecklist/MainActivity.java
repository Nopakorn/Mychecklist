package com.example.guild.mychecklist;


import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;




public class MainActivity extends Activity{


    private final Handler mHandler = new Handler();

    private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
        @Override
        public boolean onGesture(Gesture gesture) {
            if (gesture == Gesture.TAP) {
                mAudioManager.playSoundEffect(Sounds.TAP);
                startCheck();
                return true;
            } else {
                return false;
            }
        }
    };
    private AudioManager mAudioManager;
    private GestureDetector mGestureDetector;
    public static final int RECOGNIZER_CODE = 101;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(this).setBaseListener(mBaseListener);


    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }




    private void startCheck() {
        startActivity(new Intent(this, Checklist_Activity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

