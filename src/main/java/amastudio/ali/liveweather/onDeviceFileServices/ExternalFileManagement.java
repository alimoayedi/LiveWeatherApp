package amastudio.ali.liveweather.onDeviceFileServices;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.LinkedHashMap;

import amastudio.ali.liveweather.ParseFunctions;

/**
 * Created by ali on 03/08/2018.
 */

public class ExternalFileManagement { //on data saved as JSON format

    public String readFile(String filename){        //read saved favorite locations from file
        @Nullable
        String importedText;

        try {
            FileInputStream importedFile = new FileInputStream(new File(filename));
            InputStreamReader inputStreamReader = new InputStreamReader(importedFile);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String receivedData = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receivedData = bufferedReader.readLine()) != null){
                stringBuilder.append(receivedData);
            }

            importedFile.close();
            importedText = stringBuilder.toString();
            return importedText;

        } catch (FileNotFoundException e) { // file not found
            return null;
        } catch (IOException e) { //cannot read file
            return null;
        }
    }

    public boolean writeFile(LinkedHashMap<String, String> incomeDict, String filename) {       //write favorite locations on disk
        ParseFunctions parseFunctions = new ParseFunctions();
        JSONObject developedJSONObj = parseFunctions.parseToJSONFile(incomeDict);
        try {
            File outputFile = new File(filename); //e.g. "locations.json"
            Writer outputBuffer = new BufferedWriter(new FileWriter(outputFile,false));
            outputBuffer.write(developedJSONObj.get("locations").toString());
            outputBuffer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveSetting(LinkedHashMap<String, String> setting, String filename){     //saved settings on disk
        JSONObject settingJSON = new JSONObject();
        try {
            settingJSON.put("unit",setting.get("unit").toString());
            settingJSON.put("notification", setting.get("notification").toString());
            File outputFile = new File(filename);   //e.g. "locations.json"
            Writer outputBuffer = new BufferedWriter(new FileWriter(outputFile,false));
            outputBuffer.write(settingJSON.toString());
            outputBuffer.close();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}