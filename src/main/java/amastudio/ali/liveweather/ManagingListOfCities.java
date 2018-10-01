package amastudio.ali.liveweather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ali on 31/07/2018.
 */

public class ManagingListOfCities {

    private List<String> listOfCities = new ArrayList<String>();
    private LinkedHashMap<String, String> listOfLocations = new LinkedHashMap<String, String>();

    // Constructor
    public void ManagingListOfCities(HashMap<String, String> inputArrayList) {
        listOfLocations.clear();
        if (inputArrayList != null) {listOfLocations.putAll(inputArrayList);}
    }

    public List<String> getListOfCities() {
        if(listOfLocations != null) {
            listOfCities.clear();
            for (String key : listOfLocations.keySet()) {listOfCities.add(listOfLocations.get(key));}
        }
        return listOfCities;
    }

    public String[] addCityToFavorite(int cityRowIndex) {
        return new String[] {listOfLocations.keySet().toArray()[cityRowIndex].toString(),listOfLocations.get((listOfLocations.keySet().toArray())[cityRowIndex])};
    }
}

