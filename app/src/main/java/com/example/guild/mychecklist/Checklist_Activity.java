package com.example.guild.mychecklist;

/**
 * Created by Guild on 12/12/2014.
 */
import com.google.android.glass.touchpad.Gesture;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.database.sqlite.*;

public class Checklist_Activity extends BaseActivity{
    /*create class BaseActivity to provide interface*/
    private final Handler mHandler = new Handler();

    private static final int NUMBER_OF_PHRASES = 4;
    private Cursor check_cursor;
    private MyDatabase db;
    private int count = 0;
    //boolean[] statusItem = new boolean[NUMBER_OF_PHRASES];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myApp", "on create check list activity");

    }

    //adding model into database here ! just guessing
    @Override
    protected Checklist_Model createChecklist_Model() {
        List<String> allPhrases = Arrays.asList(getResources().getStringArray(
                R.array.checklist));
        Log.d("testDB","adding item");

        boolean[] statusItem = new boolean[NUMBER_OF_PHRASES];
        db = new MyDatabase(this);
        Checklist_Model checkItem = new Checklist_Model(allPhrases.subList(0, NUMBER_OF_PHRASES));


        if( db.checkTable() == 0 ){
            Log.d("testDB","First insert item");
            db.addItem(checkItem.getCheck(0),checkItem.isChecked(0));
            db.addItem(checkItem.getCheck(1),checkItem.isChecked(1));
            db.addItem(checkItem.getCheck(2),checkItem.isChecked(2));
            db.addItem(checkItem.getCheck(3),checkItem.isChecked(3));

        }else{
            // DB exist
            //update status
            for(int i = 1;i<=NUMBER_OF_PHRASES;i++){
                check_cursor = db.getItem(i);
                if(check_cursor.getInt(2)!=0){
                    statusItem[i-1] = true;
                    count++;
                }else{
                    statusItem[i-1] = false;
                }
            }
            //when all items are checked, then clearing status --> default
            if(count == 4){
                db.clearStatus(checkItem,1);
                db.clearStatus(checkItem,2);
                db.clearStatus(checkItem,3);
                db.clearStatus(checkItem,4);
                statusItem = new boolean[]{false,false,false,false};
            }

            Log.d("myApp","count for clearing status : "+count);
            checkItem.updateStatus(statusItem);

        }
        db.listItem();
        return checkItem;
    }

    @Override
    protected void handleGameGesture(Gesture gesture) {
        switch (gesture) {
            case TAP:
                checked();
                Log.d("myApp","TAP");
                Log.d("myApp",""+getCheckModel().areAllChecked());
                if(getCheckModel().areAllChecked()){
                    Log.d("myApp","in all checked");
                    endCheck();
                }
                break;
            case SWIPE_RIGHT:
                pass();
                break;
            case SWIPE_DOWN:
                endCheck();
        }
    }

    private void endCheck(){

        //add another activity, "done" screen show summary of checked list
        db.listItem();
        Intent intent = new Intent(this, FinishedActivity.class);
        intent.putExtra(FinishedActivity.EXTRA_MODEL, getCheckModel());
        startActivity(intent);

        db.close();
        check_cursor.close();
        finish();
    }

}
