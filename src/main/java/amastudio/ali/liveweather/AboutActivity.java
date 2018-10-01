package amastudio.ali.liveweather;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSize();        //set size of activity window
        setBuildVersion();      //get build version

        TextView yahooLicenceTxt = (TextView) findViewById(R.id.yahooLicence);
        TextView designerLicenceTxt = (TextView) findViewById(R.id.designerLicence);

        SpannableString yahooLicence = new SpannableString(getString(R.string.yahooAttribute));
        SpannableString weatherIconLicence =  new SpannableString(getString(R.string.iconAttribute));

        ClickableSpan yahooIconClick = new ClickableSpan() {        //link to yahoo web-page for use licence
            @Override
            public void onClick(View widget) {
                Uri yahoouri = Uri.parse("https://www.yahoo.com/?ilc=401");
                Intent intent = new Intent(Intent.ACTION_VIEW, yahoouri);
                startActivity(intent);      //asks user to choose browser
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);     //after click on link, no underline is shown.
            }
        };

        ClickableSpan weatherIconLicenceClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Uri weatherIconUri = Uri.parse("https://www.iconfinder.com/iconsets/weather-color-2");
                Intent intent = new Intent(Intent.ACTION_VIEW, weatherIconUri);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);     //after click on link, no underline is shown.
            }
        };

        ClickableSpan designerLicenceClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Uri designerUri = Uri.parse("https://www.iconfinder.com/Neolau1119");
                Intent intent = new Intent(Intent.ACTION_VIEW, designerUri);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);     //after click on link, no underline is shown.
            }
        };

        ClickableSpan iconPublisherLicenceClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Uri publisherLicence = Uri.parse("https://creativecommons.org/licenses/by/3.0/");
                Intent intent = new Intent(Intent.ACTION_VIEW, publisherLicence);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);     //after click on link, no underline is shown.
            }
        };


        // set location links in through the text
        yahooLicence.setSpan(yahooIconClick, 37,46, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        weatherIconLicence.setSpan(weatherIconLicenceClick, 38,51,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        weatherIconLicence.setSpan(designerLicenceClick, 55,62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        weatherIconLicence.setSpan(iconPublisherLicenceClick, 79,88,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        yahooLicenceTxt.setText(yahooLicence);

        yahooLicenceTxt.setMovementMethod(LinkMovementMethod.getInstance());
        yahooLicenceTxt.setHighlightColor(Color.TRANSPARENT);       //clicking on link doesn't change color of text

        designerLicenceTxt.setText(weatherIconLicence);
        designerLicenceTxt.setMovementMethod(LinkMovementMethod.getInstance());
        designerLicenceTxt.setHighlightColor(Color.TRANSPARENT);    //clicking on link doesn't change color of text

    }
    private void setupSize() {
        setContentView(R.layout.about_layout);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);      //get display size

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.85),(int) (height * 0.4));       //reduce activity size
    }
    private void setBuildVersion() {
        TextView versionTxt = (TextView) findViewById(R.id.versionTxt);
        String version = "";
        try {
            version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionTxt.setText(versionTxt.getText().toString() + version);      //set version text field
    }
}

