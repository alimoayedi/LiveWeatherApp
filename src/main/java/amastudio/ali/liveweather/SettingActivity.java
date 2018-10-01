package amastudio.ali.liveweather;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedHashMap;

import amastudio.ali.liveweather.alarmService.AlarmReceiver;
import amastudio.ali.liveweather.onDeviceFileServices.ExternalFileManagement;

public class SettingActivity extends AppCompatActivity {

    //Define XML items
    private Button doneBtn;
    private RadioGroup tempRadioGrup;
    private RadioButton fahrenheit;
    private RadioButton celsius;
    private Switch setNotificationSwitch;
    private FloatingActionButton aboutFAB;

    //variables
    private String unit;    //keeps unit
    private boolean settingChanged = false; //variable to keeps in mind if any setting changed or not

    //notification intent
    private PendingIntent pendingIntent;

    //define classes
    ExternalFileManagement externalFileManagement = new ExternalFileManagement();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_layout);

        //assign XML items
        doneBtn = (Button) findViewById(R.id.doneButton);
        tempRadioGrup = (RadioGroup) findViewById(R.id.tempRadioGrup);
        fahrenheit = (RadioButton) findViewById(R.id.fahrenheit);
        celsius = (RadioButton) findViewById(R.id.celsius);
        setNotificationSwitch = (Switch) findViewById(R.id.setNotificationSwitch);
        aboutFAB = (FloatingActionButton) findViewById(R.id.aboutFAB);

        final Intent returnIntent = new Intent();

        //alarm activity
        Intent alarmIntent = new Intent(SettingActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, 0, alarmIntent, 0);

        //initialize activity
        initialize();

        //temperature buttons control
        tempRadioGrup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.fahrenheit:   //fahrenheit checked!
                        unit = fahrenheit.getTag().toString();  //updates unit variable string
                        settingChanged = true;  //keeps in mind setting changed
                        break;
                    case R.id.celsius:  //celsius checked!
                        unit = celsius.getTag().toString(); //updates unit variable string
                        settingChanged = true;  // keeps in mind something changed
                        break;
                }
            }
        });

        setNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingChanged = true;  //keeps in mind a setting changed
                if (isChecked) {    //if notification is on
                    turnOnNotification();
                    Toast.makeText(SettingActivity.this, "Notification turned on.", Toast.LENGTH_SHORT).show();
                } else { //if notification turns off
                    cancelNotification();
                    Toast.makeText(SettingActivity.this, "Notification turned off.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingChanged) {
                    updateSetting();
                    returnIntent.putExtra("unit", unit);    //sends unit back to the main activity
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    setResult(Activity.RESULT_CANCELED);    //returns cancel to the main activity, nothing changed.
                    finish();
                }
            }
        });

        aboutFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutFAB.setEnabled(false); //makes button disable till called activity being close. (avoids double tap!)
                Intent aboutActivity = new Intent(SettingActivity.this, AboutActivity.class); //define about activity
                startActivityForResult(aboutActivity, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        aboutFAB.setEnabled(true);  //makes button enabled again
    }

    private void initialize() {
        final String settingDirectory = this.getFilesDir().getPath().toString();    //directory where settings are saved
        String retrievedInfo = externalFileManagement.readFile(settingDirectory + "/setting.json"); //read setting
        if (retrievedInfo != null) {    //if any saved setting found
            try {
                JSONObject savedJSON = new JSONObject(retrievedInfo);
                unit = savedJSON.get("unit").toString();    // sets saved temp unit
                setNotificationSwitch.setChecked(savedJSON.get("notification").equals("1")? true : false);  //checks if notification is on or off
            } catch (JSONException e) {
                e.printStackTrace();    //if file couldn't parse as JSON file
            }
        } else {    //no setting found.
            unit = "f"; // default value for temp
            setNotificationSwitch.setChecked(false);
        }

        if (unit.equals("f")) {     //updates interface checked temperature
            tempRadioGrup.check(R.id.fahrenheit);
        } else {
            tempRadioGrup.check(R.id.celsius);
        }
    }

    private void updateSetting() {      //saved changed setting
        final String settingDirectory = this.getFilesDir().getPath().toString();
        LinkedHashMap<String, String > setting = new LinkedHashMap<>();
        setting.put("unit", unit);
        setting.put("notification", setNotificationSwitch.isChecked() ? "1" : "0");
        externalFileManagement.saveSetting(setting,settingDirectory + "/setting.json");     //write changed setting in file
    }

    private void turnOnNotification(){
        AlarmManager notificationAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,6);       //time for notification
        calendar.set(Calendar.MINUTE, 30);

        notificationAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void cancelNotification() {
        AlarmManager notificationAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        notificationAlarmManager.cancel(pendingIntent);
    }

    @Override
    public void onBackPressed() {       //physical back button pressed!
        if (settingChanged) {
            doneBtn.performClick();
        }
    }
}
