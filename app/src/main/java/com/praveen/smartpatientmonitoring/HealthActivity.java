package com.praveen.smartpatientmonitoring;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HealthActivity extends AppCompatActivity {
    private TextView temp;
    private TextView condition;
    private static final String TAG = "UsingThingspeakAPI";
    private static final String THINGSPEAK_CHANNEL_ID = "1382704";
    private static final String THINGSPEAK_API_KEY = "HNKJCAT6FFL0ZR41";
    private static final String THINGSPEAK_API_KEY_STRING = "api_key";
    private static final String THINGSPEAK_FIELD1 = "field1";
    private static final String THINGSPEAK_FIELD2 = "field2";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_status);
        temp=findViewById(R.id.temp);
        condition=findViewById(R.id.condition);
        try {
            new FetchThingspeakTask().execute();
        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
        }
    }
    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            Toast.makeText(HealthActivity.this, "Fetching Data from Server.Please Wait...", Toast.LENGTH_SHORT).show();
        }
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(THINGSPEAK_CHANNEL_URL + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "");
                Log.e("url",url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            if(response == null) {
                Toast.makeText(HealthActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                double v1 = channel.getDouble("field1");
                if(v1<=90)
                {
                    temp.setText(String.valueOf(v1));
                    condition.setText("NORMAL");
                }
                else {
                    temp.setText(String.valueOf(v1));
                    condition.setText("HIGH");
                    Toast.makeText(HealthActivity.this, "HIGH TEMPERATURE ALERT!!", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
