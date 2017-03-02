package edu.drexel.ea464.doomies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
/*

*
 * Created by Ekta on 5/23/2016.

*/

public class DatabaseOpenHelper extends SQLiteAssetHelper /*SQLiteOpenHelper*/
{

    //private String DATABASE_PATH = "//assets/database/";
    //private final Context context;
    private static final String DATABASE_NAME="doomies.db";
    private static final int DATABASE_VERSION=2;
    private SQLiteDatabase db;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("context = "+context.toString());
        //this.getWritableDatabase();
        //createDB();
        /*this.context = context;
        this.DATABASE_PATH=context.getDatabasePath(DATABASE_NAME).toString();
        createDatabase();*/
    }
/*
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Login (userId TEXT NOT NULL,password TEXT NOT NULL,logged_in INTEGER NOT NULL,phone TEXT NOT NULL,emailId TEXT NOT NULL,PRIMARY KEY(userId));");
        db.execSQL("CREATE TABLE RoomDetails (roomName TEXT NOT NULL, roomDescription TEXT, roomId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT);");
        db.execSQL("CREATE TABLE RoommateDetails (userId TEXT NOT NULL, roomId INTEGER NOT NULL, PRIMARY KEY(userId,roomId));");
        //db.execSQL("CREATE TABLE sqlite_sequence(name,seq);");
        //createDB();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }*/

    /*public void createDatabase() {
        Log.i("DatabaseAccess.createDatabase()::", "Creating the database");
        createDB();
    }*/

    /*\private void createDB() {
        boolean dbExist = DBExists();
        Log.d("DatabaseAccess.createDB()::dbExist?", dbExist ? "true" : "false");

        if (!dbExist) {
            Log.i("DatabaseAccess.createDB::", "DB doesnot exist, calling copyDBFromResourse()");
            this.getWritableDatabase();
            copyDBFromResource();
        }
    }

    private boolean DBExists() {

        SQLiteDatabase database = null;

        try {

            String databasePath = this.context.getApplicationContext().getAssets().toString()+DATABASE_NAME ;
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

        Log.i("I am Here Ekta -----------------------> ", " Why this not printing");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String dbFilePath = this.context.getApplicationContext().getAssets().toString();
        Log.i("database path",dbFilePath);
        Log.i("database name",DATABASE_NAME);

        try {

            inputStream = this.context.getApplicationContext().getAssets().open(DATABASE_NAME);
            outputStream = new FileOutputStream(dbFilePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
                outputStream.write(buffer, 0, length);

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException ioException) {
            Log.e("DatabaseAccess.copyDBFromResource()::", "Error occured while creating Database"+ioException.getMessage(), new Error("Problem copying database from resource file"));
        }
    }*/

}
