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



public class Checklist_Activity extends BaseActivity{
    /*create class BaseActivity to provide interface*/
    private final Handler mHandler = new Handler();

    private static  int NUMBER_OF_PHRASES = 4;
    private Cursor check_cursor;
    private MyDatabase db;
    private int count = 0;
    String[] check_model = new String[]{};
    private DbConnected dbc;
    String [] testS = {"a","b","c","d"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myApp", "on create check activity");

    }

    //adding model into database here ! just guessing
    @Override
    protected Checklist_Model createChecklist_Model() {

        db = new MyDatabase(this);
        dbc = new DbConnected();
        boolean net = dbc.isNetworkOnline(this);
        if(net){
            dbc.query_model();
            check_model = new String[dbc.getModel().length];
            check_model = dbc.getModel();
            Log.d("DbConnected", "check_model length  "+check_model.length);
            for(int i =0;i<check_model.length;i++){
                Log.d("DbConnected", "item: "+check_model[i]);
            }


        }else{
            Log.d("DbConnected", "network fail");
        }
        List<String> allPhrases = Arrays.asList(check_model);
        NUMBER_OF_PHRASES = allPhrases.size();
        Checklist_Model checkItem = new Checklist_Model(allPhrases.subList(0, NUMBER_OF_PHRASES));


        boolean[] statusItem = new boolean[NUMBER_OF_PHRASES];
        if( db.checkTable() == 0 ){

            for(int i = 0; i< NUMBER_OF_PHRASES;i++){
                db.addItem(checkItem.getCheck(i),checkItem.isChecked(i));
            }

        }else{
            for(int i = 0; i <NUMBER_OF_PHRASES;i++){
                if(dbc.getStatus_model()[i].equals("1")){
                    statusItem[i] = true;
                    count++;

                }else{
                    statusItem[i] = false;

                }
            }

            if(count == 4){
                dbc.clear_status_dbc(NUMBER_OF_PHRASES);
                statusItem = new boolean[]{false,false,false,false};
            }
            checkItem.updateStatus(statusItem);
        }
        db.listItem();
        return checkItem;
    }

    @Override
    protected void handleGameGesture(Gesture gesture) {
        switch (gesture) {
            case TAP :
                checked();
                if(getCheckModel().areAllChecked()){
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

        db.listItem();
        Intent intent = new Intent(this, FinishedActivity.class);
        intent.putExtra(FinishedActivity.EXTRA_MODEL, getCheckModel());
        startActivity(intent);

        db.close();
        check_cursor.close();
        finish();
    }

}
