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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.drexel.ea464.doomies.AmendBean;
import edu.drexel.ea464.doomies.DutyBean;
import edu.drexel.ea464.doomies.RoomBean;
import edu.drexel.ea464.doomies.TaskBean;
import edu.drexel.ea464.doomies.UserBean;
/*


*
 * Created by Ekta on 5/23/2016.

*/

public class DatabaseQueryAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseQueryAccess instance;

    /** Private constructor to aboid object creation from outside classes.
     * * @param context
    */
    private DatabaseQueryAccess(Context context) {
        this.openHelper = new DatabaseHandler(context); //new DatabaseOpenHelper(context);
    }


    /** Return a singleton instance of DatabaseQueryAccess.
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseQueryAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseQueryAccess(context);
        }
        return instance;
    }
    // Open the database connection.
    private void open() {

        this.database = openHelper.getWritableDatabase();
    }

    // Close the database connection.
    private void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<String> getUserIds(){

        this.open();
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT email FROM UserDeatils", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            System.out.println("string is = "+cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        this.close();

        return list;
    }

    //Creating a new user account
    public boolean createNewUser(String userName, String password, String email, String phone) {

        boolean insertingSuccess = false;

        ContentValues values = new ContentValues();
        values.put("NAME", userName);
        values.put("PASSWORD", password);
        values.put("EMAIL", email);
        values.put("PHONE", phone);

        this.open();
        // Inserting Row
        long rowId = database.insert("UserDetails", null, values);

        if(rowId>=0){
            insertingSuccess=true;
        }

        System.out.println("createNewUser----->"+rowId);
        this.close();
        return insertingSuccess;
    }

    //Verify is the user is already exists using email
    public boolean checkIfUserEmailExists(String email) {
        String getUserEmail = "SELECT email FROM UserDetails WHERE email='"+email+"'";

        Cursor cursor = null;
        boolean result = false;

        try {
            this.open();
            cursor = database.rawQuery(getUserEmail, null);

            if(cursor.moveToFirst())
                result=true;
            else if(cursor.isNull(0))
                result=false;

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            this.close();
        }finally {
            this.close();
        }
        System.out.println("checkIfUserEmailExists---->"+result);
        return result;
    }

    //Verify and return if valid user
    public UserBean verifyAndGetUser(String email, String password) {
        String getUserDetails = "SELECT name,email,phone FROM UserDetails WHERE email='"+email+"' and password='"+password+"'";

        UserBean user=null;

        Cursor cursor = null;

        try {
            this.open();
            cursor = database.rawQuery(getUserDetails, null);

            if(cursor.moveToFirst()) {
                user=new UserBean();
                user.setName(cursor.getString(0));
                user.setEmail(cursor.getString(1));
                user.setPhone(cursor.getString(2));
            }else if(cursor.isNull(0))
                user=null;

        } catch (Exception e) {
            e.printStackTrace();
            user=null;
            this.close();
        }finally {
            this.close();
        }

        return user;
    }

    public ArrayList<RoomBean> getLinkedRooms(String userEmail){

        ArrayList<RoomBean> roomsLinked=null;
        RoomBean roomBean=null;

        String getRoomDetails="select roomName,roomDescription,email from Room where email='"+userEmail+"'";
        Cursor cursor = null;
        try{
            this.open();
            cursor=database.rawQuery(getRoomDetails,null);
            if(cursor.moveToFirst()){
                roomsLinked=new ArrayList<>();
                do{
                    roomBean=new RoomBean();
                    roomBean.setName(cursor.getString(0));
                    roomBean.setDescription(cursor.getString(1));
                    roomBean.setUserEmail(cursor.getString(2));
                    roomsLinked.add(roomBean);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return null;
        }finally {
            this.close();
        }

        return roomsLinked;
    }

    public boolean checkIfRoomAlreadyExists(String roomName, String userEmail){

        String checkIfRoomExists="Select roomName from Room where roomName='"+roomName+"' and email='"+userEmail+"'";
        Cursor cursor = null;
        boolean result = false;

        try {
            this.open();
            cursor = database.rawQuery(checkIfRoomExists, null);

            if(cursor.moveToFirst())
                result=true;
            else
                result=false;

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            this.close();
        }finally {
            this.close();
        }

        return result;
    }

    public  ArrayList<TaskBean> getTasksList(String userLoggedIn,String selectedRoom){

        ArrayList<TaskBean> tasksAssigned=null;
        TaskBean task;

        String getTasksDetails="select dutyName,amendName,dutyDueDate,dutyCompletionDate,amendDueDate,amendCompletionDate from Tasks_Assigned where userEmail='"+userLoggedIn+"' and roomName='"+selectedRoom+"'   ";
        Cursor cursor = null;
        try{
            this.open();
            cursor=database.rawQuery(getTasksDetails,null);
            if(cursor.moveToFirst()){
                tasksAssigned=new ArrayList<>();
                do{
                    task=new TaskBean();
                    task.setUserEmail(userLoggedIn);
                    task.setRoomName(selectedRoom);
                    task.setDutyName(cursor.getString(0));
                    task.setAmendName(cursor.getString(1));
                    task.setDutyDueDate(cursor.getString(2));
                    task.setDutyCompletionDate(cursor.getString(3));
                    task.setAmendDueDate(cursor.getString(4));
                    task.setAmendCompletionDate(cursor.getString(5));

                    tasksAssigned.add(task);

                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return null;
        }finally {
            this.close();
        }

        return tasksAssigned;

    }

    public String getDueDateOfTask(String selectedTask,String userLoggedIn,String selectedRoom){
        String dueDate=null;

        String getDueDate="select dutyDueDate from Tasks_Assigned where dutyName='"+selectedTask+"' and userEmail='"+userLoggedIn+"' and roomName='"+selectedRoom+"'";

        Cursor cursor = null;
        try{
            this.open();
            cursor=database.rawQuery(getDueDate,null);

            if(cursor.moveToFirst()){
                dueDate=cursor.getString(0);
            }

        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return null;
        }finally {
            this.close();
        }

        return dueDate;
    }

    public ArrayList<DutyBean> getRoomDuties(String selectedRoom){
        ArrayList<DutyBean> roomDuties=null;
        DutyBean dutyBean=null;

        String getRoomDuties="select dutyName,dutyDescription from Duties where roomName='"+selectedRoom+"'";
        Cursor cursor = null;
        try{
            this.open();
            cursor=database.rawQuery(getRoomDuties,null);
            if(cursor.moveToFirst()){
                roomDuties=new ArrayList<>();
                do{
                    dutyBean=new DutyBean();
                    dutyBean.setRoomName(selectedRoom);
                    dutyBean.setDutyName(cursor.getString(0));
                    dutyBean.setDutyDescription(cursor.getString(1));
                  roomDuties.add(dutyBean);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return null;
        }finally {
            this.close();
        }

        return roomDuties;
    }

    public ArrayList<String> getRoommates(String selectedRoom){

        ArrayList<String> roommatesList=new ArrayList<>();

        String getRoommates="select u.name from Room r, UserDetails u where r.roomName='"+selectedRoom+"' and  r.email = u.email";
        Cursor cursor = null;

        try{
            this.open();
            cursor=database.rawQuery(getRoommates,null);
            if(cursor.moveToFirst()){
                do{
                    roommatesList.add(cursor.getString(0));
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return null;
        }finally {
            this.close();
        }

        return roommatesList;
    }

    public ArrayList<AmendBean> getRoomAmends(String selectedRoom){
        ArrayList<AmendBean> roomAmends=null;
        AmendBean amendBean=null;

        String getRoomAmends="select amendName,amendDescription from Amends where roomName='"+selectedRoom+"'";
        Cursor cursor = null;
        try{
            this.open();
            cursor=database.rawQuery(getRoomAmends,null);
            if(cursor.moveToFirst()){
                roomAmends=new ArrayList<>();
                do{
                    amendBean=new AmendBean();
                    amendBean.setRoomName(selectedRoom);
                    amendBean.setAmendName(cursor.getString(0));
                    amendBean.setAmendDescription(cursor.getString(1));
                    roomAmends.add(amendBean);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return null;
        }finally {
            this.close();
        }

        return roomAmends;
    }

    public boolean linkDutyWithAmend(String selectedRoom, String amendName, String dutyName){

        long insertVal=-1;
        ContentValues values=new ContentValues();
        values.put("dutyName",dutyName);
        values.put("amendName",amendName);
        values.put("roomName",selectedRoom);

        try{
            this.open();
            insertVal = database.insert("Link_Duty_Amend", null, values);
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return false;
        }finally {
            this.close();
        }

        if(insertVal>=0)
            return true;

        return false;
    }

    public boolean linkDutyWithRoommate(String selectedRoom,String dutyName,String selectedRoommate){

        long insertVal=-1;
        ContentValues values=new ContentValues();
        values.put("dutyName",dutyName);
        values.put("roomName",selectedRoom);
        values.put("userEmail",selectedRoommate);

        try{
            this.open();
            insertVal = database.insert("Link_Duty_Roommate", null, values);
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return false;
        }finally {
            this.close();
        }

        if(insertVal>=0)
            return true;

        return false;
    }

    public boolean addNewDutyToRoom(String selectedRoom,String name,String description){
        long insertVal=-1;

        ContentValues values=new ContentValues();
        values.put("dutyName",name);
        values.put("dutyDescription",description);
        values.put("roomName",selectedRoom);

        try{
            this.open();
            insertVal = database.insert("Duties", null, values);
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return false;
        }finally {
            this.close();
        }

        if(insertVal>=0)
            return true;

        return false;
    }

    public boolean addNewAmendToRoom(String selectedRoom,String name,String description){
        long insertVal=-1;

        ContentValues values=new ContentValues();
        values.put("amendName",name);
        values.put("amendDescription",description);
        values.put("roomName",selectedRoom);

        try{
            this.open();
            insertVal = database.insert("Amends", null, values);
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return false;
        }finally {
            this.close();
        }

        if(insertVal>=0)
            return true;

        return false;
    }

    public boolean addNewRoommate(String selectedRoom,String name,String email){
        long insertVal=-1;

        //Check is user with name and email exists in the system
        //if not then send back an error msg saying the roommates should be a part of the system first.
        //If yes then add the email and roomName to Room with the desciption of the room

        boolean flag_add_user=createNewUser(name,"1",email,"12345");
        if(flag_add_user) {
            //Add roommate to Room
            ContentValues values = new ContentValues();
            values.put("email", email);
            values.put("roomName", selectedRoom);

            try {
                this.open();
                insertVal = database.insert("Room", null, values);
            } catch (Exception e) {
                e.printStackTrace();
                this.close();
                return false;
            } finally {
                this.close();
            }

            if (insertVal >= 0)
                return true;
        }
        return false;
    }

    public boolean addNewRoom(String roomName,String roomDescription,String loggedInUser){
        long insertVal=-1;

        ContentValues values=new ContentValues();
        values.put("roomName",roomName);
        values.put("roomDescription",roomDescription);
        values.put("email",loggedInUser);

        try{
            this.open();
            insertVal = database.insert("Room", null, values);
        }catch (Exception e){
            e.printStackTrace();
            this.close();
            return false;
        }finally {
            this.close();
        }

        if(insertVal>=0)
            return true;

        return false;
    }

    public void insertDeafultRow(String selectedRoom, String userLoggedIn){
        long insertVal1=-1;

        String dutyName="duty3";
        String amendName="amend3";

        ContentValues duties=new ContentValues();
        duties.put("dutyName",dutyName);
        duties.put("dutyDescription","Duty1_description");
        duties.put("roomName",selectedRoom);

        ContentValues amends=new ContentValues();
        amends.put("amendName",amendName);
        amends.put("amendDescription","amend1_description");
        amends.put("roomName",selectedRoom);

        ContentValues Link_Duty_Amend=new ContentValues();
        Link_Duty_Amend.put("dutyName",dutyName);
        Link_Duty_Amend.put("amendName",amendName);
        Link_Duty_Amend.put("roomName",selectedRoom);

        ContentValues Link_Duty_Roommate=new ContentValues();
        Link_Duty_Roommate.put("dutyName",dutyName);
        Link_Duty_Roommate.put("userEmail",userLoggedIn);
        Link_Duty_Roommate.put("roomName",selectedRoom);

        ContentValues Tasks_Assigned=new ContentValues();
        Tasks_Assigned.put("dutyName",dutyName);
        Tasks_Assigned.put("amendName",amendName);
        Tasks_Assigned.put("userEmail",userLoggedIn);
        Tasks_Assigned.put("roomName",selectedRoom);
        Tasks_Assigned.put("dutyDueDate","2019,02,03");
        Tasks_Assigned.put("dutyCompletionDate","2019,02,03");
        Tasks_Assigned.put("amendDueDate","2019,02,03");
        Tasks_Assigned.put("amendCompletionDate","2019,02,03");

        try{
            this.open();
            insertVal1 = database.insert("Duties", null, duties);
            insertVal1 = database.insert("Amends", null, amends);
            insertVal1 = database.insert("Link_Duty_Amend", null, Link_Duty_Amend);
            insertVal1 = database.insert("Link_Duty_Roommate", null, Link_Duty_Roommate);
            insertVal1 = database.insert("Tasks_Assigned", null, Tasks_Assigned);
        }catch (Exception e){
            e.printStackTrace();
            this.close();
        }finally {
            this.close();
        }
        System.out.println("Default Row inserted!");
    }
}
