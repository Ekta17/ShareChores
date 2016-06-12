package edu.drexel.ea464.doomies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyTasksActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);

        ArrayList<String> myTasksList=((MyApplicationClass)getApplicationContext()).myTaskList;
        //final String[] roomNames=roomArrayList.toArray(new String[roomArrayList.size()]);
        Spinner spinner=(Spinner)findViewById(R.id.spinner_my_tasks_duties);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,myTasksList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void backToViewRoom(View view){
        Intent intent=new Intent(this,ViewRoomActivity.class);
        //String roomName=((MyApplicationClass)getApplicationContext()).roomName;
        //intent.putExtra("RoomName",roomName);
        //intent.putExtra("RoomName",roomDescription);

        startActivity(intent);
    }

    public void addLog(View view){
        /*Intent intent=new Intent(this,AddLogActivity.class);
        startActivity(intent);*/
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedTask=parent.getSelectedItem().toString();

        TextView textView=(TextView)findViewById(R.id.view_my_task_due_date);

        ArrayList<String> myTasksList=((MyApplicationClass)getApplicationContext()).myTaskList;
        Calendar calendar=new GregorianCalendar();
        //calendar.set(Calendar.HOUR_OF_DAY,0);
        //calendar.set(Calendar.MINUTE,0);
        //calendar.set(Calendar.SECOND,0);
        Date date=calendar.getTime();
        System.out.println("date = "+date.toString());
        if(selectedTask==myTasksList.get(0))
            textView.setText("03 June 2016");
        else if(selectedTask==myTasksList.get(1))
            textView.setText("20 June 2016");
        else if(selectedTask==myTasksList.get(2))
            textView.setText("15 June 2016");

        textView.setTextSize(15);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
