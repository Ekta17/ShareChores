package edu.drexel.ea464.doomies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import edu.drexel.ea464.doomies.database.DatabaseAccess;

public class ViewRoomActivity extends AppCompatActivity {

    private static String selectedRoom;
    private static String userLoggedIn;
    //private static String test_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String roomName=((MyApplicationClass)getApplicationContext()).selectedRoomName;
        String roomDesc=((MyApplicationClass)getApplicationContext()).selectedRoomDescription;

        this.selectedRoom=roomName;
        this.userLoggedIn=((MyApplicationClass)getApplicationContext()).user.getEmail();

        //Checking if threads are created
       /* InsertRow row=new InsertRow();
        row.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, selectedRoom, userLoggedIn );
*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewRoomDescription=(TextView)findViewById(R.id.ViewRoomDescription);
        if(roomDesc!=null && !roomDesc.equals("")){
            textViewRoomDescription.setText(roomDesc);
        }else{
            textViewRoomDescription.setVisibility(View.GONE);
        }

        TextView textViewRoomName=(TextView)findViewById(R.id.ViewRoomName);
        textViewRoomName.setText(roomName);
    }

    public void goBackHome(View view){
        Intent intent=new Intent(this, WelcomePageActivity.class);
        startActivity(intent);
    }

    public void viewMyTasks(View view){

        Intent intent=new Intent(this,MyTasksActivity.class);
        startActivity(intent);
    }

    public void viewRoommates(View view){
        Intent intent=new Intent(this,ViewRoommatesActivity.class);
        startActivity(intent);
    }

    public void goToRoomDuties(View view){
        Intent intent=new Intent(this,RoomDutiesActivity.class);
        startActivity(intent);
    }

    public void goToRoomAmends(View view){
        Intent intent=new Intent(this,RoomAmendsActivity.class);
        startActivity(intent);
    }

    /*public void viewLogs(View view){
        *//*Intent intent=new Intent(this,RoomAmendsActivity.class);
        startActivity(intent);*//*
    }*/

    private class InsertRow extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(ViewRoomActivity.this);
            databaseAccess.insertDeafultRow(params[0].toString(),params[1].toString());
            return null;
        }
    }
}

