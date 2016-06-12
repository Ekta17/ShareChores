package edu.drexel.ea464.doomies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
/*


*
 * Created by Ekta on 5/23/2016.

*/

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

/*
*
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context

*/

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }
/*
*
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }
/*

*
     * Open the database connection.

*/

    public void open() {

        this.database = openHelper.getWritableDatabase();
    }

/*
*
     * Close the database connection.

*/

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

/*public void createDatabase() {
        Log.i("DatabaseAccess.createDatabase()::", "Creating the database");
        createDB();
    }

    private void createDB() {
        boolean dbExist = DBExists();
        Log.d("DatabaseAccess.createDB()::dbExist?", dbExist ? "true" : "false");

        if (!dbExist) {
            Log.i("DatabaseAccess.createDB::", "DB doesnot exist, calling copyDBFromResourse()");
            this.getReadableDatabase();
            copyDBFromResource();
        }
    }

    private boolean DBExists() {

        SQLiteDatabase database = null;

        try {

            String databasePath = DATABASE_PATH ;
            database = SQLiteDatabase.openDatabase(databasePath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            database.setLocale(Locale.getDefault());
            database.setVersion(1);
        } catch (SQLiteException ex) {
            Log.e("DatabaseAccess.DBExists()::", "Database not Found");
        }

        if (database != null) {
            database.close();
        }
        return database != null ? true : false;
    }

    private void copyDBFromResource() {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        String dbFilePath = DATABASE_PATH;
        Log.i("database path",DATABASE_PATH);
        Log.i("database name",DATABASE_NAME);

        try {

            inputStream = context.getAssets().open(DATABASE_NAME);
            outputStream = new FileOutputStream(dbFilePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
                outputStream.write(buffer, 0, length);

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException ioException) {
            Log.e("DatabaseAccess.copyDBFromResource()::", "Error occured while creating Database", new Error("Problem copying database from resource file"));
        }
    }*/


    public boolean verifyUser(String userName, String password) {

        /*String getUserDetails="SELECT USERID,PASSWORD FROM LOGIN WHERE USERID=\""+userName+"\" and PASSWORD=\""+password+"\"";
        Log.i("DatabaseAccess.verifyUser()::", "Query to verify User = "+getUserDetails );

        Cursor cursor = database.rawQuery(getUserDetails, null);

        if(!(cursor.isNull(0) && cursor.isNull(1)))
            return true;

        cursor.close();
        database.close();

        return false;*/

        /*ContentValues initialValues = new ContentValues();
        initialValues.put("USERID","jatin");
        initialValues.put("PASSWORD","abc");
        initialValues.put("logged_in",1);
        initialValues.put("phone","abc");
        initialValues.put("emailID","abc");*/

        String getUserDetails = "SELECT USERID,PASSWORD FROM LOGIN WHERE USERID='"+userName+"' AND PASSWORD='"+password+"'";
        Log.i("DatabaseAccess.verifyUser()::", "Query to verify User = " + getUserDetails);
        Cursor cursor = null;
        boolean result = false;

        try {
            /*Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    System.out.println("Table Name-->>" + c.getString(0));
                    c.moveToNext();
                }
            }*/
            cursor = database.rawQuery(getUserDetails, null);

            System.out.println("Count Jatin-->>" + cursor.getCount());

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    System.out.println("Second Name-->>" + cursor.getString(0));
                    cursor.moveToNext();
                }
            }

            if (!(cursor.isNull(0) && cursor.isNull(1)))
                result = true;


        } catch (Exception e) {
            result = false;
            if (cursor != null)
                database.close();

            database.close();

            e.printStackTrace();
        }
        return result;
    }

    public boolean checkIfUserExists(String email, String phone) {

        String checkUser = "SELECT EMAIL FROM LOGIN WHERE EMAILID='" + email + "' and PHONE='" + phone + "'";
        Log.i("DatabaseAccess.verifyUser()::", "Query to verify User = " + checkUser);

        Cursor cursor = database.rawQuery(checkUser, null);

        if (!(cursor.isNull(0) && cursor.isNull(1)))
            return true;

        cursor.close();
        database.close();

        return false;
    }

    public void createNewUser(String userName, String password, String email, String phone) {

        /*String createUser = "INSERT INTO LOGIN (USERID,PASSWORD,EMAILID,PHONE,LOGGED_IN) VALUES (\"" + userName + "\",\"" + password + "\",\"" + email + "\",\"" + phone + "\",0)";
        Log.i("DatabaseAccess.verifyUser()::", "Query to verify User = " + createUser);
        */
        Log.d("DatabaseAccess::createNewUser", "creating new User with values:" + userName + " " + email + " " + phone);

        ContentValues values = new ContentValues();
        values.put("USERID", userName);
        values.put("PASSWORD", password);
        values.put("EMAILID", email);
        values.put("PHONE", phone);
        values.put("LOGGED_IN", 0);

        // Inserting Row
        long i = database.insert("LOGIN", null, values);
        Log.d("DatabaseAccess::createNewUser", "Is row inserted : " + i);
        database.close(); // Closing database connection
    }

}
