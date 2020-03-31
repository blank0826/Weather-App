package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection connection;
            try{
                url =new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
            return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           try{
               JSONObject jsonObject = new JSONObject(s);
                String weatherinfo =jsonObject.getString("weather");
               JSONArray arr=new JSONArray(weatherinfo);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main=jsonPart.getString("main").substring(0,1).toUpperCase()+jsonPart.getString("main").substring(1);
                    String Description=jsonPart.getString("description").substring(0,1).toUpperCase()+jsonPart.getString("description").substring(1);
                    textView.setText(main+": "+Description+"\n");
                }
           }
           catch(Exception e){
               e.printStackTrace();
           }
        }

    }
    public void giveWeather(View view){
        DownloadTask download = new DownloadTask();
    String city = (String) editText.getText().toString();
        download.execute("https://openweathermap.org/data/2.5/weather?q="+city+"&appid=b6907d289e10d714a6e88b30761fae22");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.weatherText);
        textView=findViewById(R.id.resultText);
    }
}
