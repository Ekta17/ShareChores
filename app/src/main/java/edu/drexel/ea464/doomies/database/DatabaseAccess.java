package edu.drexel.ea464.doomies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.drexel.ea464.doomies.AmendBean;
import edu.drexel.ea464.doomies.DutyBean;
import edu.drexel.ea464.doomies.RoomBean;
import edu.drexel.ea464.doomies.TaskBean;
import edu.drexel.ea464.doomies.UserBean;

/**
 * Created by Ekta on 1/22/2017.
 */

public class DatabaseAccess {

    private static Connection connection = null;
    private static ResultSet resultSet = null;
    private static PreparedStatement statement = null;

    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {

    }

    /** Return a singleton instance of DatabaseAccess.
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public List<String> getUserIds(){
        List<String> list = new ArrayList<>();
        String getUserIds="SELECT name FROM userdetails;";
        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getUserIds);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            try {
                while(resultSet.next()){
                  list.add(resultSet.getString(0));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    //Creating a new user account
    public boolean createNewUser(String userName, String password, String email, String phone) {

       int rowInserted=0;
       String insertNewUser="Insert into UserDetails (NAME,PASSWORD,EMAIL,PHONE) values ('"+userName+"','"+password+"','"+email+"','"+phone+"');";

       try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(insertNewUser);
            rowInserted=statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rowInserted>0)
            return true;
        else
            return false;
    }

    //Verify is the user is already exists using email
    public boolean checkIfUserEmailExists(String email) {
        String getUserEmail = "SELECT email FROM UserDetails WHERE email='"+email+"'";

        boolean result = false;

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getUserEmail);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
        }
        try {
            if (resultSet.next()) {
                result = true;
            } else {
                result = false;
            }
        }catch(SQLException  e){
            e.getMessage();
        }
        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Verify and return if valid user
    public UserBean verifyAndGetUser(String email, String password) {
        String getUserDetails = "SELECT name,email,phone FROM UserDetails WHERE email='"+email+"' and password='"+password+"'";

        UserBean user=null;

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getUserDetails);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            try {
                while(resultSet.next()){
                    user=new UserBean();
                    user.setName(resultSet.getString(0));
                    user.setEmail(resultSet.getString(1));
                    user.setPhone(resultSet.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else
            user=null;

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public ArrayList<RoomBean> getLinkedRooms(String userEmail){

        ArrayList<RoomBean> roomsLinked=null;
        RoomBean roomBean=null;

        String getRoomDetails="select roomName,roomDescription,email from Room where email='"+userEmail+"'";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getRoomDetails);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            roomsLinked=new ArrayList<>();
            try {
                do {
                    roomBean = new RoomBean();
                    roomBean.setName(resultSet.getString(0));
                    roomBean.setDescription(resultSet.getString(1));
                    roomBean.setUserEmail(resultSet.getString(2));
                    roomsLinked.add(roomBean);
                } while (resultSet.next());
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomsLinked;
    }

    public boolean checkIfRoomAlreadyExists(String roomName, String userEmail){

        String checkIfRoomExists="Select roomName from Room where roomName='"+roomName+"' and email='"+userEmail+"'";
        Cursor cursor = null;
        boolean result = false;

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(checkIfRoomExists);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            result=true;
        }else{
            result=false;
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public  ArrayList<TaskBean> getTasksList(String userLoggedIn, String selectedRoom){

        ArrayList<TaskBean> tasksAssigned=null;
        TaskBean task;

        String getTasksDetails="select dutyName,amendName,dutyDueDate,dutyCompletionDate,amendDueDate,amendCompletionDate from Tasks_Assigned where userEmail='"+userLoggedIn+"' and roomName='"+selectedRoom+"'   ";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getTasksDetails);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            try {
                tasksAssigned = new ArrayList<>();
                do {
                    task = new TaskBean();
                    task.setUserEmail(userLoggedIn);
                    task.setRoomName(selectedRoom);
                    task.setDutyName(resultSet.getString(0));
                    task.setAmendName(resultSet.getString(1));
                    task.setDutyDueDate(resultSet.getString(2));
                    task.setDutyCompletionDate(resultSet.getString(3));
                    task.setAmendDueDate(resultSet.getString(4));
                    task.setAmendCompletionDate(resultSet.getString(5));

                    tasksAssigned.add(task);

                } while (resultSet.next());
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasksAssigned;

    }

    public String getDueDateOfTask(String selectedTask,String userLoggedIn,String selectedRoom){
        String dueDate=null;

        String getDueDate="select dutyDueDate from Tasks_Assigned where dutyName='"+selectedTask+"' and userEmail='"+userLoggedIn+"' and roomName='"+selectedRoom+"'";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getDueDate);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            try {
                dueDate = resultSet.getString(0);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dueDate;
    }

    public ArrayList<DutyBean> getRoomDuties(String selectedRoom){
        ArrayList<DutyBean> roomDuties=null;
        DutyBean dutyBean=null;

        String getRoomDuties="select dutyName,dutyDescription from Duties where roomName='"+selectedRoom+"'";
        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getRoomDuties);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            try {
                roomDuties=new ArrayList<>();
                do{
                    dutyBean=new DutyBean();
                    dutyBean.setRoomName(selectedRoom);
                    dutyBean.setDutyName(resultSet.getString(0));
                    dutyBean.setDutyDescription(resultSet.getString(1));
                    roomDuties.add(dutyBean);
                }while(resultSet.next());
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomDuties;
    }

    public ArrayList<String> getRoommates(String selectedRoom){

        ArrayList<String> roommatesList=new ArrayList<>();

        String getRoommates="select u.name from Room r, UserDetails u where r.roomName='"+selectedRoom+"' and  r.email = u.email";
        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getRoommates);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            try {
                do{
                    roommatesList.add(resultSet.getString(0));
                }while(resultSet.next());
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roommatesList;
    }

    public ArrayList<AmendBean> getRoomAmends(String selectedRoom){
        ArrayList<AmendBean> roomAmends=null;
        AmendBean amendBean=null;

        String getRoomAmends="select amendName,amendDescription from Amends where roomName='"+selectedRoom+"'";
        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(getRoomAmends);
            resultSet = statement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if(resultSet!=null){
            try {
                roomAmends=new ArrayList<>();
                do{
                    amendBean=new AmendBean();
                    amendBean.setRoomName(selectedRoom);
                    amendBean.setAmendName(resultSet.getString(0));
                    amendBean.setAmendDescription(resultSet.getString(1));
                    roomAmends.add(amendBean);
                }while(resultSet.next());
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomAmends;
    }

    public boolean linkDutyWithAmend(String selectedRoom, String amendName, String dutyName){

        int rowInserted=0;
        String insertDutyWithAmend="Insert into Link_Duty_Amend (dutyName,amendName,roomName) values ('"+dutyName+"','"+amendName+"','"+selectedRoom+"');";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(insertDutyWithAmend);
            rowInserted=statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rowInserted>0)
            return true;
        else
            return false;
    }

    public boolean linkDutyWithRoommate(String selectedRoom,String dutyName,String selectedRoommate){

        int rowInserted=0;
        String insertDutyWithRoommate="Insert into Link_Duty_Roommate (dutyName,roomName,userEmail) values ('"+dutyName+"','"+selectedRoom+"','"+selectedRoommate+"');";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(insertDutyWithRoommate);
            rowInserted=statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rowInserted>0)
            return true;
        else
            return false;
    }

    public boolean addNewDutyToRoom(String selectedRoom,String name,String description){
        int rowInserted=0;
        String insertDutyInRoom="Insert into Duties (dutyName,dutyDescription,roomName) values ('"+name+"','"+description+"','"+selectedRoom+"');";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(insertDutyInRoom);
            rowInserted=statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rowInserted>0)
            return true;
        else
            return false;
    }

    public boolean addNewAmendToRoom(String selectedRoom,String name,String description){
        int rowInserted=0;
        String insertAmendInRoom="Insert into Amends (amendName,amendDescription,roomName) values ('"+name+"','"+description+"','"+selectedRoom+"');";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(insertAmendInRoom);
            rowInserted=statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rowInserted>0)
            return true;
        else
            return false;
    }

    public boolean addNewRoommate(String selectedRoom,String name,String email){
        int rowInserted=0;
        //Check is user with name and email exists in the system
        //if not then send back an error msg saying the roommates should be a part of the system first.
        //If yes then add the email and roomName to Room with the desciption of the room

        boolean flag_add_user=createNewUser(name,"1",email,"12345");
        if(flag_add_user) {
            //Add roommate to Room
            String insertRoommate="Insert into Room (email,roomName) values ('"+email+"','"+selectedRoom+"');";
            try {
                connection = ConnectionClass.getConnection();
                statement = connection.prepareStatement(insertRoommate);
                rowInserted=statement.executeUpdate();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(rowInserted>0)
                return true;
            else
                return false;
        }
        return false;
    }

    public boolean addNewRoom(String roomName,String roomDescription,String loggedInUser){
        int rowInserted=0;

        String insertRoom="Insert into Room (roomName,roomDescription,email) values ('"+roomName+"','"+roomDescription+"','"+loggedInUser+"');";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(insertRoom);
            rowInserted=statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rowInserted>0)
            return true;
        else
            return false;
    }

    public void insertDeafultRow(String selectedRoom, String userLoggedIn){

        String dutyName="duty3";
        String amendName="amend3";

        String insertDuty="Insert into Duties (dutyName,dutyDescription,roomName) values ('"+dutyName+"','Duty1_description','"+selectedRoom+"'); ";
        String insertAmend="Insert into Amends (amendName,amendDescription,roomName) values ('"+amendName+"','amend1_description','"+selectedRoom+"')";
        String linkDutyToAmend="Insert into Link_Duty_Amend (dutyName,amendName,roomName) values ('"+dutyName+"','"+amendName+"','"+selectedRoom+"')";
        String linkDutyToRoommate="Insert into Link_Duty_Roommate (dutyName,userEmail,roomName) values ('"+dutyName+"','"+userLoggedIn+"','"+selectedRoom+"')";
        String assignTask="Insert into Tasks_Assigned (dutyName,amendName,userEmail,roomName,dutyDueDate,dutyCompletionDate,amendDueDate,amendCompletionDate) " +
                "values ('"+dutyName+"','"+amendName+"','"+userLoggedIn+"','"+selectedRoom+"','2019,02,03','2019,02,03','2019,02,03','2019,02,03')";

        try {
            connection = ConnectionClass.getConnection();
            statement = connection.prepareStatement(insertDuty);
            statement = connection.prepareStatement(insertAmend);
            statement = connection.prepareStatement(linkDutyToAmend);
            statement = connection.prepareStatement(linkDutyToRoommate);
            statement = connection.prepareStatement(assignTask);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Default Row inserted!");
    }
}
