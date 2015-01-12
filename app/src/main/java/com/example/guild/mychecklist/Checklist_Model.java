package com.example.guild.mychecklist;

/**
 * Created by Guild on 12/12/2014.
 */
import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class Checklist_Model implements Serializable{
    private  static final long serialVersionUID = 1L;

    private final String[] mCheck;

    /*For checking list if its already checked*/
    private final boolean[] mAleadyChecked;

    private int mCurrentCheck;
    private int mCount;

    public  Checklist_Model(List<String> check){
        mCheck = check.toArray(new String[check.size()]);
        mAleadyChecked = new boolean[check.size()];
        mCount = 0;
        mCurrentCheck = 0;

    }

    //update status from DB
    public void updateStatus(boolean[] flag){
        for(int i=0;i<mAleadyChecked.length;i++) {
            if (flag[i]) {
                mAleadyChecked[i] = flag[i];
                mCount++;
            } else{
                mAleadyChecked[i] = flag[i];
            }
        }
    }

    public int getCheckCount(){return mCheck.length;}

    public int getCurrentCheckIndex(){return mCurrentCheck;}

    public String getCurrentCheck(){return getCheck(mCurrentCheck);}

    public String getCheck(int index){return mCheck[index];}

    public int getCurrentCheck_already(){return mCount;}

    /*Return true if all of the checklist have been checked*/
    public boolean areAllChecked(){return getCurrentCheck_already() == mCheck.length;}
    /*Return true if the check model at the specified index has been checked*/
    public boolean isChecked(int index){    return mAleadyChecked[index];}

    //Return number of item that already checked
    public int getNumberChecked(){return mCount;}

    public int getIndexCurrentCheck(){ return mCurrentCheck;  }


    public boolean pass(){return advance();}

    public boolean markChecked(){
        mAleadyChecked[mCurrentCheck] = true;
        mCount++;
        return advance();
    }


    private boolean advance(){
        if(!areAllChecked()){
            do{
                mCurrentCheck = (mCurrentCheck+1) % mCheck.length;
            }while(mAleadyChecked[mCurrentCheck]);
            return false;

        }
        return true;
    }


    //go back, SWIPE_LEFT
    public boolean previous_b(){return advance_b();}

    private boolean advance_b(){
        if(!areAllChecked()){
            do{
                mCurrentCheck = (mCurrentCheck-1) % mCheck.length;
                //mCurrentCheck = mCurrentCheck-1;
            }while(mAleadyChecked[mCurrentCheck]);
            return false;

        }
        return true;
    }

}
