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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomAmendsActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView listView;

    ArrayList<String> amendsListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_amends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        amendsListItems = ((MyApplicationClass) getApplicationContext()).roomAmendsList;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, amendsListItems);
        listView = (ListView) findViewById(R.id.RoomAmendsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomAmendsActivity.this);
                builder.setTitle("Amend Linkage");
                String amendSelected = parent.getAdapter().getItem(position).toString();

                HashMap<String,String> amendDutyMap=((MyApplicationClass)getApplicationContext()).duty_amend_hashMap;
                ArrayList<String> dutyLinkTo=new ArrayList<String>();

                for(Map.Entry<String,String>entry:amendDutyMap.entrySet()){
                    if(entry.getValue().contains(amendSelected)){
                        dutyLinkTo.add(entry.getKey());
                    }
                }

                if(dutyLinkTo!=null)
                    builder.setMessage("Amend: "+amendSelected + " is linked to Duties: " + dutyLinkTo.toString());
                else
                    builder.setMessage("Amend is not currently linked to any Duty");

                Log.d("duties linked=", dutyLinkTo.toString());

                builder.setPositiveButton("Ok", null);
                builder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(RoomAmendsActivity.this);
                builder.setTitle("Delete?");
                builder.setMessage("Are sure you want to delete "+parent.getAdapter().getItem(position).toString());
                final int positionToRemove=position;
                builder.setNegativeButton("Cancel",null);
                builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name=parent.getAdapter().getItem(positionToRemove).toString();
                        if(((MyApplicationClass)getApplicationContext()).roommatesList.size()>0) {
                            String displaySuccessAlert = "An email has been sent to your Roommates. Once everyone approves your request to delete the Amend "+name+", "+name+" will be deleted from your Room Amends List";
                            Toast toast = Toast.makeText(RoomAmendsActivity.this, displaySuccessAlert, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                        amendsListItems.remove(positionToRemove);
                        //Log.i("dutiesListItems size = ",Integer.toString(dutiesListItems.size()));
                        if(amendsListItems.size()<=0 || amendsListItems==null){
                            TextView textView = (TextView) RoomAmendsActivity.this.findViewById(R.id.ErrorViewAmends);
                            textView.setText("Room is does not contain any amend. Please add an Amend");
                            textView.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                return false;
            }
        });

        if (amendsListItems.size() <= 0 || amendsListItems == null) {
            TextView textView = (TextView) findViewById(R.id.ErrorViewAmends);
            textView.setText("Room is does not contain any amend. Please add an Amend");
        }
    }

    public void addAmend(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.add_amend_layout, null);

        //Link amend with Duty
        ArrayList<String> roomDuties = ((MyApplicationClass) getApplicationContext()).roomDutiesList;

        ListView listViewLinkAmendWithDuty = (ListView) layoutView.findViewById(R.id.LinkAmendWithDuty);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RoomAmendsActivity.this, android.R.layout.simple_list_item_1, roomDuties);
        if (roomDuties.size() <= 0 || roomDuties == null) {
            TextView textViewLinkAmendWithDuty = (TextView) layoutView.findViewById(R.id.TextViewSelectDuty);
            textViewLinkAmendWithDuty.setVisibility(View.GONE);
        }


        listViewLinkAmendWithDuty.setAdapter(adapter1);

        listViewLinkAmendWithDuty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDuty = parent.getAdapter().getItem(position).toString();
                HashMap<String, String> linkAmendWithDuty = ((MyApplicationClass) getApplicationContext()).duty_amend_hashMap;
                EditText editTextAmendName = (EditText) layoutView.findViewById(R.id.editText_add_amend_name);
                String amendName = editTextAmendName.getText().toString();
                linkAmendWithDuty.put(selectedDuty,amendName);

                Log.d("amend linked to duty = " + selectedDuty, linkAmendWithDuty.get(selectedDuty));
            }
        });

        builder.setView(layoutView)
                .setPositiveButton(R.string.button_add_amend_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_amend_name);
                        String name = editTextName.getText().toString();

                        if(((MyApplicationClass)getApplicationContext()).roommatesList.size()>0) {
                            String displaySuccessAlert = "An email has been sent to your Roommates. Once everyone approves your request to add the Amend "+name+", "+name+" will be added to your Room Amends List";
                            Toast toast = Toast.makeText(RoomAmendsActivity.this, displaySuccessAlert, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                        ((MyApplicationClass) getApplicationContext()).roomAmendsList.add(name);

                        ListView amendsListView = (ListView) RoomAmendsActivity.this.findViewById(R.id.RoomAmendsList);
                        adapter = new ArrayAdapter<String>(RoomAmendsActivity.this, android.R.layout.simple_list_item_1, amendsListItems );
                        amendsListView.setAdapter(adapter);

                        TextView textView = (TextView) RoomAmendsActivity.this.findViewById(R.id.ErrorViewAmends);
                        textView.setVisibility(View.GONE);*/
                    }
                })
                .setNegativeButton(R.string.button_add_amend_cancel, new DialogInterface.OnClickListener() {

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
                EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_amend_name);
                String name = editTextName.getText().toString();

                TextView textViewErorr = (TextView) layoutView.findViewById(R.id.ErrorAddAmend);
                ArrayList<String> roomAmendsList = ((MyApplicationClass) getApplicationContext()).roomAmendsList;

                if (name.equals("") || name.equals(null) || name.length() <= 0) {
                    textViewErorr.setText("Please add the name of the Amend.");
                    textViewErorr.setVisibility(View.VISIBLE);
                } else if (roomAmendsList.contains(name)) {
                    textViewErorr.setText("Amend with this name already present");
                    textViewErorr.setVisibility(View.VISIBLE);
                } else {
                    textViewErorr.setVisibility(View.GONE);
                    if (((MyApplicationClass) getApplicationContext()).roommatesList.size() > 0) {
                        String displaySuccessAlert = "An email has been sent to your Roommates. Once everyone approves your request to add the Amend " + name + ", " + name + " will be added to your Room Amends List";
                        Toast toast = Toast.makeText(RoomAmendsActivity.this, displaySuccessAlert, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    ((MyApplicationClass) getApplicationContext()).roomAmendsList.add(name);

                    ListView amendsListView = (ListView) RoomAmendsActivity.this.findViewById(R.id.RoomAmendsList);
                    adapter = new ArrayAdapter<String>(RoomAmendsActivity.this, android.R.layout.simple_list_item_1, amendsListItems);
                    amendsListView.setAdapter(adapter);

                    TextView textView = (TextView) RoomAmendsActivity.this.findViewById(R.id.ErrorViewAmends);
                    textView.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }
        });
    }

    /*public void goBackHomeFromAmends(View view){
        Intent intent=new Intent(this,WelcomePageActivity.class);
        startActivity(intent);
    }*/

}
