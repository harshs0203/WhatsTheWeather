package com.harshs.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;

    public void findWeather(View view){

        Log.i("Button", cityName.getText().toString());

        Weather task = new Weather();
        try {

           String encodedURL =URLEncoder.encode(cityName.getText().toString(), "UTF-8");

            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedURL + "&appid=ecd9af9d7b34407890a8b4096b6b90e2");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            mgr.hideSoftInputFromInputMethod(cityName.getWindowToken(), 0);
        }catch (Exception e){

            Toast.makeText(getApplicationContext(),"Could not find the Weather",Toast.LENGTH_LONG);

        }
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

            } catch (Exception e) {

               Toast.makeText(getApplicationContext(),"Could not find the Weather",Toast.LENGTH_LONG);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                String mess="";

                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo= jsonObject.getString("weather");

                JSONArray arr= new JSONArray(weatherInfo);

                for (int i=0; i< arr.length();i++){

                    JSONObject jSonPart = arr.getJSONObject(i);

                    String main="";
                    String description="";


                  main= jSonPart.getString("main");
                    description= jSonPart.getString("description");

                    if(main != "" && description !="'"){

                        mess +=main +": "+description+"\r\n";
                    }
                }
                if (mess != ""){

                    resultTextView.setText(mess);

                }else{

                    Toast.makeText(getApplicationContext(),"Could not find the Weather",Toast.LENGTH_LONG);

                }

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(),"Could not find the Weather",Toast.LENGTH_LONG);

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName=(EditText) findViewById(R.id.cityName);
        resultTextView=(TextView) findViewById(R.id.resultTextView);


    }
}
