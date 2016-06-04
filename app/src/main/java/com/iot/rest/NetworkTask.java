package com.iot.rest;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gmartin on 28/05/2016.
 */
public class NetworkTask extends AsyncTask<String, Void, String> {

    private Callback callback;

    public NetworkTask() {
    }

    public NetworkTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(1000 * 3);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (callback != null)
            callback.run(s);
    }
}
