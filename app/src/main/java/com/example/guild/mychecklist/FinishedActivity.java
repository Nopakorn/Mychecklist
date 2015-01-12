package com.example.guild.mychecklist;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.glass.widget.CardScrollView;

/**
 * Created by Guild on 12/19/2014.
 */
public class FinishedActivity extends Activity {

    public static final String EXTRA_MODEL = "model";

    private final Handler mHandler = new Handler();

    private TextView message;
    private TextView numberC;
    private MyDatabase db;

    private int fag = 0;
    //model

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myApp", "On finish activity");
        setContentView(R.layout.activity_finish);
        Checklist_Model model = (Checklist_Model) getIntent().getSerializableExtra(EXTRA_MODEL);
        fag = model.getNumberChecked();

        message = (TextView) findViewById(R.id.finish_message);
        numberC = (TextView) findViewById(R.id.numCheck);

        db = new MyDatabase(this);
        db.listItem();
        db.close();
        updateText();


    }

    private void updateText(){
          Log.d("myApp", "check fag = "+fag);
          String num = Integer.toString(fag);
          numberC.setText(num);
    }
    private void endCheck(){
        finish();
    }

}
