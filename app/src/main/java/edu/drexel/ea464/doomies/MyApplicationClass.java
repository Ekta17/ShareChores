package edu.drexel.ea464.doomies;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ekta on 5/24/2016.
 */
public class MyApplicationClass extends Application {

   public Boolean DB_CREATION_SUCCESS;
   public ArrayList<String> roomNames=new ArrayList<String>();

    public ArrayList<String> roommatesList=new ArrayList<String>();
    public ArrayList<String> roomDutiesList=new ArrayList<String>();
    public ArrayList<String> roomAmendsList=new ArrayList<String>();

    public UserBean user=new UserBean();
    public String selectedRoomName="";
    public String selectedRoomDescription="";

   public HashMap<String,String> roomAndRoomDesc=new HashMap<String,String>();
    public HashMap<String,String> roommateNameAndEmail=new HashMap<String,String>();

    public HashMap<String,ArrayList<String>> roommate_duty_hashMap=new HashMap<String,ArrayList<String>>();
    public HashMap<String,String> duty_amend_hashMap=new HashMap<String,String>();
    public HashMap<String,String> duty_timelines_hashMap=new HashMap<String,String>();
    public HashMap<String,Integer> duty_status_hashMap=new HashMap<String,Integer>();

}
