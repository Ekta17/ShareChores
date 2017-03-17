package edu.drexel.ea464.doomies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import edu.drexel.ea464.doomies.database.DatabaseAccess;

public class WelcomePageActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    String[] roomNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void createNewRoom(View view) {
        Intent intent = new Intent(this, CreateRoomActivity.class);
        startActivity(intent);
    }

    public void viewRoom(View view) {

        LoadRooms loadRooms=new LoadRooms();
        loadRooms.execute();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, EntryPageActivity.class);
        startActivity(intent);
    }

    private class LoadRooms extends AsyncTask<Void,Void, String[]>{

        @Override
        protected String[] doInBackground(Void... params) {
            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(WelcomePageActivity.this);
            String loggedInUser=((MyApplicationClass)getApplicationContext()).user.getEmail();
            ArrayList<RoomBean> roomsLinkedToUser=databaseAccess.getLinkedRooms(loggedInUser);

            if(roomsLinkedToUser!=null && roomsLinkedToUser.size()>0) {
                ArrayList<String> allLinkedRooms = new ArrayList<>();

                for (RoomBean roomBean : roomsLinkedToUser) {
                    allLinkedRooms.add(roomBean.getName());
                }
                roomNames = allLinkedRooms.toArray(new String[allLinkedRooms.size()]);
            }

            return roomNames;
        }

        @Override
        protected void onPostExecute(String results[]){

            builder = new AlertDialog.Builder(WelcomePageActivity.this);
            builder.setTitle("Select Room");

            if(results!=null && results.length>0){

                builder.setItems(roomNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String roomName = roomNames[which];
                        Intent intent = new Intent(WelcomePageActivity.this, ViewRoomActivity.class);
                        EditText editTextRoomName = (EditText) findViewById(R.id.roomName);

                        ((MyApplicationClass) getApplicationContext()).selectedRoomName = roomName;

                        startActivity(intent);
                    }
                });

            }else{
                builder.setMessage("No Rooms created yet, please create a room first.");
                builder.setPositiveButton("OK", null);
            }

            builder.show();
        }
    }

}
