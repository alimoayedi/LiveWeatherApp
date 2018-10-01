package amastudio.ali.liveweather.overNetServices;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by ali on 30/08/2018.
 */

public class CheckInternetAccess {      //tries to make a connection with Google server, if response returned connection is available.
    private boolean connected = false;
    public void CheckInternetAccess() {
        new InternetAccess().execute();
    }

    class InternetAccess extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                int timeoutMs = 1800;   //waiting time before connection lost!
                Socket sock = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);     //Google server IP

                sock.connect(socketAddress, timeoutMs);
                sock.close();

                return true;
            } catch (IOException e) { return false; }
        }


        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            connected = status;     //saves connection status for later use
        }
    }

    public boolean getConnectionStatus(){ return connected; }       //called from outside to check if connection is available or not!
}
