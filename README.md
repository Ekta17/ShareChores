ShareChores

This is an Android app which helps the people staying in a shared accommodation to efficiently shared the household chores among themselves.

Build Instructions:

1) To Build and test the application, please download the project as a zip file.

2) Extract the zip file into an appropriate folder.

3) To run the application any IDE can be used among Android Studio and Eclipse. Both these IDEs are open source and can be easily downloaded from their following official website: a. Eclipse: https://www.eclipse.org/downloads/ Also, it is required to install Android plugins in Eclipse to run Android Applications. The steps to install Android plugin in Eclipse can be found on the following link: http://developer.android.com/sdk/installing/installing-adt.html b. Android Studio: http://developer.android.com/sdk/index.html

4) After installation of the IDE, import the Android Application into the workspace using the instructions: Eclipse: I. Go to File II. Import III. Import an existing Android code into Workspace IV. In the Root Directory, browse the extracted code of the android application and select the 'ShareChores' folder. V. Click 'Finish'

Android Studio: 
I. Choose the option Import project in the first screen on the start of Android Studio. II. Browse and provide the location of 'ShareChores' folder and click OK.

If you face any difficulties, please refer to the instructions given in the following website:
Eclipse: http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Ftasks%2Ftasks-importproject.htm

Android Studio: 
http://developer.android.com/sdk/installing/migrate.html#migrate

5) To Run the Application, follow the following instructions: Eclipse: I. Right click on the application folder and select 'Run As'. II. For the list options available in Run configuration, select 'Android Application'. III. Now connect the android phone and use that as a target to run the application. IV. In step 3 we can also you Android Emulator device to run the application but it is not recommended. To read about Android Emulator, please follow the following link: http://developer.android.com/tools/devices/emulator.html

Android Studio:
I. Go to 'Run' and select the option 'Run 'app''. II. Now you will be prompted to choose the device to run the application. You can choose between running the application on actual android device or on android emulator. III. To run the application on your phone, keep the phone connected to the computer using USB cable and activate the device for use. To set up an Android Emulator, please follow the instructions given in the following link: http://developer.android.com/tools/devices/index.html Please note: While creating Emulator, please choose the following options for the application to work successfully: API level: 23

6) We can also generate a .apk file for our android application and then this .apk file can be used to run the application on android devices which satisfy the minimum sdk requirement. For this application, minimum sdk requirement are: Android Version: 17, Android Jelly Bean

To generate .apk file, please follow the instructions given on the following link:
    http://developer.android.com/tools/publishing/app-signing.html

There is a apk file named 'app-debug.apk' has been provided with the project code.
Working with app:

1) Download the app.

2) Register with the app and then login into the application. (There is still work in progress with the database of this application. So currently, the user can login into the application by clicking on the 'Login' button on the Entry page of the application.)

3) After successful login, the user will see three buttons on the screen, 'Create Room' button to create a new room, 'View Existing Rooms' button to view already existing rooms and 'Logout' button to logout of the application.

4) If the user clicks on 'Create Room' button, the user is redirected to create room page where the user can provide the name and description of the room. Name of the room is compulsory, if the users misses to specify the name of the room, the app throws an error and does not allow the user to proceed further. The user also has an option to cancel the room creation action anytime during this activity by clicking on 'Cancel' button. To create a room, the user needs to click on 'Create' button.

5) If the user clicks on the 'View Existing Rooms' button, the list displaying the pre-existing rooms pops up and the user can select the room of his/her choice. If there are no rooms created so far, clicking on 'View Existing Rooms' button will display the message saying 'No room present. Please create a room first.'.

6) When the user selects a pre-existing room, the user is redirected to the room page of the selected room, where the user has the option of working with following features:

i) My Tasks: This functionality lets the user view the tasks assigned to the user with due dates of the tasks. There are two buttons, 'Ok' which redirects the user back to 'My Room' page, 'Add Log' button which lets the user to update the data related to the duties completed. (Currently, the Add Log functionality is still under development and is not linked to any action.)

ii) Logs: This functionality lets the user to view the logs of the roommates and this functionality is under development and clicking on the button is not linked with any action.

iii) Room Duties: First time when user clicks on this room duties button and if no duties are currently linked to this room then a message saying 'Room does not contain any duty. Please add a duty' is displayed for the user. If the duties are present in the room, then list of duties appear on this screen. The user can delete a duty by long pressing a particular duty on the list, this will pop up a message if the user wants to delete the duty and when the user confirms the duty is removed from the list. If the user single clicks on a duty, a pop appears on the screen displaying the linkage of the duty with amends and roommates it is assigned to. The user also has an option to add a new duty to the room by clicking on 'ADD DUTY' button on this screen, where the user can enter the compulsory name of the duty with optional description of the duty, also if the room has a list of amends then the duty can be linked to a pre-existing amend from the list and also in the way the duty can be assigned to the pre-existing roommates in the room. On click of 'Ok', a notification is sent to all the roommates present in the room for their approval to add the duty. If everyone agrees to add the duty, then that duty will appear in the list of duties on the 'Room Duties' screen. The user can wish to cancel the duty creation by clicking on 'Cancel' button. (Currently, due to no database connectivity, the app does not send notifications to the roommates on duty creation or deletion and the duty is added to or removed from the screen directly.)

iv) Room Amends: First time when user clicks on this room amends button and if no amends are currently linked to this room then a message saying 'Room does not contain any amends. Please add an amend' is displayed for the user. If the amends are present in the room, then list of amends appear on this screen. The user can delete an amend by long pressing a particular amend on the list, this will pop up a message if the user wants to delete the amend and when the user confirms the amend is removed from the list. If the user single clicks on an amend, a pop appears on the screen displaying the linkage of the amend with duties. The user also has an option to add a new amend to the room by clicking on 'ADD AMEND' button on this screen, where the user can enter the compulsory name of the amend with optional description of the amend, also if the room has a list of duties then the amend can be linked to a pre-existing duty from the list. On click of 'Ok', a notification is sent to all the roommates present in the room for their approval to add the amend. If everyone agrees to add the amend then that amend will appear in the list of amends on the 'Room Amends' screen. The user can wish to cancel the amend creation by clicking on 'Cancel' button. (Currently, due to no database connectivity, the app does not send notifications to the roommates on amend creation or deletion and the amend is added to or removed from the screen directly.)

v) View Roommates: First time when user clicks on this view roommates button and if no roommates are currently linked to this room then a list displaying the user currently logged into the app is shown in the list of roommates of this room. If the roommates are present in the room, then list of roommates appear on this screen. The user can delete a roommate by long pressing a particular roommate on the list, this will pop up a message if the user wants to delete the roommate and when the user confirms, an approval message is sent to the roommate the user wants to delete and on successful confirmation, the roommate is removed from the list. If the user single clicks on a roommate, a pop appears on the screen displaying the linkage of the roommate with duties. The user also has an option to add a new roommate to the room by clicking on 'ADD ROOMMATE' button on this screen, where the user can enter the compulsory name and email of the roommate, also if the room has a list of duties then the roommate can be linked to a pre-existing duty from the list. On click of 'Ok', an invitation is sent to the newly added roommate, and once the roommate confirms he/she is added to the roommates list of the user. The user can wish to cancel the roommate creation by clicking on 'Cancel' button. (Currently, due to no database connectivity, the app does not send invitation to the newly added roommate on roommate creation and also if the roommate is deleted a notification message is not sent to the concerned roommates.)
