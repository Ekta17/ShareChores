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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.drexel.ea464.doomies.database.DatabaseAccess;

public class MyTasksActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String userLoggedIn;
    private String selectedRoom;
    String[] allTasks;
    final String PENDING_TASKS="You have some incomplete tasks.";
    final String NO_PENDING_TASKS="There are no pending tasks for you!";
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);

        this.userLoggedIn=((MyApplicationClass)getApplicationContext()).user.getEmail();
        this.selectedRoom=((MyApplicationClass)getApplicationContext()).selectedRoomName;

        TasksOfUser tasksOfUser=new TasksOfUser();
        tasksOfUser.execute();

    }

    public void backToViewRoom(View view){
        Intent intent=new Intent(this,ViewRoomActivity.class);
        startActivity(intent);
    }

    public void addLog(View view){
        /*Intent intent=new Intent(this,AddLogActivity.class);
        startActivity(intent);*/
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedTask=parent.getSelectedItem().toString();

        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(this);
        String taskDueDate=databaseAccess.getDueDateOfTask(selectedTask,userLoggedIn,selectedRoom);

        TextView textView=(TextView)findViewById(R.id.view_my_task_due_date);
        textView.setText(taskDueDate);
        textView.setTextSize(15);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class TasksOfUser extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {

            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(MyTasksActivity.this);
            ArrayList<TaskBean> myTasksList=databaseAccess.getTasksList(userLoggedIn,selectedRoom);

            if(myTasksList!=null && myTasksList.size()>0) {
                ArrayList<String> allAssignedTasks = new ArrayList<>();

                for (TaskBean taskAssigned : myTasksList) {
                    allAssignedTasks.add(taskAssigned.getDutyName());
                }

                allTasks = allAssignedTasks.toArray(new String[allAssignedTasks.size()]);
                result=PENDING_TASKS;
            }else{
                result=NO_PENDING_TASKS;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            if(result!=null && result.equals(PENDING_TASKS)){

                Spinner spinner = (Spinner) findViewById(R.id.spinner_my_tasks_duties);
                ArrayAdapter arrayAdapter = new ArrayAdapter(MyTasksActivity.this, android.R.layout.simple_spinner_item, allTasks);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(MyTasksActivity.this);

            }else if(result!=null && result.equals(NO_PENDING_TASKS)){

                AlertDialog.Builder builder = new AlertDialog.Builder(MyTasksActivity.this);
                builder.setTitle("Tasks Assigned");
                builder.setMessage("There are no pending tasks for you!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(MyTasksActivity.this,ViewRoomActivity.class);
                        startActivity(intent);
                    }
                });

                builder.show();
            }

        }
    }
}
