package com.mk.firstapp;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Mohamed Kamel on 01/04/2017.
 */

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }
    public String makeServiceCall(String reqUrl){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           // connection.getRequestMethod();

            InputStream stream = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(stream);



        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e){
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return response;
    }
    private String convertStreamToString (InputStream istream){

        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null){
                builder.append(line).append("\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                istream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }
}
