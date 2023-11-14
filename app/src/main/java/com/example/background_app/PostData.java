package com.example.background_app;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PostData extends AsyncTask<String, Void, String> {
    //Map<String, String> cache = new HashMap();

    @Override
    protected String doInBackground(String... strings) {
        String responseString = null;
        try {

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            // on below line creating a url to post the data.
            URL url = new URL("http://172.20.10.2:1880/app/capt/" + ts);

            // on below line opening the connection.
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            // on below line setting method as post.
            client.setRequestMethod("POST");

            // on below line setting content type and accept type.
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("Accept", "application/json");

            // on below line setting client.
            client.setDoOutput(true);

            // on below line we are creating an output stream and posting the data.
            try (OutputStream os = client.getOutputStream()) {
                byte[] input = strings[0].getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // on below line creating and initializing buffer reader.
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(client.getInputStream(), "utf-8"))) {

                // on below line creating a string builder.
                StringBuilder response = new StringBuilder();

                // on below line creating a variable for response line.
                String responseLine = null;

                // on below line writing the response
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Log.i("response", response.toString());
                return response.toString();

                // on below line displaying a toast message.
                //Toast.makeText(MainActivity.this, "Data has been posted to the API.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

            // on below line handling the exception.
            e.printStackTrace();
            //Toast.makeText(MainActivity.this, "Fail to post the data : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
        Log.i("postResponse", result);
    }
}
