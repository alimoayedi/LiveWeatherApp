package amastudio.ali.liveweather.overNetServices;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import amastudio.ali.liveweather.FavoriteCitiesActivity;
import amastudio.ali.liveweather.ParseFunctions;
import amastudio.ali.liveweather.R;

/**
 * Created by ali on 04/08/2018.
 */

public class FetchWeather {

    private LinkedHashMap<String,String> locations = new LinkedHashMap<String, String>();   //list of locations
    private String tempUnit;    //temperature
    private FavoriteCitiesActivity favoriteCitiesActivity;      //activity to be updated after fetch
    private ProgressBar loadingBar;

    final Handler delayOnRequest = new Handler();

    // constructor
    public FetchWeather(FavoriteCitiesActivity calledActivity, LinkedHashMap listOfLocations, String tempUnit){
        this.favoriteCitiesActivity = calledActivity;
        this.locations = listOfLocations;
        this.tempUnit = tempUnit;
        gettingWeatherInfo();
    }

    // setting URL
    private String setURL(String woeid, String unit){       //defines url
        String firstSec = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20%3D%20%22";
        String midSec = "%22%20and%20u%3D%22";
        String lastSec = "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        return firstSec + woeid + midSec + unit + lastSec;
    }

    private void gettingWeatherInfo(){

        Iterator iterator = locations.entrySet().iterator();

        while (iterator.hasNext()){     //gets weather one by one
            final Map.Entry pair = (Map.Entry) iterator.next();
            delayOnRequest.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new RetrieveFromNet().execute(pair.getKey().toString());
                }
            }, 500);
        }

        delayOnRequest.postDelayed(new Runnable() {
            @Override
            public void run() {     //enables sync button on the main activity after 4000ms, avoids consequent requests
                favoriteCitiesActivity.enableSyncBtn();
            }
        }, 4000);
    }

    // fetching weather
    private class RetrieveFromNet extends AsyncTask<String , Void, JSONObject> {
        @Override
        protected JSONObject doInBackground (String... woeid) {

            String url_string = setURL(woeid[0], tempUnit);
            @Nullable HttpURLConnection connection = null;
            @Nullable BufferedReader bufferedReader = null;

                /* Retrieving data from internet */
            try {
                URL url = new URL(url_string);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                return new JSONObject(stringBuffer.toString());
            } catch (MalformedURLException e) {
                Toast.makeText(favoriteCitiesActivity.getApplicationContext(), "Error in connection to server.", Toast.LENGTH_SHORT).show();
                return null;
            } catch (IOException e) {
                Toast.makeText(favoriteCitiesActivity.getApplicationContext(), "Failed to connect to server. Check your connection and try.", Toast.LENGTH_SHORT).show();
                return null;
            } catch (JSONException e) {
                Toast.makeText(favoriteCitiesActivity.getApplicationContext(), "Error in data format.", Toast.LENGTH_SHORT).show();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    Toast.makeText(favoriteCitiesActivity.getApplicationContext(), "Error in formatted data", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onPostExecute(JSONObject fetchedData){       //after execution updates main activity
            super.onPostExecute(fetchedData);
            ParseFunctions parseFunctions = new ParseFunctions();
            favoriteCitiesActivity.setupInterface(parseFunctions.parseWeatherFromNet(fetchedData));     //updates interface in the main activity
            loadingBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {     //before execution shows loading bar
            loadingBar = favoriteCitiesActivity.findViewById(R.id.loadingBar);
            loadingBar.setVisibility(View.VISIBLE);
        }

    }
}
