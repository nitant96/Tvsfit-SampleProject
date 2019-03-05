package com.example.nitantsood.tvssampleapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPMapGetRequest extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        String stringUrl = strings[0];
        String queryCity = strings[1];
        String result="";
        StringBuilder sb=new StringBuilder();


        URL myUrl = null;
        try {

            //Create a URL object holding our url
            myUrl = new URL(stringUrl+queryCity+"&key=AIzaSyDsMtXN2s8UXoLrboSVC64UOCqsbaz3E8g");
            //Create a connection
            URL newURL=new URL("http://www.mapquestapi.com/geocoding/v1/address?key=cG1zppczrEsYUsGTkCgpxt39MmeUdDsF&location="+queryCity);
            HttpURLConnection urlConnection =(HttpURLConnection)
                    newURL.openConnection();



            urlConnection.setDoOutput(true);
//            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);

            urlConnection.connect();

            int HttpResult =urlConnection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                return sb.toString();

            }else{
                return urlConnection.getResponseMessage();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
