package edu.drexel.ea464.doomies;

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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.drexel.ea464.doomies.database.DatabaseAccess;

public class CreateRoomActivity extends AppCompatActivity {

    final String ROOMNAME_ALREADY_EXISTS="Please specify a different room name. Room with this name already exists";
    final String ERROR_OCCURRED="Some unexpected error has occured! Please try again later";
    final String ROOM_CREATION_SUCCESS="Room created successfully!";
    TextView textView;
    String roomName;
    String roomDescription;
    String result;

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

        EditText editTextRoomName = (EditText) findViewById(R.id.roomName);
        roomName = editTextRoomName.getText().toString();

        EditText editTextRoomDescription = (EditText) findViewById(R.id.roomDescription);
        roomDescription = editTextRoomDescription.getText().toString();

        textView = (TextView) findViewById(R.id.ErrorCreateRoom);

        Log.d("Room name in edit text=", roomName);

       if (roomName == null || roomName.length() <= 0) {

            textView.setText("Please provide a room name to continue.");
            textView.setVisibility(View.VISIBLE);

        }else{
            RoomCreation roomCreation=new RoomCreation();
           roomCreation.execute(roomName,roomDescription);
        }

    }

    private class RoomCreation extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String[] params) {

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CreateRoomActivity.this);
            String loggedInUser=((MyApplicationClass) getApplicationContext()).user.getEmail();

            if(databaseAccess.checkIfRoomAlreadyExists(roomName,loggedInUser)) {
                result=ROOMNAME_ALREADY_EXISTS; //Remain in the same activity and wait for user to enter a new name to proceed
            }else{
                if(databaseAccess.addNewRoom(roomName,roomDescription,loggedInUser)){ //Create the room and go to the room
                    ((MyApplicationClass) getApplicationContext()).selectedRoomName = roomName;
                    ((MyApplicationClass) getApplicationContext()).selectedRoomDescription = roomDescription;
                    result=ROOM_CREATION_SUCCESS;
                }else{
                    result=ERROR_OCCURRED; //Go back to WelcomePageActivity
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Intent intent;

            if(result!=null && result.equals(ROOMNAME_ALREADY_EXISTS)){
                textView.setText("Room with name " + roomName + " is already present. Please specify a different room name.");
                textView.setVisibility(View.VISIBLE);
            }else if(result!=null && result.equals(ERROR_OCCURRED)){
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateRoomActivity.this);
                AlertDialog alertDialog=builder.create();
                alertDialog.setMessage(result);
                alertDialog.show();
                intent = new Intent(CreateRoomActivity.this, WelcomePageActivity.class);
                startActivity(intent);
            }else if(result!=null && result.equals(ROOM_CREATION_SUCCESS)){
                intent=new Intent(CreateRoomActivity.this, ViewRoomActivity.class);
                startActivity(intent);
            }
        }
    }
}
