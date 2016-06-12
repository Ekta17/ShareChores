package edu.drexel.ea464.doomies;

import android.content.DialogInterface;
import android.content.Intent;
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

public class RoomDutiesActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView listView;

    ArrayList<String> dutiesListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duties);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dutiesListItems = ((MyApplicationClass) getApplicationContext()).roomDutiesList;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dutiesListItems);
        listView = (ListView) findViewById(R.id.RoomDutiesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomDutiesActivity.this);
                builder.setTitle("Duty Linkage");
                String dutySelected = parent.getAdapter().getItem(position).toString();

                HashMap<String, ArrayList<String>> dutyRoommateMap = ((MyApplicationClass) getApplicationContext()).roommate_duty_hashMap;
                ArrayList<String> roommateLinkedTo = new ArrayList<String>();

                for (Map.Entry<String, ArrayList<String>> entry : dutyRoommateMap.entrySet()) {
                    if (entry.getValue().contains(dutySelected)) {
                        roommateLinkedTo.add(entry.getKey().toString());
                    }
                }

                HashMap<String,String> dutyAmendMap=((MyApplicationClass)getApplicationContext()).duty_amend_hashMap;
                String amendLinkTo=dutyAmendMap.get(dutySelected);

                if(amendLinkTo!=null && roommateLinkedTo.size()>0)
                    builder.setMessage("Duty: "+dutySelected + " is linked to Roommates: " + roommateLinkedTo.toString()+" and Amends: "+amendLinkTo);
                else if(amendLinkTo==null && roommateLinkedTo.size()>0)
                    builder.setMessage("Duty: "+dutySelected + " is linked to Roommates: " + roommateLinkedTo.toString());
                else if(amendLinkTo!=null && roommateLinkedTo.size()<=0)
                    builder.setMessage("Duty: "+dutySelected + " is linked to Amends: " + amendLinkTo);
                else if(amendLinkTo==null && roommateLinkedTo.size()<=0)
                    builder.setMessage("Duty is not currently linked to any Amend and Roommates");

                /*Log.d("roommates linked=", roommateLinkedTo.toString());
                Log.d("amend linked=",amendLinkTo);*/

                builder.setPositiveButton("Ok", null);
                builder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomDutiesActivity.this);
                builder.setTitle("Delete?");
                builder.setMessage("Are sure you want to delete " + parent.getAdapter().getItem(position).toString());
                final int positionToRemove = position;
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = parent.getAdapter().getItem(positionToRemove).toString();
                        if (((MyApplicationClass) getApplicationContext()).roommatesList.size() > 0) {
                            String displaySuccessAlert = "An email has been sent to your Roommates. Once everyone approves your request to delete the Duty " + name + ", " + name + " will be deleted from your Room Duties List";
                            Toast toast = Toast.makeText(RoomDutiesActivity.this, displaySuccessAlert, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                        dutiesListItems.remove(positionToRemove);
                        //Log.i("dutiesListItems size = ",Integer.toString(dutiesListItems.size()));
                        if (dutiesListItems.size() <= 0 || dutiesListItems == null) {
                            TextView textView = (TextView) RoomDutiesActivity.this.findViewById(R.id.ErrorViewDuties);
                            textView.setText("Room is does not contain any duty. Please add a Duty");
                            textView.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                return false;
            }
        });

        if (dutiesListItems.size() <= 0 || dutiesListItems == null) {
            TextView textView = (TextView) findViewById(R.id.ErrorViewDuties);
            textView.setText("Room is does not contain any duty. Please add a Duty");
        }

    }

    public void addDuty(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.add_duty_layout, null);

        //Link Duty with Amends
        ArrayList<String> roomAmends = ((MyApplicationClass) getApplicationContext()).roomAmendsList;

        ListView listViewLinkDutyWithAmend = (ListView) layoutView.findViewById(R.id.LinkDutyWithAmend);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, roomAmends);
        if (roomAmends.size() <= 0 || roomAmends == null) {
            TextView textViewLinkDutyWithAmend = (TextView) layoutView.findViewById(R.id.TextViewSelectAmend);
            textViewLinkDutyWithAmend.setVisibility(View.GONE);
        }


        listViewLinkDutyWithAmend.setAdapter(adapter1);

        listViewLinkDutyWithAmend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAmend = parent.getAdapter().getItem(position).toString();
                HashMap<String, String> linkDutyAndAmend = ((MyApplicationClass) getApplicationContext()).duty_amend_hashMap;
                EditText editTextDutyName = (EditText) layoutView.findViewById(R.id.editText_add_duty_name);
                String dutyName = editTextDutyName.getText().toString();
                linkDutyAndAmend.put(dutyName, selectedAmend);

                Log.d("amend linked to duty = " + dutyName, linkDutyAndAmend.get(dutyName));
            }
        });

        //Link duty with Roommate
        ArrayList<String> roommates = ((MyApplicationClass) getApplicationContext()).roommatesList;
        if (roommates.size() <= 0 || roommates == null)
            roommates.add("Myself");
        ListView listViewLinkDutyWithRoommate = (ListView) layoutView.findViewById(R.id.LinkDutyWithRoommate);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, roommates);

        listViewLinkDutyWithRoommate.setAdapter(adapter2);

        listViewLinkDutyWithRoommate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRoommate = parent.getAdapter().getItem(position).toString();
                HashMap<String, ArrayList<String>> linkDutyAndRoommate = ((MyApplicationClass) getApplicationContext()).roommate_duty_hashMap;
                EditText editTextDutyName = (EditText) layoutView.findViewById(R.id.editText_add_duty_name);
                String dutyName = editTextDutyName.getText().toString();
                ArrayList<String> dutyArray;
                if (!linkDutyAndRoommate.containsKey(selectedRoommate)) {
                    dutyArray = new ArrayList<String>();
                } else {
                    dutyArray = linkDutyAndRoommate.get(selectedRoommate);
                }

                dutyArray.add(dutyName);
                linkDutyAndRoommate.put(selectedRoommate, dutyArray);

                Log.d("Roommate linked = " + selectedRoommate, "to duties " + linkDutyAndRoommate.get(selectedRoommate).toString());
            }
        });

        builder.setView(layoutView)
                .setPositiveButton(R.string.button_add_duty_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_duty_name);
                        String name = editTextName.getText().toString();

                        TextView textViewErorr=(TextView) layoutView.findViewById(R.id.ErrorAddDuty);

                        if(name.equals("")||name.equals(null)||name.length()<=0){
                            textViewErorr.setText("Please add the name of the Duty.");
                            textViewErorr.setVisibility(View.VISIBLE);
                        }else{
                            textViewErorr.setVisibility(View.GONE);

                            if (((MyApplicationClass) getApplicationContext()).roommatesList.size() > 0) {
                                String displaySuccessAlert = "An email has been sent to your Roommates. Once everyone approves your request to add the Duty " + name + ", " + name + " will be added to your Room Duties List";
                                Toast toast = Toast.makeText(RoomDutiesActivity.this, displaySuccessAlert, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                            ((MyApplicationClass) getApplicationContext()).roomDutiesList.add(name);

                            ListView dutiesListView = (ListView) RoomDutiesActivity.this.findViewById(R.id.RoomDutiesList);
                            adapter = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, dutiesListItems);
                            dutiesListView.setAdapter(adapter);

                            TextView textView = (TextView) RoomDutiesActivity.this.findViewById(R.id.ErrorViewDuties);
                            textView.setVisibility(View.GONE);
                        }*/
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

                TextView textViewErorr=(TextView) layoutView.findViewById(R.id.ErrorAddDuty);
                ArrayList<String> roomDutiesList=((MyApplicationClass)getApplicationContext()).roomDutiesList;

                if(name.equals("")||name.equals(null)||name.length()<=0){
                    textViewErorr.setText("Please add the name of the Duty.");
                    textViewErorr.setVisibility(View.VISIBLE);
                }else if(roomDutiesList.contains(name)){
                    textViewErorr.setText("Duty with this name already present");
                    textViewErorr.setVisibility(View.VISIBLE);
                }else{
                    textViewErorr.setVisibility(View.GONE);

                    if (((MyApplicationClass) getApplicationContext()).roommatesList.size() > 0) {
                        String displaySuccessAlert = "An email has been sent to your Roommates. Once everyone approves your request to add the Duty " + name + ", " + name + " will be added to your Room Duties List";
                        Toast toast = Toast.makeText(RoomDutiesActivity.this, displaySuccessAlert, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    ((MyApplicationClass) getApplicationContext()).roomDutiesList.add(name);

                    ListView dutiesListView = (ListView) RoomDutiesActivity.this.findViewById(R.id.RoomDutiesList);
                    adapter = new ArrayAdapter<String>(RoomDutiesActivity.this, android.R.layout.simple_list_item_1, dutiesListItems);
                    dutiesListView.setAdapter(adapter);

                    TextView textView = (TextView) RoomDutiesActivity.this.findViewById(R.id.ErrorViewDuties);
                    textView.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }
        });
    }

    /*public void goBackHomeFromDuty(View view){
        Intent intent=new Intent(this,WelcomePageActivity.class);
        startActivity(intent);
    }*/
}
