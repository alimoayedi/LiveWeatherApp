package amastudio.ali.liveweather;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import amastudio.ali.liveweather.onDeviceFileServices.GetWeatherImg;

/**
 * Created by ali on 17/08/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String tempUnit;
    private ArrayList<HashMap<String, HashMap<String, String>>> details = new ArrayList<HashMap<String, HashMap<String,String>>>();

    public ExpandableListAdapter(Context context, ArrayList<HashMap<String , HashMap<String, String>>> details, String tempUnit) {
        this.context = context;
        this.details = details;
        this.tempUnit = tempUnit;
    }

    @Override
    public int getGroupCount() {
        return details.size();
    }

    @Override
    public int getChildrenCount(int mainItemIndex) {
        return  1;  // Each item is the HashMap is both child and parent. So parent count (1) should be returned as childrenCount.
        //return listHashMap.get(listGroups.get(groupIndex)).size();
    }

    @Override
    public HashMap<String, HashMap<String,String>> getGroup(int mainItemIndex) {
        return details.get(mainItemIndex);
    }

    @Override
    public HashMap<String, HashMap<String,String>> getChild(int mainItemIndex, int childIndex) {
        return details.get(mainItemIndex);
    }

    @Override
    public long getGroupId(int groupIndex) {
        return groupIndex;
    }

    @Override
    public long getChildId(int groupIndex, int childIndex) {
        return childIndex;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupIndex, boolean b, View view, ViewGroup viewGroup) {

        HashMap<String, HashMap<String, String>> location = getGroup(groupIndex);
        GetWeatherImg getWeatherImg = new GetWeatherImg();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_layout, null);
        }

        TextView nameTxtView = (TextView) view.findViewById(R.id.name);
        TextView countryTxtView = (TextView) view.findViewById(R.id.country);
        TextView tempTxtView = (TextView) view.findViewById(R.id.temp);
        TextView minTempTxtView = (TextView) view.findViewById(R.id.minTemp);
        TextView maxTempTxtView = (TextView) view.findViewById(R.id.maxTemp);
        ImageView weatherConditionImg = (ImageView) view.findViewById(R.id.weatherConditionImg);

        // for each item, it's check if data is available or not, if not textview sets as -- .
        nameTxtView.setText(location != null ? location.get("location").get("name").toString() : "Location Not Found!");
        countryTxtView.setText(location != null ? location.get("location").get("country").toString() : "--");
        tempTxtView.setText(location != null ? location.get("condition").get("temp").toString() + "°" : "--");
        minTempTxtView.setText(location != null ? "Min: " + location.get("0").get("low").toString() + "°" : "Min: --");
        maxTempTxtView.setText(location != null ? "Max: " + location.get("0").get("high").toString() + "°" : "Max: --");
        weatherConditionImg.setImageResource(location != null ? getWeatherImg.getWeatherImg(location.get("condition").get("code").toString()) : getWeatherImg.getWeatherImg("99"));
        // 99 is a code not in the code list to return default img which is an error sign

        return view;
    }

    @Override
    public View getChildView(int groupIndex, int childIndex, boolean b, View view, ViewGroup viewGroup) {

        final HashMap<String, HashMap<String, String>> location = getChild(groupIndex,childIndex);
        GetWeatherImg getWeatherImg = new GetWeatherImg();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.detailed_weather_layout, null);
        }

        TextView weatherConditionTxt = (TextView) view.findViewById(R.id.weatherConditionTxt);
        TextView windTxt = (TextView) view.findViewById(R.id.windSpeedTxt);
        TextView humidityTxt = (TextView) view.findViewById(R.id.humidityTxt);
        TextView sunriseTxt = (TextView)  view.findViewById(R.id.sunriseTxt);
        TextView sunsetTxt = (TextView) view.findViewById(R.id.sunsetTxt);

        TextView fOneDate = (TextView) view.findViewById(R.id.fOneDate);
        ImageView fOneImg = (ImageView) view.findViewById(R.id.fOneImg);
        TextView fOneMin = (TextView) view.findViewById(R.id.fOneMin);
        TextView fOneMax  = (TextView) view.findViewById(R.id.fOneMax);

        TextView fTwoDate = (TextView) view.findViewById(R.id.fTwoDate);
        ImageView fTwoImg = (ImageView) view.findViewById(R.id.fTwoImg);
        TextView fTwoMin = (TextView) view.findViewById(R.id.fTwoMin);
        TextView fTwoMax = (TextView) view.findViewById(R.id.fTwoMax);

        TextView fThreeDate = (TextView) view.findViewById(R.id.fThreeDate);
        ImageView fThreeImg = (ImageView) view.findViewById(R.id.fThreeImg);
        TextView fThreeMin = (TextView) view.findViewById(R.id.fThreeMin);
        TextView fThreeMax = (TextView) view.findViewById(R.id.fThreeMax);

        TextView fFourDate = (TextView) view.findViewById(R.id.fFourDate);
        ImageView fFourImg = (ImageView) view.findViewById(R.id.fFourImg);
        TextView fFourMin = (TextView) view.findViewById(R.id.fFourMin);
        TextView fFourMax = (TextView) view.findViewById(R.id.fFourMax);

        TextView fFiveDate = (TextView) view.findViewById(R.id.fFiveDate);
        ImageView fFiveImg = (ImageView) view.findViewById(R.id.fFiveImg);
        TextView fFiveMin = (TextView) view.findViewById(R.id.fFiveMin);
        TextView fFiveMax = (TextView) view.findViewById(R.id.fFiveMax);

        TextView fSixDate = (TextView) view.findViewById(R.id.fSixDate);
        ImageView fSixImg = (ImageView) view.findViewById(R.id.fSixImg);
        TextView fSixMin = (TextView) view.findViewById(R.id.fSixMin);
        TextView fSixMax = (TextView) view.findViewById(R.id.fSixMax);

        ImageView ywLogo = (ImageView) view.findViewById(R.id.ywLogo);

        //check if each value is available or not
        weatherConditionTxt.setText(location != null ? location.get("condition").get("text").toString() : "--");

        fOneDate.setText(location != null ? location.get("0").get("date").toString() : "--");
        fOneImg.setImageResource(getWeatherImg.getWeatherIcon(location != null ? location.get("0").get("code").toString() : "99"));
        fOneMin.setText("Min: " + (location != null ? location.get("0").get("low").toString() + "°" : "--"));
        fOneMax.setText("Max: " + (location != null ? location.get("0").get("high").toString() + "°" : "--"));

        fTwoDate.setText(location != null ? location.get("1").get("date").toString() : "--");
        fTwoImg.setImageResource(getWeatherImg.getWeatherIcon(location != null ? location.get("1").get("code").toString() : "99"));
        fTwoMin.setText("Min: " + (location != null ? location.get("1").get("low").toString() + "°" : "--"));
        fTwoMax.setText("Max: " + (location != null ? location.get("1").get("high").toString() + "°" : "--"));

        fThreeDate.setText(location != null ? location.get("2").get("date").toString() : "--");
        fThreeImg.setImageResource(getWeatherImg.getWeatherIcon(location != null ? location.get("2").get("code").toString() : "99"));
        fThreeMin.setText("Min: " + (location != null ? location.get("2").get("low").toString() + "°" : "--"));
        fThreeMax.setText("Max: " + (location != null ? location.get("2").get("high").toString() + "°" : "--"));

        fFourDate.setText(location != null ? location.get("3").get("date").toString() : "--");
        fFourImg.setImageResource(getWeatherImg.getWeatherIcon(location != null ? location.get("3").get("code").toString() : "99"));
        fFourMin.setText("Min: " + (location != null ? location.get("3").get("low").toString() + "°" : "--"));
        fFourMax.setText("Max: " + (location != null ? location.get("3").get("high").toString() + "°" : "--"));

        fFiveDate.setText(location != null ? location.get("4").get("date").toString() : "--");
        fFiveImg.setImageResource(getWeatherImg.getWeatherIcon(location != null ? location.get("4").get("code").toString() : "99"));
        fFiveMin.setText("Min: " + (location != null ? location.get("4").get("low").toString() + "°" : "--"));
        fFiveMax.setText("Max: " + (location != null ?location.get("4").get("high").toString() + "°" : "--"));

        fSixDate.setText(location != null ? location.get("5").get("date").toString() : "--");
        fSixImg.setImageResource(getWeatherImg.getWeatherIcon(location != null ? location.get("5").get("code").toString() : "99"));
        fSixMin.setText("Min: " + (location != null ? location.get("5").get("low").toString() + "°" : "--"));
        fSixMax.setText("Max: " + (location != null ? location.get("5").get("high").toString() + "°" : "--"));

        windTxt.setText((location != null ? location.get("wind").get("speed") : "-") + (tempUnit.equals("c") ? " km/h" : " mph"));
        humidityTxt.setText((location != null ? location.get("atmosphere").get("humidity").toString() : "-") + "%");
        sunriseTxt.setText(location != null ? location.get("astronomy").get("sunrise").toString() : "--");
        sunsetTxt.setText(location != null ? location.get("astronomy").get("sunset").toString() : "--");

        //on logo click listener
        ywLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(location.get("copyrights").get("link").toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupId, int childId) {
        return true;
    }

    public void remove(@Nullable int position) {    //if item from list removed
        details.remove(position);
        notifyDataSetChanged();
    }

    public void updateTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }   //updates temperature sign
}
