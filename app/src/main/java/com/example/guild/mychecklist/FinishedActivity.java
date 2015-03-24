package com.example.guild.mychecklist;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


/**
 * Created by Guild on 12/19/2014.
 */
public class FinishedActivity extends Activity {

    public static final String EXTRA_MODEL = "model";

    private final Handler mHandler = new Handler();

    private TextView message;
    private TextView numberC;
    private MyDatabase db;

    //the number of checked models
    private int fag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Checklist_Model model = (Checklist_Model) getIntent().getSerializableExtra(EXTRA_MODEL);
        fag = model.getNumberChecked();

        message = (TextView) findViewById(R.id.finish_message);
        numberC = (TextView) findViewById(R.id.numCheck);

        updateText();


    }

    private void updateText(){
          String num = Integer.toString(fag);
          numberC.setText(num);
    }
    private void endCheck(){
        finish();
    }

}
