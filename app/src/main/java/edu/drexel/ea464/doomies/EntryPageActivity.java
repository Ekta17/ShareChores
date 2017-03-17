package edu.drexel.ea464.doomies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.UiThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import java.util.ArrayList;


import edu.drexel.ea464.doomies.database.DatabaseAccess;

public class EntryPageActivity extends AppCompatActivity {

    UserBean getUser;
    ArrayList<RoomBean> roomsLinked;
    boolean userVerified=false;
    AlertDialog alertDialog;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAboutUs(View view){
        Intent intent= new Intent(this, ShowAboutUsActivity.class);
        startActivity(intent);
    }

    public void RegisterUser(View view){
        Intent intent= new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @UiThread
    public void authenticateUser(View view){

        alertBuilder=new AlertDialog.Builder(this);

        EditText editTextUserName=(EditText)findViewById(R.id.username);
        String userName=editTextUserName.getText().toString(); //Email of user

        EditText editTextPassword=(EditText)findViewById(R.id.password);
        String password=editTextPassword.getText().toString();

        checkUser user=new checkUser();
        String[] params={userName,password};
        user.execute(params);
    }

    public void ViewUserIds(View view){
        Intent intent= new Intent(this, ViewUseridActivity.class);
        startActivity(intent);
    }

   @MainThread
    private class checkUser extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String[] params) {
            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(EntryPageActivity.this);

            getUser= databaseAccess.verifyAndGetUser(params[0],params[1]);

            if(getUser!=null){
                userVerified=true;
                ArrayList<RoomBean> roomsLinked=databaseAccess.getLinkedRooms(params[0]);

                //Add user details to session
                ((MyApplicationClass) getApplicationContext()).user.setName(getUser.getName());
                ((MyApplicationClass) getApplicationContext()).user.setEmail(getUser.getEmail());
                ((MyApplicationClass) getApplicationContext()).user.setPhone(getUser.getPhone());
                ((MyApplicationClass) getApplicationContext()).user.setRoomsLinked(roomsLinked);

            }else{
                userVerified=false;
            }

            return userVerified;
        }

        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);

            if(result==true){
                Intent intent= new Intent(EntryPageActivity.this, WelcomePageActivity.class);
                startActivity(intent);
            }else{
                alertDialog=alertBuilder.create();
                alertDialog.setMessage("Invalid User Name/Password.");
                alertDialog.show();
                Intent intent=new Intent(EntryPageActivity.this,EntryPageActivity.class);
                startActivity(intent);
            }

        }
    }

}
