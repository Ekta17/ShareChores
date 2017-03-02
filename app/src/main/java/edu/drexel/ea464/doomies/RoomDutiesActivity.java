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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.drexel.ea464.doomies.database.DatabaseAccess;

public class RoomDutiesActivity extends AppCompatActivity {

    private String userLoggedIn;
    private String selectedRoom;
    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> dutiesListItems;
    Boolean result;
    ArrayList<DutyBean> roomDutiesDetails;
    Boolean amendsExistInRoom;

    String[] outerSelectedAmend;
    String[] outerSelectedRoommate;
    ArrayList<String> allAmendsInRoom;
    ArrayList<String> roommates;
    AlertDialog dialog;
    TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duties);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.selectedRoom=((MyApplicationClass)getApplicationContext()).selectedRoomName;
        DutiesOfRoom dutiesOfRoom=new DutiesOfRoom();
        dutiesOfRoom.execute();
    }

    public void addDuty(View view) {

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.add_duty_layout, null);

        EditText editTextDutyName = (EditText) layoutView.findViewById(R.id.editText_add_duty_name);
        String dutyName = editTextDutyName.getText().toString();

        this.selectedRoom=((MyApplicationClass)getApplicationContext()).selectedRoomName;
        //Link Duty with Amends
        final DatabaseAccess databaseAccess=DatabaseAccess.getInstance(this);
        ArrayList<String> roommatesInRoom=databaseAccess.getRoommates(this.selectedRoom);
        ArrayList<AmendBean> amendsInRoom=databaseAccess.getRoomAmends(this.selectedRoom);
        final String[] outerSelectedAmend = new String[1];
        final String[] outerSelectedRoommate=new String[1];

        if(amendsInRoom!=null) {
            ArrayList<String> allAmendsInRoom = new ArrayList<>();

            for (AmendBean amend : amendsInRoom) {
                allAmendsInRoom.add(amend.getAmendName());
            }

         ListView listViewLinkDutyWithAmend = (ListView) layoutView.findViewById(R.id.LinkDutyWithAmend);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, allAmendsInRoom);
            listViewLinkDutyWithAmend.setAdapter(adapter1);

            listViewLinkDutyWithAmend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String innerSelectedAmend = parent.getAdapter().getItem(position).toString();
                    outerSelectedAmend[0]=innerSelectedAmend;
                }
            });

        }else if (amendsInRoom==null) {
            TextView textViewLinkDutyWithAmend = (TextView) layoutView.findViewById(R.id.TextViewSelectAmend);
            textViewLinkDutyWithAmend.setVisibility(View.GONE);
        }

        //Link duty with Roommate

       ArrayList<String> roommates;

        if (roommatesInRoom.size() <= 0 || roommatesInRoom==null) {
            roommates = new ArrayList<>();
            roommates.add(((MyApplicationClass) getApplicationContext()).user.getName());//logged in user
        }else{
            roommates=roommatesInRoom;
        }

        ListView listViewLinkDutyWithRoommate = (ListView) layoutView.findViewById(R.id.LinkDutyWithRoommate);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, roommates);
        listViewLinkDutyWithRoommate.setAdapter(adapter2);

        listViewLinkDutyWithRoommate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String innerSelectedRoommate = parent.getAdapter().getItem(position).toString();
                outerSelectedRoommate[0]=innerSelectedRoommate;

            }
        });

        builder.setView(layoutView)
                .setPositiveButton(R.string.button_add_duty_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(R.string.button_add_duty_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        final AlertDialog dialog=builder.create();
        dialog.show();
        //builder.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_duty_name);
                String name = editTextName.getText().toString();

                EditText editTextDescription = (EditText) layoutView.findViewById(R.id.editText_add_duty_description);
                String description = editTextDescription.getText().toString();


                TextView textViewErorr = (TextView) layoutView.findViewById(R.id.ErrorAddDuty);
                ArrayList<String> roomDutiesList = ((MyApplicationClass) getApplicationContext()).roomDutiesList;

                if (name.equals("") || name==null || name.length() <= 0) {
                    textViewErorr.setText("Please add the name of the Duty.");
                    textViewErorr.setVisibility(View.VISIBLE);
                } else if (roomDutiesList.contains(name)) {
                    textViewErorr.setText("Duty with this name already present");
                    textViewErorr.setVisibility(View.VISIBLE);
                } else {
                    textViewErorr.setVisibility(View.GONE);

                    *//*if (outerSelectedRoommate.length > 0) {
                        String displaySuccessAlert = "An email has been sent to your Roommates. Once everyone approves your request to add the Duty " + name + ", " + name + " will be added to your Room Duties List";
                        Toast toast = Toast.makeText(RoomDutiesActivity.this, displaySuccessAlert, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }*//*

                    boolean addNewDuty = databaseAccess.addNewDutyToRoom(selectedRoom, name, description);
                    if (addNewDuty) {

                        if(outerSelectedAmend[0]!=null && outerSelectedAmend[0].length()>0)
                            databaseAccess.linkDutyWithAmend(selectedRoom, outerSelectedAmend[0], name);

                        if(outerSelectedRoommate[0]!=null && outerSelectedRoommate[0].length()>0)
                            databaseAccess.linkDutyWithRoommate(selectedRoom, name, outerSelectedRoommate[0]);

                        ArrayList<DutyBean> roomDuties = databaseAccess.getRoomDuties(selectedRoom);
                        ArrayList<String> dutiesListItems = null;

                        if (roomDuties!=null) {
                            dutiesListItems = new ArrayList<>();

                            for (DutyBean roomDuty : roomDuties) {
                                dutiesListItems.add(roomDuty.getDutyName());
                            }

                            ListView dutiesListView = (ListView) RoomDutiesActivity.this.findViewById(R.id.RoomDutiesList);
                            adapter = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, dutiesListItems);
                            dutiesListView.setAdapter(adapter);

                            TextView textView = (TextView) RoomDutiesActivity.this.findViewById(R.id.ErrorViewDuties);
                            textView.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                    }else {
                        textViewErorr.setText("Some unexpected error has occured");
                        textViewErorr.setVisibility(View.VISIBLE);
                    }
                }
            }
        });*/
        AmendsAndRoommatesToDialog amendsAndRoommatesToDialog=new AmendsAndRoommatesToDialog();
        amendsAndRoommatesToDialog.execute();
    }

    private class DutiesOfRoom extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(RoomDutiesActivity.this);
            roomDutiesDetails=databaseAccess.getRoomDuties(selectedRoom);

            if(roomDutiesDetails!=null) {
                dutiesListItems = new ArrayList<>();

                for (DutyBean roomDuty : roomDutiesDetails) {
                    dutiesListItems.add(roomDuty.getDutyName());
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
                adapter = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, dutiesListItems);
                listView = (ListView) findViewById(R.id.RoomDutiesList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RoomDutiesActivity.this);
                        builder.setTitle("Duty Description");
                        String dutySelected = parent.getAdapter().getItem(position).toString();

                        String dutyDescription=null;
                        for(DutyBean roomDuty : roomDutiesDetails){
                            if(roomDuty.getDutyName().equals(dutySelected)){
                                dutyDescription=roomDuty.getDutyDescription();
                            }
                        }

                        if(dutyDescription!=null && dutyDescription.length()>0){
                            builder.setMessage(dutyDescription);
                        }else{
                            builder.setMessage("Duty is not described properly.");
                        }

                        builder.setPositiveButton("Ok", null);
                        builder.show();
                    }
                });

            }else {
                TextView textView = (TextView) findViewById(R.id.ErrorViewDuties);
                textView.setText("Room is does not contain any duty. Please add a Duty");
            }
        }
    }

    private class AmendsAndRoommatesToDialog extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(RoomDutiesActivity.this);

            ArrayList<String> roommatesInRoom=databaseAccess.getRoommates(selectedRoom);
            ArrayList<AmendBean> amendsInRoom=databaseAccess.getRoomAmends(selectedRoom);
            outerSelectedAmend = new String[1];
            outerSelectedRoommate=new String[1];

            if(amendsInRoom!=null) {
                allAmendsInRoom = new ArrayList<>();

                for (AmendBean amend : amendsInRoom) {
                    allAmendsInRoom.add(amend.getAmendName());
                }
                amendsExistInRoom=true;
            }else{
                amendsExistInRoom=false;
            }

            if (roommatesInRoom.size() <= 0 || roommatesInRoom==null) {
                roommates = new ArrayList<>();
                roommates.add(((MyApplicationClass) getApplicationContext()).user.getName());//logged in user
            }else{
                roommates=roommatesInRoom;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            AlertDialog.Builder builder = new AlertDialog.Builder(RoomDutiesActivity.this);
            LayoutInflater inflater = RoomDutiesActivity.this.getLayoutInflater();
            final View layoutView = inflater.inflate(R.layout.add_duty_layout, null);

            if(amendsExistInRoom){ //Amends are present in Room
                ListView listViewLinkDutyWithAmend = (ListView) layoutView.findViewById(R.id.LinkDutyWithAmend);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, allAmendsInRoom);
                listViewLinkDutyWithAmend.setAdapter(adapter1);

                listViewLinkDutyWithAmend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String innerSelectedAmend = parent.getAdapter().getItem(position).toString();
                        outerSelectedAmend[0]=innerSelectedAmend;
                    }
                });

            }else if (!amendsExistInRoom){
                TextView textViewLinkDutyWithAmend = (TextView) layoutView.findViewById(R.id.TextViewSelectAmend);
                textViewLinkDutyWithAmend.setVisibility(View.GONE);
            }

            ListView listViewLinkDutyWithRoommate = (ListView) layoutView.findViewById(R.id.LinkDutyWithRoommate);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, roommates);
            listViewLinkDutyWithRoommate.setAdapter(adapter2);

            listViewLinkDutyWithRoommate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String innerSelectedRoommate = parent.getAdapter().getItem(position).toString();
                    outerSelectedRoommate[0]=innerSelectedRoommate;

                }
            });

            builder.setView(layoutView)
                    .setPositiveButton(R.string.button_add_duty_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(R.string.button_add_duty_cancel, new DialogInterface.OnClickListener() {

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
                    EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_duty_name);
                    String name = editTextName.getText().toString();

                    EditText editTextDescription = (EditText) layoutView.findViewById(R.id.editText_add_duty_description);
                    String description = editTextDescription.getText().toString();


                    textViewError = (TextView) layoutView.findViewById(R.id.ErrorAddDuty);
                    ArrayList<String> roomDutiesList = ((MyApplicationClass) getApplicationContext()).roomDutiesList;

                    if (name.equals("") || name==null || name.length() <= 0) {
                        textViewError.setText("Please add the name of the Duty.");
                        textViewError.setVisibility(View.VISIBLE);
                    } else if (roomDutiesList.contains(name)) {
                        textViewError.setText("Duty with this name already present");
                        textViewError.setVisibility(View.VISIBLE);
                    } else {
                        textViewError.setVisibility(View.GONE);
                        AddDutiesToRoom addDutiesToRoom=new AddDutiesToRoom();
                        addDutiesToRoom.execute(name,description);
                    }
                }
            });
        }//onPostExecute end
    }

    private class AddDutiesToRoom extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            String name = params[0];
            String description = params[1];
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(RoomDutiesActivity.this);

            if (databaseAccess.addNewDutyToRoom(selectedRoom, name, description)) {

                if (outerSelectedAmend[0] != null && outerSelectedAmend[0].length() > 0)
                    databaseAccess.linkDutyWithAmend(selectedRoom, outerSelectedAmend[0], name);

                if (outerSelectedRoommate[0] != null && outerSelectedRoommate[0].length() > 0)
                    databaseAccess.linkDutyWithRoommate(selectedRoom, name, outerSelectedRoommate[0]);

                ArrayList<DutyBean> roomDuties = databaseAccess.getRoomDuties(selectedRoom);
                dutiesListItems = null;

                if (roomDuties != null) {
                    dutiesListItems = new ArrayList<>();

                    for (DutyBean roomDuty : roomDuties) {
                        dutiesListItems.add(roomDuty.getDutyName());
                    }
                }
                result = true;
            }else {
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);

            if(result){

                ListView dutiesListView = (ListView) RoomDutiesActivity.this.findViewById(R.id.RoomDutiesList);
                adapter = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, dutiesListItems);
                dutiesListView.setAdapter(adapter);

                TextView textView = (TextView) RoomDutiesActivity.this.findViewById(R.id.ErrorViewDuties);
                textView.setVisibility(View.GONE);
                dialog.dismiss();

            }else if(!result){
                textViewError.setText("Some unexpected error has occured");
                textViewError.setVisibility(View.VISIBLE);
            }
        }
    }

}
