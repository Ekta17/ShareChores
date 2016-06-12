package edu.drexel.ea464.doomies;

import android.content.Intent;
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

public class ViewRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String roomName=((MyApplicationClass)getApplicationContext()).roomName;
        String roomDesc=((MyApplicationClass)getApplicationContext()).roomAndRoomDesc.get(roomName);

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

    public void viewLogs(View view){
        /*Intent intent=new Intent(this,RoomAmendsActivity.class);
        startActivity(intent);*/
    }

}
