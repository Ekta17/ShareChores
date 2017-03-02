package edu.drexel.ea464.doomies.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ekta on 1/23/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    Context ctx;

    // Database Name
    private static String DATABASE_NAME = "shareChores";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx=context;
        this.DATABASE_NAME = DATABASE_NAME;
        //this.DBPATH = this.ctx.getDatabasePath(DBNAME).getAbsolutePath();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE UserDetails (\n" +
                "\tname\tTEXT NOT NULL,\n" +
                "\tpassword\tTEXT NOT NULL,\n" +
                "\temail\tTEXT NOT NULL UNIQUE,\n" +
                "\tphone INTEGER,\n" +
                "\tPRIMARY KEY(email)\n" +
                ");");
        db.execSQL("CREATE TABLE Room (\n" +
                "\troomName\tTEXT NOT NULL,\n" +
                "\troomDescription\tTEXT,\n" +
                "\temail\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(roomName,email),\n" +
                "\tFOREIGN KEY(email) REFERENCES UserDetails(email)\n" +
                ");");
        db.execSQL("CREATE TABLE Duties (\n" +
                "\tdutyName\tTEXT NOT NULL,\n" +
                "\tdutyDescription\tTEXT,\n" +
                "\troomName TEXT NOT NULL,\n" +
                "\tPRIMARY KEY(dutyName,roomName),\n" +
                "\tFOREIGN KEY(roomName) REFERENCES Room(roomName)\n" +
                ");");
        db.execSQL("CREATE TABLE Amends (\n" +
                "\tamendName\tTEXT NOT NULL,\n" +
                "\tamendDescription\tTEXT,\n" +
                "\troomName TEXT NOT NULL,\n" +
                "\tPRIMARY KEY(amendName,roomName),\n" +
                "\tFOREIGN KEY(roomName) REFERENCES Room(roomName)\n" +
                ");");
        db.execSQL("CREATE TABLE Link_Duty_Amend (\n" +
                "\tdutyName\tTEXT NOT NULL,\n" +
                "\tamendName\tTEXT NOT NULL,\n" +
                "\troomName TEXT NOT NULL,\n" +
                "\tPRIMARY KEY(dutyName,amendName),\n" +
                "\tFOREIGN KEY(dutyName) REFERENCES Duties(dutyName),\n" +
                "\tFOREIGN KEY(amendName) REFERENCES Amends(amendName),\n" +
                "\tFOREIGN KEY(roomName) REFERENCES Room(roomName)\n" +
                ");");
        db.execSQL("CREATE TABLE Link_Duty_Roommate (\n" +
                "\tdutyName\tTEXT NOT NULL,\n" +
                "\tuserEmail TEXT NOT NULL,\n" +
                "\troomName TEXT NOT NULL,\n" +
                "\tPRIMARY KEY(dutyName,userEmail),\n" +
                "\tFOREIGN KEY(dutyName) REFERENCES Duties(dutyName),\n" +
                "\tFOREIGN KEY(userEmail) REFERENCES UserDetails(email),\n" +
                "\tFOREIGN KEY(roomName) REFERENCES Room(roomName)\n" +
                ");");
        db.execSQL("CREATE TABLE Tasks_Assigned (\n" +
                "\tdutyName\tTEXT NOT NULL,\n" +
                "\tamendName\tTEXT NOT NULL,\n" +
                "\tuserEmail TEXT NOT NULL,\n" +
                "\troomName TEXT NOT NULL,\n" +
                "\tdutyDueDate TEXT NOT NULL,\n" +
                "\tdutyCompletionDate TEXT,\n" +
                "\tamendDueDate TEXT NOT NULL,\n" +
                "\tamendCompletionDate TEXT,\n" +
                "\tPRIMARY KEY(dutyName,amendName,userEmail,roomName),\n" +
                "\tFOREIGN KEY(dutyName) REFERENCES Duties(dutyName),\n" +
                "\tFOREIGN KEY(amendName) REFERENCES Amends(amendName),\n" +
                "\tFOREIGN KEY(userEmail) REFERENCES UserDetails(email),\n" +
                "\tFOREIGN KEY(roomName) REFERENCES Room(roomName)\n" +
                ");");

        //for testing purpose, added a default row
        addDefaultRow(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {

            db.execSQL("drop table if exists UserDetails ");
            db.execSQL("drop table if exists Room");
            db.execSQL("drop table if exists Duties");
            db.execSQL("drop table if exists Amends");
            db.execSQL("drop table if exists Link_Duty_Amend");
            db.execSQL("drop table if exists Link_Duty_Roommate");
            db.execSQL("drop table if exists Tasks_Assigned");

            onCreate(db);
        }
    }

    private void addDefaultRow(SQLiteDatabase db){
        String insert_default_userDets="insert into UserDetails values ('ekta','123','ekta.arora@blah.com',1234567890)";
        db.execSQL(insert_default_userDets);
    }
}
