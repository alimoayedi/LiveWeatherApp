package amastudio.ali.liveweather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Callable;

import amastudio.ali.liveweather.overNetServices.CheckInternetAccess;
import amastudio.ali.liveweather.overNetServices.GetYahooLogo;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView yahooLogo;
    private TextView versionText;
    private AlertDialog.Builder connectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        yahooLogo = (ImageView) findViewById(R.id.yahooLogo);
        connectionDialog = new AlertDialog.Builder(this);       //connection error dialog
        connectionDialog.setMessage("No Internet access. Please check your connection.").setCancelable(false);
        connectionDialog.setPositiveButton("Try again!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkConnectivity(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        setup();    //if connected initialize logos
                        return null;
                    }
                });
            }
        });
        setVersionText();       //set version text
        checkConnectivity(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                setup();
                return null;
            }
        });

    }

    private void checkConnectivity(final Callable<Void> method){
        final CheckInternetAccess checkInternetAccess = new CheckInternetAccess();      //check connection
        final Handler waiting = new Handler();
        checkInternetAccess.CheckInternetAccess();
        waiting.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkInternetAccess.getConnectionStatus()){     //check if connection was successful
                    try {
                        method.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    connectionDialog.show();
                }
            }
        },1850);        //waits 1850 ms before checking if connection was available or not
    }

    private void setup() {      //setup interface (set textview texts, version number, yahoo logo)
        new GetYahooLogo().getYahooLogo(yahooLogo);
        final Handler waiting = new Handler();
        waiting.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity = new Intent(WelcomeActivity.this, FavoriteCitiesActivity.class);
                startActivity(mainActivity);
                finish();
            }
        }, 3000);   //waits 3000 ms before going to main activity

    }

    private void setVersionText() {     //gets version and set ups version's text field
        versionText = (TextView) findViewById(R.id.versionText);
        String version = "";
        try {
            version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionText.setText(versionText.getText().toString() + version);
    }

    @Override
    public void onBackPressed() {       //physical back button pressed
    }
}
