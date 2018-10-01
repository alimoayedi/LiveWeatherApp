package amastudio.ali.liveweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import amastudio.ali.liveweather.onDeviceFileServices.ExternalFileManagement;
import amastudio.ali.liveweather.overNetServices.CheckInternetAccess;
import amastudio.ali.liveweather.overNetServices.FetchWeather;
import amastudio.ali.liveweather.overNetServices.GetYahooLogo;

public class FavoriteCitiesActivity extends AppCompatActivity {

    // define variables
    ArrayList<HashMap<String, HashMap<String, String>>> listToAdaptor = new ArrayList<HashMap<String, HashMap<String,String>>>(); //list of favorite cities passed to adapter
    private LinkedHashMap<String, String> favoriteLocations = new LinkedHashMap<String, String>(); // Structure: <woeid, name>, woeids are unique and both string ,  keeps locations in order
    private String tempUnit = "f"; // Default is Fahrenheit
    private boolean loadYahooLogo = true;   //checks if yahoo logo loaded or not (in case of no connection at start up!)
    private int lastExpandedPosition = -1;  //keeps index of last expanded item
    private int codeOfPendingTask = -1; // keeps index of pending task in case of no connection, "-1": no task, "1": sync task, "2": addCityActivity

    // initialize methods
    ExternalFileManagement externalFileManagement = new ExternalFileManagement();
    ParseFunctions parseFunctions = new ParseFunctions();
    CheckInternetAccess checkInternetAccess = new CheckInternetAccess();

    // define xml items
    private TextView emptyListText;
    private ExpandableListView favoriteCitiesListView;
    private FloatingActionButton fabSetting, fabAddLocation, fabSync;
    private ImageView yahooLogo;
    private AlertDialog.Builder connectionDialog, removeItemDialog;
    private ProgressBar waitingForConnectionBar;

