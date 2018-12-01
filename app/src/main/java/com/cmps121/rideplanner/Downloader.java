package com.cmps121.rideplanner;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader  extends AsyncTask<String, Void, String> {

//    private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
    InputStream inputStream = null;
    String result = "";


//        String url_select = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+702+College+Nine+Road,+Santa+Cruz,CA+
//            "%22&destinations="1156+High+Street,+Santa+Cruz,CA+"&key=AIzaSyCaIBBY1fTbyKuTUdfjEceF01ycLpH7OdM";

        @Override
        protected String doInBackground(String... strings) {

            for(int i =0; i<=strings.length; i++){
                strings[i] = strings[i].replaceAll("[ ]", "+");

            }

            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+strings[0]+
                        "%22&destinations="+strings[1]+"&key=AIzaSyCaIBBY1fTbyKuTUdfjEceF01ycLpH7OdM");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(1000);
                con.setReadTimeout(1000);
                con.setRequestMethod("GET");
                InputStream is = con.getInputStream();

                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line = bf.readLine()) != null){
                    sb.append(line);
                }
                bf.close();
                is.close();
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            //grabbing the miles a
            int where = s.indexOf("distance");
            int where1 = s.indexOf("mi");
            String newww= s.substring(where, where1).replaceAll("[^\\d.]", "") + "miles from event";;

            int wheretime = s.indexOf("duration");
            int wheretime1 = s.indexOf("}");
            String time= s.substring(where, where1).replaceAll("[^\\d.]", "") + "seconds from event";;




        }
}
