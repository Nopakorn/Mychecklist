package com.example.guild.mychecklist;


import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import android.view.View.OnClickListener;
import android.speech.RecognizerIntent;


public class MainActivity extends Activity{


    private final Handler mHandler = new Handler();

    private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
        @Override
        public boolean onGesture(Gesture gesture) {
            if (gesture == Gesture.TAP) {
                mAudioManager.playSoundEffect(Sounds.TAP);
                Log.d("myApp", "on gesture detected");
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
        Log.d("myApp", "on create");
        setContentView(R.layout.activity_start);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(this).setBaseListener(mBaseListener);


    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }




    private void startCheck() {
        Log.d("myApp", "start Checklist activity");
        startActivity(new Intent(this, Checklist_Activity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

