package com.example.guild.mychecklist;

/**
 * Created by Guild on 12/15/2014.
 */
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;


public abstract class BaseActivity extends Activity{

    /** The Unicode character for the hollow circle representing a phrase not yet guessed. */
    private static final char HOLLOW_CIRCLE = '\u25cb';

    /** The Unicode character for the filled circle representing a correctly guessed phrase. */
    private static final char FILLED_CIRCLE = '\u25cf';

    /** A light blue color applied to the circle representing the current phrase. */
    private static final int CURRENT_PHRASE_COLOR = Color.rgb(0x34, 0xa7, 0xff);

    /** A light green color applied briefly to a phrase when it is guessed correctly. */
    private static final int SCORED_PHRASE_COLOR = Color.rgb(0x99, 0xcc, 0x33);

    /** Handler used to post a delayed animation when a phrase is scored. */
    private final Handler mHandler = new Handler();

    private AudioManager mAudioManager;

    private MyDatabase db;



    private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
        @Override
        public boolean onGesture(Gesture gesture) {
            if (areGesturesEnabled()) {
                switch (gesture) {
                    case SWIPE_LEFT:
                        Log.d("myApp", "inside swipe left");
                        tugPhrase();
                        //handleGameGesture(gesture);
                        return true;
                    case TAP:
                    case SWIPE_RIGHT:
                        handleGameGesture(gesture);
                        return true;
                    case SWIPE_DOWN:
                        handleGameGesture(gesture);
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        }
    };



    /** Detects gestures during the game. */
    private GestureDetector mGestureDetector;

    /** Model that stores the state of the game. */
    private Checklist_Model mCheck;
    protected Checklist_Model getCheckModel() {
        return mCheck;
    }
    private static final long SCORED_PHRASE_DELAY_MILLIS = 500;
    /**
     * Value that can be updated to enable/disable gesture handling in the game. For example,
     * gestures are disabled briefly when a phrase is scored so that the user cannot score or
     * pass again until the animation has completed.
     */
    private boolean mGesturesEnabled;

    /** View flipper with two views used to provide the flinging animations between phrases. */
    private ViewFlipper mPhraseFlipper;

    /** TextView containing the dots that represent the scored/unscored phrases in the game. */
    private TextView mCheckState;

    /** Animation used to briefly tug a phrase when the user swipes left. */
    private Animation mTugRightAnimation;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_checklist);
        setGesturesEnabled(true);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(this).setBaseListener(mBaseListener);
        mPhraseFlipper = (ViewFlipper) findViewById(R.id.phrase_flipper);
        mCheckState = (TextView) findViewById(R.id.game_state);
        mTugRightAnimation = AnimationUtils.loadAnimation(this, R.anim.tug_right);

        mCheck = createChecklist_Model();
        updateDisplay();
        //if first item is checked, move to the next one
        if(mCheck.isChecked(0)){
            pass();
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }


    protected abstract void handleGameGesture(Gesture gesture);

    protected abstract Checklist_Model createChecklist_Model();

    private void updateDisplay() {
        getCurrentTextView().setText(mCheck.getCurrentCheck());
        getCurrentTextView().setTextColor(Color.WHITE);
        mCheckState.setText(buildScoreBar());
    }

    private boolean areGesturesEnabled() {
        return mGesturesEnabled;
    }



    private CharSequence buildScoreBar() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (int i = 0; i < mCheck.getCheckCount(); i++) {
            if (i > 0) {
                builder.append(' ');
            }

            if (i == mCheck.getCurrentCheckIndex()) {
                builder.append(HOLLOW_CIRCLE);
                builder.setSpan(new ForegroundColorSpan(CURRENT_PHRASE_COLOR),
                        builder.length() - 1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (mCheck.isChecked(i)) {
                builder.append(FILLED_CIRCLE);
            } else {
                builder.append(HOLLOW_CIRCLE);
            }
        }
        return builder;
    }

    protected void playSoundEffect(int effectType) {
        mAudioManager.playSoundEffect(effectType);
    }

    protected void pass(){

        mCheck.pass();
        mPhraseFlipper.showNext();
        updateDisplay();

    }

    //Need to test this func
    protected void checked(){
        //playSoundEffect(Sounds.SUCCESS);
        setGesturesEnabled(false);

        playSoundEffect(Sounds.SUCCESS);

        db = new MyDatabase(this);
        Log.d("myApp","before update id : "+mCheck.getIndexCurrentCheck());
        db.updateStatus(mCheck,mCheck.getIndexCurrentCheck()+1);
        db.close();

        mCheck.markChecked();
        getCurrentTextView().setTextColor(SCORED_PHRASE_COLOR);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mCheck.areAllChecked()) {
                    mPhraseFlipper.showNext();
                    updateDisplay();

                    // Re-enable gesture handling after the delay has passed.
                    setGesturesEnabled(true);

                }
            }
        }, SCORED_PHRASE_DELAY_MILLIS);
        //---

    }

    protected void previous(){
        mCheck.previous_b();
        mPhraseFlipper.showPrevious();
        updateDisplay();
    }

    private void setGesturesEnabled(boolean enabled){mGesturesEnabled = enabled;}

    /** Returns the {@code TextView} inside the flipper that is currently on-screen. */
    private TextView getCurrentTextView() {
        return (TextView) mPhraseFlipper.getCurrentView();
    }

    /** Plays a tugging animation that provides feedback when the user tries to swipe backward. */
    private void tugPhrase() {
        mPhraseFlipper.startAnimation(mTugRightAnimation);
    }



}
