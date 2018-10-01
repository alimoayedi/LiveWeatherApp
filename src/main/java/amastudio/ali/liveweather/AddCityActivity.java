package amastudio.ali.liveweather;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddCityActivity extends AppCompatActivity {


    // Initialize other classes
    ManagingListOfCities managingListOfCities = new ManagingListOfCities();

    //Initialize variables
    private List<String> recommendedCities = new ArrayList<String>();

    // UI items
    private EditText citynameInput;
    private ListView recommendedCitiesListView;
    private Button cancelBtn;
    private ProgressBar fetchingDataBar;

    // Adapters
    private ArrayAdapter<String> recommendedCitiesAdaptor;

    // Intent
    final Intent returnIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_layout);

        // connecting views
        citynameInput = (EditText) findViewById(R.id.citynameInput);
        recommendedCitiesListView = (ListView) findViewById(R.id.recommendedCitiesListView);

        // setting up adaptors
        recommendedCitiesAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recommendedCities);

        // assigning adaptors
        recommendedCitiesListView.setAdapter(recommendedCitiesAdaptor);

        //setup buttons
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        fetchingDataBar = (ProgressBar) findViewById(R.id.fetchingDataBar);

        //listening for text change in editText on UI
        citynameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing to do
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {    //something must be already entered!
                    if (!Character.isWhitespace(charSequence.charAt(charSequence.length()-1))) {    //checks if last character is white space or not! white space crashes on API 19
                        new RetrieveFromNet().execute(charSequence.toString());
                    }
                } else {
                    recommendedCitiesAdaptor.clear();       //if all entered text cleared, list view also clears adapter
                    recommendedCitiesAdaptor.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //nothing to do
            }
        });

        //adding item to favorite list
        recommendedCitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int viewPosition, long rowId) {
                returnIntent.putExtra("new", managingListOfCities.addCityToFavorite(viewPosition)); //returns selected city of the main activity to add to the list
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);    //returns nothing to main activity
                finish();
            }
        });
    }

    //retrieving data from web
    private class RetrieveFromNet extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground (String... url_strings) {

            String url_string = setURL(url_strings[0]);     //gets the first currency forms a url request using it
            @Nullable HttpURLConnection connection = null;
            @Nullable BufferedReader bufferedReader = null;

            /* Retrieving data from internet */
            try {
                URL url = new URL(url_string);      //check if string is a valid url type
                connection = (HttpURLConnection) url.openConnection();      //opens connection
                connection.connect();       //connects

                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));    //gets data
                StringBuffer stringBuffer = new StringBuffer();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);      //changes to string
                }

                return new JSONObject(stringBuffer.toString());
            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "Error in connection to server.", Toast.LENGTH_SHORT).show();
                return null;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Failed to connect to server. Check your connection and try.", Toast.LENGTH_SHORT).show();
                return null;
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error in data format.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Error in formatted data", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onPostExecute(JSONObject fetchedData){       //after getting data from server
            super.onPostExecute(fetchedData);

            fetchingDataBar.setVisibility(View.GONE);       //makes processingBar invisible
            ParseFunctions parseFunctions = new ParseFunctions();

            // data send for management
            managingListOfCities.ManagingListOfCities(parseFunctions.parseLocationFromNet(fetchedData));    //parses fetched data
            recommendedCities = managingListOfCities.getListOfCities();
            recommendedCitiesAdaptor.clear();
            recommendedCitiesAdaptor.addAll(recommendedCities);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchingDataBar.setVisibility(View.VISIBLE);
        }
    }

    //setting URL path to server
    public String setURL(String enteredTxt){
        String firstSec = "https://query.yahooapis.com/v1/public/yql?q=select%20woeid%2C%20name%2C%20country%2C%20admin1%2C%20centroid%20from%20geo.places(10)%20where%20text%3D%22";
        String lastSec = "*%22&format=json";
        try {
            return firstSec + URLEncoder.encode(enteredTxt,"UTF-8") + lastSec;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        cancelBtn.performClick();
    }
}
