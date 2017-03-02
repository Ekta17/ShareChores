package edu.drexel.ea464.doomies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import edu.drexel.ea464.doomies.database.DatabaseAccess;
/*
import java.io.File;

import edu.drexel.ea464.doomies.database.DatabaseAccess;*/

public class RegistrationActivity extends AppCompatActivity {

    AlertDialog.Builder alerBuilder;
    final String USER_ALREADY_EXISTS="User Already Exists with this email address. Please login";
    final String USER_REGISTERED_SUCCESSFULLY="Registration successful! Please Login now";
    final String ERROR_OCCURRED="Some error has occurred. Please try again later!";
    AlertDialog alertDialog;
    String userRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        alerBuilder=new AlertDialog.Builder(this);
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

        String details="name = "+registerName+" , password = "+password+" confirmpwd = "+confirmPassword+" email="+email+" phone= "+phone;
        System.out.println("details==>"+details);

        if(registerName==null ||registerName.length()<=0 || password==null || password.length()<=0 ||confirmPassword==null || confirmPassword.length()<=0 || phone==null || phone.length()<=0 || email==null || email.length()<=0 || !password.equals(confirmPassword)){
            AlertDialog alertDialog=alerBuilder.create();
            alertDialog.setMessage("Please check the details and try again--->"+details);
            alertDialog.show();
        }else{
            RegisterUser registerUser=new RegisterUser();
            registerUser.execute(registerName,password,email,phone);
        }
    }

    private class RegisterUser extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            //Check if the user already exists
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(RegistrationActivity.this);
            if(databaseAccess.checkIfUserEmailExists(params[2])){
                userRegistration=USER_ALREADY_EXISTS;
            }else{
                //else register User
                if(databaseAccess.createNewUser(params[0],params[1],params[2],params[3])) {
                    userRegistration=USER_REGISTERED_SUCCESSFULLY;
                }
            }
            return userRegistration;
        }

        @Override
        protected void onPostExecute(String result){
            Intent intent;
            if(result!=null && result.equals(USER_ALREADY_EXISTS)){
                alertDialog=alerBuilder.create();
                alertDialog.setMessage(result);
                alertDialog.show();
                intent=new Intent(RegistrationActivity.this,EntryPageActivity.class);
                startActivity(intent);
            }else if(result!=null && result.equals(USER_REGISTERED_SUCCESSFULLY)){
                alertDialog=alerBuilder.create();
                alertDialog.setMessage(result);
                alertDialog.show();
                intent = new Intent(RegistrationActivity.this, EntryPageActivity.class);
                startActivity(intent);
            }else{
                alertDialog=alerBuilder.create();
                alertDialog.setMessage(ERROR_OCCURRED);
                alertDialog.show();
                intent=new Intent(RegistrationActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        }
    }

}
