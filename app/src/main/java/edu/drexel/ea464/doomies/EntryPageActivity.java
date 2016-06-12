package edu.drexel.ea464.doomies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

import edu.drexel.ea464.doomies.database.DatabaseAccess;

/*import edu.drexel.ea464.doomies.database.DatabaseAccess;*/

public class EntryPageActivity extends AppCompatActivity {

    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this.databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        ArrayList<String> myTasksList=((MyApplicationClass)getApplicationContext()).myTaskList;
        myTasksList.add("Groceries");
        myTasksList.add("Change Lamp Light");
        myTasksList.add("Cook Dinner");
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

    public void authenticateUser(View view){
        EditText editTextUserName=(EditText)findViewById(R.id.username);
        String userName=editTextUserName.getText().toString();

        EditText editTextPassword=(EditText)findViewById(R.id.password);
        String password=editTextPassword.getText().toString();

        /*DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        Log.i("details passed ",userName+" "+password);

        boolean validUser=databaseAccess.verifyUser(userName,password);
        databaseAccess.close();

        if(validUser){
            Intent intent= new Intent(this, WelcomePageActivity.class);
            startActivity(intent);
        }else{
            Intent intent=new Intent(this,this.getClass());
            startActivity(intent);
        }*/

        /*Intent intent= new Intent(this, WelcomePageActivity.class);
        startActivity(intent);*/

        /*databaseAccess.open();
        boolean validUser=databaseAccess.verifyUser(userName,password);
        Log.d("validUser?",new Boolean(validUser).toString());
        databaseAccess.close();

        if(validUser){*/
            Intent intent= new Intent(this, WelcomePageActivity.class);
            startActivity(intent);
        /*}else {
            Intent intent = new Intent(this, this.getClass());
            startActivity(intent);

        }*/

    }

}
