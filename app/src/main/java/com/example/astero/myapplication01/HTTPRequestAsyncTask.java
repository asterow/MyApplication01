package com.example.astero.myapplication01;

import android.os.AsyncTask;

import com.example.astero.myapplication01.projectmanager.data.ProjectList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by astero on 25/02/2017.
 */

public class HTTPRequestAsyncTask extends AsyncTask<String, Void, Void> {
    InputStream isGif = null;
    StringBuilder result = null;
    String ACTION = "";

    public interface AsyncResponse {
        void processFinish(String result, String ACTION);
    }

    public AsyncResponse delegate = null;

    public HTTPRequestAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        result = new StringBuilder();
        ACTION = params[0];


        try{
            URL url = new URL(params[2]);

            connection = (HttpURLConnection)url.openConnection();
//            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod(params[1]);
            connection.connect();
            int codeResponse = connection.getResponseCode();
            if( 200 <= codeResponse && codeResponse < 300 ){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while((line = br.readLine()) != null){
                    result.append(line);
                }
                br.close();
                //System.out.println(br.toString());


            }
            else
                System.out.println("else");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void params) {


        delegate.processFinish(result.toString(), ACTION);
    }



}
