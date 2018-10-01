package amastudio.ali.liveweather;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ali on 12/08/2018.
 */

public class ParseFunctions {
    public HashMap parseWeatherFromNet(JSONObject receivedData){

        LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
        LinkedHashMap<String, HashMap<String,String>> weatherInfo = new LinkedHashMap<String, HashMap<String, String>>();
        String copyrightRegex = "^.*\\*";
        String dateRegex = ".{5}$";

        try {
            receivedData.getJSONObject("query").getJSONObject("results").getJSONObject("channel").get("title"); // checks if any valid result returned, if not goes to catch
            JSONObject infoSection = receivedData.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
            // link to yahoo! weather
            details.put("link", infoSection.getJSONObject("item").get("link").toString().replaceAll(copyrightRegex,""));
            weatherInfo.put("copyrights", (HashMap<String, String>) details.clone());
            details.clear();
            // location
            details.put("name",infoSection.getJSONObject("location").get("city").toString() +", "+ infoSection.getJSONObject("location").get("region").toString());
            details.put("country", infoSection.getJSONObject("location").get("country").toString());
            details.put("lat", infoSection.getJSONObject("item").get("lat").toString());
            details.put("long",infoSection.getJSONObject("item").get("long").toString());
            weatherInfo.put("location", (HashMap<String, String>) details.clone());
            details.clear();
            // wind data
            details.put("speed", infoSection.getJSONObject("wind").get("speed").toString());
            weatherInfo.put("wind", (HashMap<String, String>) details.clone());
            details.clear();
            // atmosphere data
            details.put("humidity", infoSection.getJSONObject("atmosphere").get("humidity").toString());
            weatherInfo.put("atmosphere", (HashMap<String, String>) details.clone());
            details.clear();
            // astronomy data
            details.put("sunrise", infoSection.getJSONObject("astronomy").get("sunrise").toString());
            details.put("sunset", infoSection.getJSONObject("astronomy").get("sunset").toString());
            weatherInfo.put("astronomy", (HashMap<String, String>) details.clone());
            details.clear();
            // current condition
            details.put("temp", infoSection.getJSONObject("item").getJSONObject("condition").get("temp").toString());
            details.put("code", infoSection.getJSONObject("item").getJSONObject("condition").get("code").toString());
            details.put("text", infoSection.getJSONObject("item").getJSONObject("condition").get("text").toString());
            weatherInfo.put("condition", (HashMap<String, String>) details.clone());
            details.clear();
            // forecast
            JSONArray forecastArray = infoSection.getJSONObject("item").getJSONArray("forecast");
            for (int itemIndex = 0; itemIndex < forecastArray.length(); itemIndex++){
                details.put("date", forecastArray.getJSONObject(itemIndex).get("date").toString().replaceAll(dateRegex,""));
                details.put("day", forecastArray.getJSONObject(itemIndex).get("day").toString());
                details.put("code", forecastArray.getJSONObject(itemIndex).get("code").toString());
                details.put("high", forecastArray.getJSONObject(itemIndex).get("high").toString());
                details.put("low", forecastArray.getJSONObject(itemIndex).get("low").toString());
                weatherInfo.put(Integer.toString(itemIndex), (HashMap<String, String>) details.clone());
                details.clear();
            }
            return weatherInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseSavedSetting (String savedSetting) {
        if (savedSetting != null) {
            try {
                JSONObject savedJSON = new JSONObject(savedSetting);
                return savedJSON.get("unit").toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return "f";
        }
    }

    public static LinkedHashMap<String, String> parseSavedFile(String savedText) {
        LinkedHashMap<String, String> listOfCities = new LinkedHashMap<String, String>();
        try {
            JSONArray locations = new JSONArray(savedText);
            for (int itemIndex = 0; itemIndex < locations.length(); itemIndex++) {   //loop on index
                JSONObject jsonCity = locations.getJSONObject(itemIndex);
                String woeid = jsonCity.get("woeid").toString();
                String city = jsonCity.get("name").toString();
                listOfCities.put(woeid,city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfCities;
    }

    @Nullable
    public JSONObject parseToJSONFile(LinkedHashMap<String, String> incomeDict){
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        Iterator iterator = incomeDict.entrySet().iterator();
        while (iterator.hasNext()) {
            jsonObject = new JSONObject();
            Map.Entry<String, String> pair = (Map.Entry<String, String>) iterator.next();
            try {
                jsonObject.put("woeid", pair.getKey().toString());
                jsonObject.put("name", pair.getValue().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        try {
            return new JSONObject().put("locations",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String ,String> parseLocationFromNet(JSONObject inComingJsonSet) {
        HashMap<String, String> parsedData = new HashMap<String, String>();
        try {   // Checking if result is JSON type
            JSONObject result = inComingJsonSet.getJSONObject("query").getJSONObject("results"); // checks if result is returned, if null result not found!
            if (result != null) {
                JSONArray toParseArray = result.getJSONArray("place");  //loop on retrieved places
                for (int indexOfCities = 0; indexOfCities < toParseArray.length(); indexOfCities++) {   //loop on index
                    JSONObject jsonItem = toParseArray.getJSONObject(indexOfCities);
                    String[] cityInfo = parseDetails(jsonItem);    //get parsed Data
                    if ( cityInfo != null) {parsedData.put(cityInfo[0], cityInfo[1]);}   //save parsed data
                }
                return parsedData;
            } else {
                return null;
            }
        } catch (JSONException e) {
            return null;
        }
    }

    // parsing each JSON location Item
    @Nullable
    private String[] parseDetails(JSONObject jsonCityInfo) {
        try {
            if (!jsonCityInfo.isNull("name") && !jsonCityInfo.isNull("country")) {
                String city = jsonCityInfo.get("name").toString() + ", ";
                String country = jsonCityInfo.getJSONObject("country").get("content").toString();
                String province = "";
                if (!jsonCityInfo.isNull("admin1")) {province = jsonCityInfo.getJSONObject("admin1").get("content").toString() + ", ";}
                String woeid = jsonCityInfo.get("woeid").toString();
                return new String[]{woeid, city + province + country};
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}