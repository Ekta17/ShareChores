package edu.drexel.ea464.doomies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class WelcomePageActivity extends AppCompatActivity {

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
        Intent intent = getIntent();
        ArrayList<String> roomArrayList = ((MyApplicationClass) getApplicationContext()).roomNames;
        final String[] roomNames = roomArrayList.toArray(new String[roomArrayList.size()]);
        //String noRoomVar="No Rooms created yet, please create a room first.";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Room");
        if (roomNames.equals(null) || roomNames.length <= 0) {
            builder.setMessage("No Rooms created yet, please create a room first.");
            builder.setPositiveButton("OK", null);
        } else {
            builder.setItems(roomNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String roomName = roomNames[which];
                    /*String roomDesc=((MyApplicationClass)getApplicationContext()).roomAndRoomDesc.get(roomName);
                    if(roomDesc!=null && !roomDesc.equals(""))

*/
                    Intent intent = new Intent(WelcomePageActivity.this, ViewRoomActivity.class);

                    EditText editTextRoomName = (EditText) findViewById(R.id.roomName);

                    ((MyApplicationClass) getApplicationContext()).roomName = roomName;
                    //((MyApplicationClass) getApplicationContext()).roomDescription = roomDescription;

                    startActivity(intent);
                }
            });
        }
        builder.show();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, EntryPageActivity.class);
        startActivity(intent);
    }

}