    // listView Adapter
    ExpandableListAdapter listViewAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_cities_layout);

        final String saveDirectory = this.getFilesDir().getPath().toString();   // directory where list of locations and setting are saved

        yahooLogo = (ImageView) findViewById(R.id.yahooLogo);

        emptyListText = (TextView) findViewById(R.id.emptyListText);
        favoriteCitiesListView = (ExpandableListView) findViewById(R.id.favoriteCitiesListView);
        fabSetting = (FloatingActionButton) findViewById(R.id.fabSetting);
        fabAddLocation = (FloatingActionButton) findViewById(R.id.fabAddLocation);
        fabSync = (FloatingActionButton) findViewById(R.id.fabSync);
        waitingForConnectionBar = (ProgressBar) findViewById(R.id.loadingBar);

        listViewAdaptor = new ExpandableListAdapter(this, listToAdaptor, tempUnit);  //favoriteLocations is send to adapter to

        favoriteCitiesListView.setEmptyView(emptyListText);     //if list of favorite locations be empty, this hint will appear

        connectionDialog = new AlertDialog.Builder(this);   // dialog box to notify of having no connection
        connectionDialog.setMessage("No Internet access. Please check your connection.").setCancelable(false);
        connectionDialog.setPositiveButton("Try again!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkConnectivity();
            }
        });

        setLoadYahooLogo();     //loads yahoo logo on the main activity
        initialSetup();     //loads list of favorite cities if any

        favoriteCitiesListView.setAdapter(listViewAdaptor);

        favoriteCitiesListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(final ExpandableListView expandableListView, View view, final int groupId, long l) {
                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int expandedItem) {
                        if (lastExpandedPosition != -1 && lastExpandedPosition != expandedItem) { //if the item is expanded, collapses it
                            favoriteCitiesListView.collapseGroup(lastExpandedPosition);
                        }
                        lastExpandedPosition = expandedItem;    //saves index of last expanded item
                    }
                });
                return false;
            }
        });

        favoriteCitiesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int position, long l) {      //long click asks if item should be deleted
                favoriteCitiesListView.setEnabled(false);   //disables to avoid expansion
                removeItemDialog = new AlertDialog.Builder(FavoriteCitiesActivity.this);    //asks to confirmation
                //setups dialog
                removeItemDialog.setMessage("Do you wish to remove " + favoriteLocations.get(favoriteLocations.keySet().toArray()[position].toString()) + " from list?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favoriteCitiesListView.setEnabled(true);    //makes list enabled again
                        favoriteLocations.remove(favoriteLocations.keySet().toArray()[position].toString());// favoriteLocations' structure: <woeid, name> both string
                        listViewAdaptor.remove(position);   //removes item
                        externalFileManagement.writeFile(favoriteLocations, saveDirectory + "/locations.json");
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favoriteCitiesListView.setEnabled(true);    //makes list enabled again
                    }
                }).show();
                return false;
            }
        });

        fabSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableBtns(false);      //disables other buttons
                fabSync.setEnabled(false);      //sync button disabled

                Intent setting = new Intent(FavoriteCitiesActivity.this, SettingActivity.class);    //define setting intent
                /*
                Iterator iterator = favoriteLocations.entrySet().iterator();    //pass favorite locations for getting notification, may used later!! ;)
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    setting.putExtra(pair.getKey().toString(), pair.getValue().toString());
                }
                */
                favoriteCitiesListView.collapseGroup(lastExpandedPosition);     //collapse any expanded item to avoid later errors on reload!
                startActivityForResult(setting, 1);     //starts activity
            }
        });

        fabAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableBtns(false);      //disables other buttons
                fabSync.setEnabled(false);      //disables sync button

                checkInternetAccess.CheckInternetAccess();      //checks internet activity
                waitingForConnectionBar.setVisibility(View.VISIBLE);    //shows waiting bar
                final Handler waiting = new Handler();
                waiting.postDelayed(new Runnable() {    //waits for 1850 ms before checking if connection was successful or not
                    @Override
                    public void run() {
                        waitingForConnectionBar.setVisibility(View.GONE);
                        if (checkInternetAccess.getConnectionStatus()){     //if successful
                            openAddCityActivity();
                        } else {                                            //if connection failed
                            codeOfPendingTask = 2;      //saves waiting activity (addLocation code is 2)
                            connectionDialog.show();
                        }
                    }
                },1850);
            }
        });

        fabSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteLocations.size() != 0) {
                    fabSync.setEnabled(false);      //disables sync button
                    favoriteCitiesListView.collapseGroup(lastExpandedPosition);     //collapse any expanded item
                    listToAdaptor.clear();      //clears adapter
                    getWeather(null);       //gets weather without any key(fetch weather for all locations)
                }
            }
        });

        yahooLogo.setOnClickListener(new View.OnClickListener() {       //if yahoo logo tapped
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.yahoo.com/?ilc=401"); // missing 'http://' will cause crash
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void checkConnectivity(){
        checkInternetAccess.CheckInternetAccess();      //checks connectivity
        waitingForConnectionBar.setVisibility(View.VISIBLE);        //shows waiting bar
        final Handler waiting = new Handler();
        waiting.postDelayed(new Runnable() {
            @Override
            public void run() {
                waitingForConnectionBar.setVisibility(View.GONE);
                if (checkInternetAccess.getConnectionStatus()){     //check if connected or not
                    switch (codeOfPendingTask){     //switches on code of pending activity
                        case 1:
                            getWeather(null);
                            codeOfPendingTask = -1;     //sets pending activity to -1
                            break;
                        case 2:
                            openAddCityActivity();
                            codeOfPendingTask = -1;     //sets pending activity to -1
                            break;
                        default:
                            break;
                    }
                } else {
                    enableBtns(false);      //if connection failed all buttons get unable
                    fabSync.setEnabled(false);
                    connectionDialog.show();
                }
            }
        },1850);
    }

    private void initialSetup(){
        fabSync.setEnabled(false);      //initially sync button disabled to avoid double load on slow connections
        final String saveDirectory = this.getFilesDir().getPath().toString();

        String savedSetting = externalFileManagement.readFile(saveDirectory + "/setting.json");     //loads setting
        String savedData = externalFileManagement.readFile(saveDirectory + "/locations.json");      //loads locations

        tempUnit = parseFunctions.parseSavedSetting(savedSetting);      //parse setting and get temperature
        if (savedData != null && !savedData.equals("[]")) {
            favoriteLocations = ParseFunctions.parseSavedFile(savedData);   // favoriteLocations' structure: <woeid, name>, woeids are unique.
            getWeather(null);       //get weather for all locations
        }
    }

    private void getWeather(final String key){
        checkInternetAccess.CheckInternetAccess();      //checks connection
        waitingForConnectionBar.setVisibility(View.VISIBLE);
        final Handler waiting = new Handler();
        waiting.postDelayed(new Runnable() {
            @Override
            public void run() {
                waitingForConnectionBar.setVisibility(View.GONE);
                if (checkInternetAccess.getConnectionStatus()){
                    if (key != null) {      //key null means load weather for all locations
                        final LinkedHashMap<String,String> newAddedLocation = new LinkedHashMap<String, String>(){{put(key, favoriteLocations.get(key));}};
                        new FetchWeather(FavoriteCitiesActivity.this, newAddedLocation, tempUnit);
                    } else {        //loads weather for only key locations
                        new FetchWeather(FavoriteCitiesActivity.this, favoriteLocations, tempUnit);
                    }
                    enableBtns(true);       //enables all button after load
                    setLoadYahooLogo();             //loads yahoo logo
                } else {
                    enableBtns(false);      // in case of no connection buttons remain disabled
                    fabSync.setEnabled(false);
                    codeOfPendingTask = 1;
                    connectionDialog.show();
                }
            }
        },1850);
    }

    public void setupInterface(HashMap<String, HashMap<String, String>> incomingData){      //whenever a weather fetched this function is called to update interface and list
        if (incomingData != null) {
            listToAdaptor.add(incomingData);
        } else {
            listToAdaptor.add(null);
        }
        listViewAdaptor.notifyDataSetChanged();
    }

    private void openAddCityActivity(){     //opens add location activity
        Intent addLocation = new Intent(FavoriteCitiesActivity.this, AddCityActivity.class);
        favoriteCitiesListView.collapseGroup(lastExpandedPosition);
        startActivityForResult(addLocation, 2);
    }

    private void addLocation(final String key, final String value) {
        if (favoriteLocations.get(key) == null) {       //checks if location already exits or not, if not added to the list, weather fetched and updated list will be saved.
            final String saveDirectory = this.getFilesDir().getPath().toString();
            favoriteLocations.put(key, value);
            externalFileManagement.writeFile(favoriteLocations, saveDirectory + "/locations.json");
            getWeather(key);
        } else {        //otherwise, location exists!
            Snackbar.make(this.findViewById(R.id.favoriteActivityLayout), "Your added location (" + value + ") already exists in list.", Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:     //setting activity
                if (resultCode == Activity.RESULT_OK) {
                    enableBtns(true);
                    tempUnit = data.getStringExtra("unit"); //updates temp unit
                    listViewAdaptor.updateTempUnit(tempUnit);   //updates adapter
                    listToAdaptor.clear();  //clears adapter
                    getWeather(null);   //fetch again!
                } else {
                    enableBtns(true);       //buttons enabled
                    fabSync.setEnabled(true);
                }
                break;
            case 2:     //add location activity - anyway all buttons are enabled
                if (resultCode == Activity.RESULT_OK) {
                    enableBtns(true);
                    fabSync.setEnabled(true);
                    addLocation(data.getStringArrayExtra("new")[0],data.getStringArrayExtra("new")[1]);     //fetch new location weather
                } else {
                    enableBtns(true);
                    fabSync.setEnabled(true);
                }
                break;
        }
    }

    public void enableSyncBtn() { fabSync.setEnabled(true); }       //this function to be called from other activity!

    private void enableBtns(boolean status) {
        fabSetting.setEnabled(status);
        fabAddLocation.setEnabled(status);
    }

    private void setLoadYahooLogo(){
        if (loadYahooLogo){     //if logo not loaded!
            new GetYahooLogo().getYahooLogo(yahooLogo);
            loadYahooLogo = false;
        }
    }

    @Override
    public void onBackPressed() {       //physical back button pressed!
        finish();
    }
}
