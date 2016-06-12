package edu.drexel.ea464.doomies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ViewRoommatesActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ListView listView;

    ArrayList<String> roommatesListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_roommates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> roommates = ((MyApplicationClass) getApplicationContext()).roommatesList;
        if (roommates.size() <= 0 || roommates == null)
            roommates.add("Myself");

        roommatesListItems = ((MyApplicationClass) getApplicationContext()).roommatesList;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roommatesListItems);
        listView = (ListView) findViewById(R.id.RoommatesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewRoommatesActivity.this);
                builder.setTitle("Roommate Linkage");
                String roommateSelected = parent.getAdapter().getItem(position).toString();

                HashMap<String,ArrayList<String>> roommateDutyMap=((MyApplicationClass)getApplicationContext()).roommate_duty_hashMap;
                ArrayList<String> dutyLinkTo=new ArrayList<String>();

                if(roommateDutyMap.containsKey(roommateSelected)){
                    dutyLinkTo=roommateDutyMap.get(roommateSelected);
                }
                Log.d("dutyLinked to size=",Integer.toString(dutyLinkTo.size()));
                if(dutyLinkTo!=null && dutyLinkTo.size()>0)
                    builder.setMessage("Roommate: "+roommateSelected + " is linked to Duties: " + dutyLinkTo.toString());
                else
                    builder.setMessage("Roommate " +roommateSelected+" is not currently linked to any Duty");

                Log.d("duties linked=", dutyLinkTo.toString());

                builder.setPositiveButton("Ok", null);
                builder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ViewRoommatesActivity.this);
                builder.setTitle("Delete?");
                builder.setMessage("Are sure you want to delete "+parent.getAdapter().getItem(position).toString());
                final int positionToRemove=position;
                builder.setNegativeButton("Cancel",null);
                builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name=parent.getAdapter().getItem(positionToRemove).toString();
                        String displaySuccessAlert="An email has been sent to "+name+" . Once "+name+" approves your request, "+name+" will be deleted from your Roommates list";
                        Toast toast=Toast.makeText(ViewRoommatesActivity.this,displaySuccessAlert,Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();

                        roommatesListItems.remove(positionToRemove);
                        if(roommatesListItems.size()<=0 || roommatesListItems==null){
                            TextView textView = (TextView) ViewRoommatesActivity.this.findViewById(R.id.ErrorViewRoommates);
                            textView.setText("Room is Empty. Please add a Roommate");
                            textView.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                return false;
            }
        });

        if (roommatesListItems.size() <= 0 || roommatesListItems == null) {
            TextView textView = (TextView) findViewById(R.id.ErrorViewRoommates);
            textView.setText("Room is Empty. Please add a Roommate");
        }

    }

    public void addRoommate(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.add_roommate_layout, null);

        //Link Roommate with Duty
        ArrayList<String> roomDuties = ((MyApplicationClass) getApplicationContext()).roomDutiesList;

        ListView listViewLinkRoommateWithDuty = (ListView) layoutView.findViewById(R.id.LinkRoommateWithDuty);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ViewRoommatesActivity.this, android.R.layout.simple_list_item_1, roomDuties);
        if (roomDuties.size() <= 0 || roomDuties == null) {
            TextView textViewLinkRoommateWithDuty = (TextView) layoutView.findViewById(R.id.TextViewSelectDutyForRoommate);
            textViewLinkRoommateWithDuty.setVisibility(View.GONE);
        }


        listViewLinkRoommateWithDuty.setAdapter(adapter1);

        listViewLinkRoommateWithDuty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDuty = parent.getAdapter().getItem(position).toString();
                HashMap<String, ArrayList<String>> linkRoommateWithDuty = ((MyApplicationClass) getApplicationContext()).roommate_duty_hashMap;
                EditText editTextAmendName = (EditText) layoutView.findViewById(R.id.editText_add_roommate_name);
                String roommateName = editTextAmendName.getText().toString();

                ArrayList<String> dutiesArray;
                if(linkRoommateWithDuty.containsKey(roommateName)){
                    dutiesArray=linkRoommateWithDuty.get(roommateName);
                    dutiesArray.add(selectedDuty);
                }else{
                    dutiesArray=new ArrayList<String>();
                    dutiesArray.add(selectedDuty);
                }

                linkRoommateWithDuty.put(roommateName,dutiesArray);

                Log.d("Roommate="+roommateName, "linked to duty = " + linkRoommateWithDuty.get(roommateName));
            }
        });

        builder.setView(layoutView)
                .setPositiveButton(R.string.button_add_roommate_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_roommate_name);
                        String name = editTextName.getText().toString();

                        String displaySuccessAlert="An email has been sent to "+name+" . Once "+name+" approves your request, "+name+" will be added to your Roommates list";
                        Toast toast=Toast.makeText(ViewRoommatesActivity.this,displaySuccessAlert,Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();

                        ((MyApplicationClass) getApplicationContext()).roommatesList.add(name);

                        ListView roommatesListView = (ListView) ViewRoommatesActivity.this.findViewById(R.id.RoommatesList);
                        adapter = new ArrayAdapter<String>(ViewRoommatesActivity.this, android.R.layout.simple_list_item_1, roommatesListItems);
                        roommatesListView.setAdapter(adapter);

                        TextView textView = (TextView) ViewRoommatesActivity.this.findViewById(R.id.ErrorViewRoommates);
                        textView.setVisibility(View.GONE);*/
                    }
                })
                .setNegativeButton(R.string.button_add_roommate_cancel, new DialogInterface.OnClickListener() {

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
                EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_roommate_name);
                String name = editTextName.getText().toString();

                EditText editTextEmail=(EditText)layoutView.findViewById(R.id.editText_add_roommate_email);
                String email=editTextEmail.getText().toString();

                TextView textViewErorr = (TextView) layoutView.findViewById(R.id.ErrorAddRoommate);
                //ArrayList<String> roommatesList = ((MyApplicationClass) getApplicationContext()).roommatesList;
                HashMap<String, String> roommateNameAndEmail=((MyApplicationClass)getApplicationContext()).roommateNameAndEmail;

               // Log.d(Boolean.toString(roommateNameAndEmail.containsKey(name)),Boolean.toString(roommateNameAndEmail.get(name).equals(email)));

                if (name.equals("") || name.equals(null) || name.length() <= 0) {
                    textViewErorr.setText("Please add the name of the Roommate.");
                    textViewErorr.setVisibility(View.VISIBLE);
                }else if(email.equals("") || email.equals(null) || email.length() <= 0){
                    textViewErorr.setText("Please add the email of the Roommate.");
                    textViewErorr.setVisibility(View.VISIBLE);
                }else if (roommateNameAndEmail.containsKey(name)&& roommateNameAndEmail.get(name).equals(email)) {
                    textViewErorr.setText("Roommate with this name and email already present");
                    textViewErorr.setVisibility(View.VISIBLE);
                } else {
                    textViewErorr.setVisibility(View.GONE);

                    if (((MyApplicationClass) getApplicationContext()).roommatesList.size() > 0) {
                        String displaySuccessAlert = "An email has been sent to your Roommates. Once everyone approves your request to add the Amend " + name + ", " + name + " will be added to your Room Amends List";
                        Toast toast = Toast.makeText(ViewRoommatesActivity.this, displaySuccessAlert, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    ((MyApplicationClass) getApplicationContext()).roommatesList.add(name);
                    ((MyApplicationClass)getApplicationContext()).roommateNameAndEmail.put(name,email);

                    ListView roommatesListView = (ListView) ViewRoommatesActivity.this.findViewById(R.id.RoommatesList);
                    adapter = new ArrayAdapter<String>(ViewRoommatesActivity.this, android.R.layout.simple_list_item_1, roommatesListItems);
                    roommatesListView.setAdapter(adapter);

                    TextView textView = (TextView) ViewRoommatesActivity.this.findViewById(R.id.ErrorViewRoommates);
                    textView.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }
        });
    }

   /* public void goBackHomeFromRoommates(View view){
        Intent intent=new Intent(this,WelcomePageActivity.class);
        startActivity(intent);
    }*/

}
