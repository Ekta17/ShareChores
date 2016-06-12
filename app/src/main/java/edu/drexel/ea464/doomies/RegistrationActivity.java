package edu.drexel.ea464.doomies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import edu.drexel.ea464.doomies.database.DatabaseAccess;
/*
import java.io.File;

import edu.drexel.ea464.doomies.database.DatabaseAccess;*/

public class RegistrationActivity extends AppCompatActivity {

    /*private Context context;
    private static String filename="Doomies.txt";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* this.context=this.getApplicationContext();*/
    }

    public void showTnC(View view){
        Intent intent= new Intent(this, ShowTnCActivity.class);
        startActivity(intent);
    }

    public void cancelRegistration(View view){
        Intent intent= new Intent(this, EntryPageActivity.class);
        startActivity(intent);
    }


    public void signUpUser(View view){

        Intent intent;

        EditText editTextRegisterName=(EditText)findViewById(R.id.registerName);
        String registerName=editTextRegisterName.getText().toString();

        EditText editTextPassword=(EditText)findViewById(R.id.registerPassword);
        String password=editTextPassword.getText().toString();

        EditText editTextConfirmPassword=(EditText)findViewById(R.id.registerConfirmPassword);
        String confirmPassword=editTextConfirmPassword.getText().toString();

        EditText editTextEmail=(EditText)findViewById(R.id.registerEmail);
        String email=editTextEmail.getText().toString();

        EditText editTextPhone=(EditText)findViewById(R.id.registerPhone);
        String phone=editTextPhone.getText().toString();

        if(registerName==null || password==null ||confirmPassword ==null || phone ==null || email==null || password!=confirmPassword){
            intent=new Intent(this,this.getClass());
            startActivity(intent);
        }else{
            //Check if the user already exists
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            boolean userAlreadyExists=databaseAccess.checkIfUserExists(email,phone);
            if(userAlreadyExists){
                intent=new Intent(this,this.getClass());
                startActivity(intent);
            }else{
                //else register User
                databaseAccess.createNewUser(registerName,password,email,phone);
                intent = new Intent(this, EntryPageActivity.class);
                startActivity(intent);
            }
            databaseAccess.close();

        }

        /*intent = new Intent(this, EntryPageActivity.class);
        startActivity(intent);*/
    }

}
