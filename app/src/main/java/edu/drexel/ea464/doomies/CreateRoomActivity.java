package edu.drexel.ea464.doomies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void backToWelcomePage(View view) {
        Intent intent = new Intent(this, WelcomePageActivity.class);
        startActivity(intent);
    }

    public void goToRoom(View view) {

        Intent intent = new Intent(this, ViewRoomActivity.class);

        EditText editTextRoomName = (EditText) findViewById(R.id.roomName);
        String roomName = editTextRoomName.getText().toString();

        EditText editTextRoomDescription = (EditText) findViewById(R.id.roomDescription);
        String roomDescription = editTextRoomDescription.getText().toString();

        HashMap<String, String> roomAndDescMap = ((MyApplicationClass) getApplicationContext()).roomAndRoomDesc;
        TextView textView = (TextView) findViewById(R.id.ErrorCreateRoom);

        Log.d("Room name in edit text=", roomName);

        ArrayList<String> preExistingRooms = ((MyApplicationClass) getApplicationContext()).roomNames;

        if (roomAndDescMap.containsKey(roomName) || preExistingRooms.contains(roomName)) {
            textView.setText("Room with name " + roomName + " is already present. Please specify a different room name.");
            textView.setVisibility(View.VISIBLE);
            //intent = new Intent(this, this.getClass());
            //startActivity(intent);
        } else if (roomName == null || roomName.length() <= 0) {
            textView.setText("Please provide a room name to continue.");
            textView.setVisibility(View.VISIBLE);
            //intent = new Intent(this, this.getClass());
            //startActivity(intent);
        } else {
            //Set Room names in Global Room Name variable
            ((MyApplicationClass) getApplicationContext()).roomNames.add(roomName);
            ((MyApplicationClass) getApplicationContext()).roomName = roomName;
            ((MyApplicationClass) getApplicationContext()).roomDescription = roomDescription;
            if (roomDescription != null && !roomDescription.equals(""))
                ((MyApplicationClass) getApplicationContext()).roomAndRoomDesc.put(roomName, roomDescription);

            startActivity(intent);
        }
    }
}
