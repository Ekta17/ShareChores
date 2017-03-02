package edu.drexel.ea464.doomies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.drexel.ea464.doomies.database.DatabaseAccess;

public class RoomAmendsActivity extends AppCompatActivity {

    String userLoggedIn;
    String selectedRoom;

    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> amendsListItems;
    Boolean result;
    ArrayList<AmendBean> roomAmendsDetails;

    Boolean dutiesExistInRoom;

    ArrayList<String> allDutiesInRoom;
    String[] outerSelectedDuty;
    AlertDialog dialog;
    TextView textViewErorr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_amends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.selectedRoom = ((MyApplicationClass) getApplicationContext()).selectedRoomName;

        AmendsOfRoom amendsOfRoom=new AmendsOfRoom();
        amendsOfRoom.execute();
    }

    public void addAmend(View view) {

        DutiesToDialog dutiesToDialog=new DutiesToDialog();
        dutiesToDialog.execute();
    }

    private class AmendsOfRoom extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(RoomAmendsActivity.this);

            roomAmendsDetails = databaseAccess.getRoomAmends(selectedRoom);

            if (roomAmendsDetails != null) {
                amendsListItems = new ArrayList<>();

                for (AmendBean roomAmend : roomAmendsDetails) {
                    amendsListItems.add(roomAmend.getAmendName());
                }
                result=true;

            } else {
                result=false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);

            if(result){
                adapter = new ArrayAdapter<String>(RoomAmendsActivity.this, android.R.layout.simple_list_item_1, amendsListItems);
                listView = (ListView) findViewById(R.id.RoomAmendsList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RoomAmendsActivity.this);
                        builder.setTitle("Amend Description");
                        String amendSelected = parent.getAdapter().getItem(position).toString();

                        String amendDescription = null;
                        for (AmendBean roomAmend : roomAmendsDetails) {
                            if (roomAmend.getAmendName().equals(amendSelected)) {
                                amendDescription = roomAmend.getAmendDescription();
                            }
                        }

                        if (amendDescription != null && amendDescription.length() > 0) {
                            builder.setMessage(amendDescription);
                        } else {
                            builder.setMessage("Amend is not described properly.");
                        }

                        builder.setPositiveButton("Ok", null);
                        builder.show();
                    }
                });
            }else if (!result){
                TextView textView = (TextView) findViewById(R.id.ErrorViewAmends);
                textView.setText("Room is does not contain any amend. Please add an Amend");
            }
        }
    }

    private class DutiesToDialog extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(RoomAmendsActivity.this);
            ArrayList<DutyBean> dutyInRoom=databaseAccess.getRoomDuties(selectedRoom);
            outerSelectedDuty = new String[1];

            if(dutyInRoom!=null){
                allDutiesInRoom = new ArrayList<>();

                for (DutyBean duty : dutyInRoom) {
                    allDutiesInRoom.add(duty.getDutyName());
                }
                dutiesExistInRoom=true;
            }else
                dutiesExistInRoom=false;

            return dutiesExistInRoom;
        }

            @Override
            protected void onPostExecute(Boolean result){
                super.onPostExecute(result);

                AlertDialog.Builder builder = new AlertDialog.Builder(RoomAmendsActivity.this);
                LayoutInflater inflater = RoomAmendsActivity.this.getLayoutInflater();
                final View layoutView = inflater.inflate(R.layout.add_amend_layout, null);

                if(result){ //Duties are present in Room
                    ListView listViewLinkAmendWithDuty = (ListView) layoutView.findViewById(R.id.LinkAmendWithDuty);

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RoomAmendsActivity.this, android.R.layout.simple_list_item_1, allDutiesInRoom);
                    listViewLinkAmendWithDuty.setAdapter(adapter1);

                    listViewLinkAmendWithDuty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String innerSelectedDuty = parent.getAdapter().getItem(position).toString();
                            outerSelectedDuty[0]=innerSelectedDuty;
                        }
                    });

                }else if (!result){
                    TextView textViewLinkDutyWithAmend = (TextView) layoutView.findViewById(R.id.TextViewSelectDuty);
                    textViewLinkDutyWithAmend.setVisibility(View.GONE);
                }

                builder.setView(layoutView)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                dialog=builder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_amend_name);
                        String name = editTextName.getText().toString();

                        EditText editTextDescription = (EditText) layoutView.findViewById(R.id.editText_add_amend_description);
                        String description = editTextDescription.getText().toString();

                        textViewErorr = (TextView) layoutView.findViewById(R.id.ErrorAddAmend);
                        ArrayList<String> roomAmendsList = ((MyApplicationClass) getApplicationContext()).roomAmendsList;

                        if (name.equals("") || name == null || name.length() <= 0) {
                            textViewErorr.setText("Please add the name of the Amend.");
                            textViewErorr.setVisibility(View.VISIBLE);
                        } else if (roomAmendsList.contains(name)) {
                            textViewErorr.setText("Amend with this name already present");
                            textViewErorr.setVisibility(View.VISIBLE);
                        } else {
                            textViewErorr.setVisibility(View.GONE);
                            AddToAmendsOfRoom addToAmendsOfRoom=new AddToAmendsOfRoom();
                            addToAmendsOfRoom.execute(name,description);
                        }}
                });
            }//onPostExecute end
        }

    private class AddToAmendsOfRoom extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            String name=params[0];
            String description=params[1];
            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(RoomAmendsActivity.this);

            if (databaseAccess.addNewAmendToRoom(selectedRoom, name, description)) {

                if (outerSelectedDuty[0] != null && outerSelectedDuty[0].length() > 0)
                    databaseAccess.linkDutyWithAmend(selectedRoom, name, outerSelectedDuty[0]);

                ArrayList<AmendBean> roomAmends = databaseAccess.getRoomAmends(selectedRoom);

                if (roomAmends != null) {
                    amendsListItems = new ArrayList<>();

                    for (AmendBean roomAmend : roomAmends) {
                        amendsListItems.add(roomAmend.getAmendName());
                    }
                }
                result=true;
            } else {
                result=false;
            }

                return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);

            if(result){

                ListView amendsListView = (ListView) RoomAmendsActivity.this.findViewById(R.id.RoomAmendsList);
                adapter = new ArrayAdapter<String>(RoomAmendsActivity.this, android.R.layout.simple_list_item_1, amendsListItems);
                amendsListView.setAdapter(adapter);

                TextView textView = (TextView) RoomAmendsActivity.this.findViewById(R.id.ErrorViewAmends);
                textView.setVisibility(View.GONE);
                dialog.dismiss();

            }else if(!result){
                textViewErorr.setText("Some unexpected error has occured");
                textViewErorr.setVisibility(View.VISIBLE);
            }
        }
    }

}
