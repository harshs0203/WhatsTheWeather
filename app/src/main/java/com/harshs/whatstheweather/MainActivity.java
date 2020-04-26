package com.harshs.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;

    public void findWeather(View view){

        Log.i("Button", cityName.getText().toString());

    }


    public class Weather extends AsyncTask<String, Void ,String> {

        @Override
        protected String doInBackground(String... urls) {

            String result="";

            URL url ;

            HttpURLConnection connection;

            try {
                url = new URL(urls[0]);

                connection = (HttpURLConnection) url.openConnection();

                InputStream in = connection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data=reader.read();

                while(data !=-1){

                    char current= (char)data;
                    result +=current;

                    data=reader.read();
                }
                return result;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo= jsonObject.getString("weather");

                JSONArray arr= new JSONArray(weatherInfo);

                for (int i=0; i< arr.length();i++){

                    JSONObject jSonPart = arr.getJSONObject(i);

                    Log.i("Weather Main", jSonPart.getString("main"));
                    Log.i("Weather Detail", jSonPart.getString("description"));
                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName=(EditText) findViewById(R.id.cityName);
    }
}
