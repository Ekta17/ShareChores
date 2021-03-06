package edu.drexel.ea464.doomies;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.drexel.ea464.doomies.database.DatabaseAccess;

public class ViewRoommatesActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ListView listView;

    private String userLoggedIn;
    private String selectedRoom;
    ArrayList<String> roommateDetails;
    Boolean result;
    String[] outerSelectedDuty;
    ArrayList<String> allDutiesInRoom;

    Boolean dutiesExistInRoom;
    AlertDialog dialog;
    TextView textViewError;
    ArrayList<String> roommatesInRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_roommates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.selectedRoom = ((MyApplicationClass) getApplicationContext()).selectedRoomName;
        RoommatesInRoom roommatesInRoom=new RoommatesInRoom();
        roommatesInRoom.execute();
    }

    public void addRoommate(View view) {

        DutiesToDialog dutiesToDialog=new DutiesToDialog();
        dutiesToDialog.execute();
    }

    private class RoommatesInRoom extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ViewRoommatesActivity.this);

            roommateDetails = databaseAccess.getRoommates(selectedRoom);

            if (roommateDetails != null) {
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
                adapter = new ArrayAdapter<String>(ViewRoommatesActivity.this, android.R.layout.simple_list_item_1, roommateDetails);
                listView = (ListView) findViewById(R.id.RoommatesList);
                listView.setAdapter(adapter);
            }else if (!result){
                TextView textView = (TextView) findViewById(R.id.ErrorViewRoommates);
                textView.setText("Room is Empty. Please add a Roommate");
            }
        }
    }

    private class DutiesToDialog extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(ViewRoommatesActivity.this);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(ViewRoommatesActivity.this);
            LayoutInflater inflater = ViewRoommatesActivity.this.getLayoutInflater();
            final View layoutView = inflater.inflate(R.layout.add_roommate_layout, null);

            if(result){ //Duties are present in Room
                ListView listViewLinkRoommateWithDuty = (ListView) layoutView.findViewById(R.id.LinkRoommateWithDuty);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ViewRoommatesActivity.this, android.R.layout.simple_list_item_1, allDutiesInRoom);
                listViewLinkRoommateWithDuty.setAdapter(adapter1);

                listViewLinkRoommateWithDuty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String innerSelectedDuty = parent.getAdapter().getItem(position).toString();
                        outerSelectedDuty[0]=innerSelectedDuty;
                    }
                });

            }else if (!result){
                TextView textViewLinkRoommateWithDuty = (TextView) layoutView.findViewById(R.id.TextViewSelectDutyForRoommate);
                textViewLinkRoommateWithDuty.setVisibility(View.GONE);
            }

            builder.setView(layoutView)
                    .setPositiveButton(R.string.button_add_roommate_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(R.string.button_add_roommate_cancel, new DialogInterface.OnClickListener() {

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
                    EditText editTextName = (EditText) layoutView.findViewById(R.id.editText_add_roommate_name);
                    String name = editTextName.getText().toString();

                    EditText editTextEmail=(EditText)layoutView.findViewById(R.id.editText_add_roommate_email);
                    String email=editTextEmail.getText().toString();

                    textViewError = (TextView) layoutView.findViewById(R.id.ErrorAddRoommate);

                    if (name.equals("") || name==null || name.length() <= 0) {
                        textViewError.setText("Please add the name of the Roommate.");
                        textViewError.setVisibility(View.VISIBLE);
                    }else if(email.equals("") || email==null || email.length() <= 0){
                        textViewError.setText("Please add the email of the Roommate.");
                        textViewError.setVisibility(View.VISIBLE);
                    }else {
                        textViewError.setVisibility(View.GONE);
                        AddRoommatesInRoom addRoommatesInRoom=new AddRoommatesInRoom();
                        addRoommatesInRoom.execute(name,email);
                    }}
            });
        }//onPostExecute end
    }

    private class AddRoommatesInRoom extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            String name=params[0];
            String email=params[1];

            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(ViewRoommatesActivity.this);

            if (databaseAccess.addNewRoommate(selectedRoom,name,email)) {

                if (outerSelectedDuty[0] != null && outerSelectedDuty[0].length() > 0)
                    databaseAccess.linkDutyWithRoommate(selectedRoom, outerSelectedDuty[0], email );

               roommatesInRoom=databaseAccess.getRoommates(selectedRoom);

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

                ListView roommatesListView = (ListView) ViewRoommatesActivity.this.findViewById(R.id.RoommatesList);
                adapter = new ArrayAdapter<String>(ViewRoommatesActivity.this, android.R.layout.simple_list_item_1, roommatesInRoom);
                roommatesListView.setAdapter(adapter);

                TextView textView = (TextView) ViewRoommatesActivity.this.findViewById(R.id.ErrorViewRoommates);
                textView.setVisibility(View.GONE);
                dialog.dismiss();

            }else if(!result){
                textViewError.setText("Some unexpected error has occured");
                textViewError.setVisibility(View.VISIBLE);
            }
        }
    }

}
