package com.example.guild.mychecklist;

/**
 * Created by Guild on 1/19/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbConnected {
    String[] model = new String[]{};
    String[] model_status = new String[]{};


    private int number_of_model;

    private String url = "jdbc:jtds:sqlserver://192.168.1.106:1433;instanceName=ICE-SSDPC\\SQLEXPRESS;DatabaseName=checklist;integratedSecurity=true;user=sa;password=icessdpc";

    private String url_g = "jdbc:jtds:sqlserver://192.168.1.103:1433;instanceName=GUILD-PC\\SQLEXPRESS;DatabaseName=checklist;integratedSecurity=true;user=sa;password=g1234";

    Connection connection = null;
    Statement statement = null;


    public Connection conn;

    //constructor
    public DbConnected(){
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
        } catch (Exception ex) {}
    }

    public void query_model(){

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            connection = DriverManager.getConnection(url_g);

            statement = connection.createStatement();
            String sql = "select *, count(*) over (partition by 1) rows from dbo.ma_part";
            ResultSet rs = statement.executeQuery(sql);
            String [] part = new String[]{};
            String [] part_status = new String[]{};
            boolean isFirstRow = true;

            while (rs.next()) {
                if (isFirstRow) {
                    part = (String[]) resizeArray(part, rs.getInt("rows")+1);
                    part_status = (String[]) resizeArray(part, rs.getInt("rows")+1);
                }

                part[rs.getInt("id")] = rs.getString("name");
                part_status[rs.getInt("id")] = ""+rs.getInt("status");

                isFirstRow = false;
                Log.d("model:",part[rs.getInt("id")]+"id: "+rs.getInt("id")+"status: "+rs.getInt("status"));
            }

            model = new String[part.length-1];
            model_status = new String[part_status.length-1];
            for(int i = 1;i<=part.length-1;i++){
                model[i-1] = part[i];
                model_status[i-1] = part_status[i];
            }


        } catch (SQLException ex) {
            Log.e("SQLException:", ex.toString());
        }
        finally
        {
            if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
            if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
        }
    }


    public String[] getModel(){
        for(int i =0;i<model.length;i++){
            Log.d("model", "check item: "+model[i]);
        }
        return model;
    }
    public String[] getStatus_model(){
        return model_status;
    }




    public void update_status_dbc(int id){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            connection = DriverManager.getConnection(url_g);

            PreparedStatement updateState = null;
            String sql = "update dbo.ma_part set Status = 1 where Id = "+id;
            updateState = connection.prepareStatement(sql);
            updateState.executeUpdate();


            Statement sta = connection.createStatement();
            sql = "insert into dbo.da_check (CheckInId, partId, result, transDate)values ("+id+"," + id + ",1, CURRENT_TIMESTAMP)";
            int rowAffected = sta.executeUpdate(sql);

            updateState.close();
            sta.close();


        } catch (SQLException ex) {
            Log.e("SQLException:", ex.toString());
        }
        finally
        {
            if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
        }


    }
    public void clear_status_dbc(int mSize){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            connection = DriverManager.getConnection(url_g);

            PreparedStatement updateState = null;
            String sql = "update dbo.ma_part set Status = 0";
            updateState = connection.prepareStatement(sql);
            updateState.executeUpdate();

            sql = "insert into dbo.da_check (CheckInId, partId, result, transDate)values (?,?,?, CURRENT_TIMESTAMP)";
            PreparedStatement sta = connection.prepareStatement(sql);
            for(int i =1;i<=mSize;i++){

                sta.setInt(1,i);
                sta.setInt(2,i);
                sta.setInt(3,0);
                sta.addBatch();
            }
            sta.executeBatch();

            updateState.close();
            sta.close();

        }catch (SQLException ex){
            Log.e("SQLException:", ex.toString());
        }
        finally
        {
            if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
        }
    }


    //resize array
    private static Object resizeArray (Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();

        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);

        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0)
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);

        return newArray;
    }


    public boolean isNetworkOnline(Context c) {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }
}
