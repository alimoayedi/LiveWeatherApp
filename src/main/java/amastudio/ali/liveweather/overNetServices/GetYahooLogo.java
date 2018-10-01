package amastudio.ali.liveweather.overNetServices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by ali on 01/09/2018.
 */

public class GetYahooLogo {     //gets yahoo logo
    private String url  = "https://poweredby.yahoo.com/purple.png";
    private ImageView image;
    public void getYahooLogo(ImageView imgBtn){
        this.image = imgBtn;
        new downloadImg().execute(url);
    }

    private class downloadImg extends AsyncTask<String,Void,Bitmap> {       //downloads image

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url_string = urls[0];
            Bitmap yahooLogo = null;
            try {
                InputStream inputStream = new java.net.URL(url_string).openStream();
                yahooLogo = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return yahooLogo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {       //sets image
            image.setImageBitmap(bitmap);
        }
    }
}
