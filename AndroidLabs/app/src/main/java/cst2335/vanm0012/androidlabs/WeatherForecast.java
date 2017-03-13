package cst2335.vanm0012.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class WeatherForecast extends AppCompatActivity
{
    protected static final String ACTIVITY_NAME = "WeatherForecast";

    protected ImageView weatherImage;
    protected ProgressBar progressBar;
    protected TextView currentTemp;
    protected TextView minTemp;
    protected TextView maxTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherImage = (ImageView) findViewById(R.id.weatherImage);
        progressBar = (ProgressBar) findViewById(R.id.weatherProgressBar);
        currentTemp = (TextView) findViewById(R.id.currentTemperature);
        minTemp = (TextView) findViewById(R.id.minTemperature);
        maxTemp = (TextView) findViewById(R.id.maxTemperature);

        progressBar.setVisibility(View.VISIBLE);

        new ForecastQuery().execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=json&units=metric");
    }

     private class ForecastQuery extends AsyncTask<String, Integer, HashMap<String, Object>>
    {
        InputStream GET(String url_addr)
        {
            InputStream inputStream = null;

            try
            {
                URL url = new URL(url_addr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
            }
            catch (MalformedURLException e)
            {
                Log.e(ACTIVITY_NAME, "URL is malformed");
            }
            catch (IOException e)
            {
                Log.e(ACTIVITY_NAME, "IOException when opening connection");
            }

            return inputStream;
        }

        protected void saveBitmap(InputStream bitmapStream, String fname)
        {
            try
            {
                Bitmap image = BitmapFactory.decodeStream(bitmapStream);
                FileOutputStream outputStream = openFileOutput(fname, Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
                Log.i(ACTIVITY_NAME, "Saved bitmap: " + fname);
            }
            catch (IOException e)
            {
                Log.e(ACTIVITY_NAME, "IOException while saving bitmap");
            }
        }

        protected Bitmap getBitmap(String name)
        {
            Bitmap image = null;
            InputStream bitmapStream;
            String fname = name + ".png";

            try
            {
                bitmapStream = openFileInput(fname);
            }
            catch (FileNotFoundException e)
            {
                Log.d(ACTIVITY_NAME, "FileNotFound, will download it now");
                saveBitmap(GET("http://openweathermap.org/img/w/" + fname), fname);
                bitmapStream = GET("http://openweathermap.org/img/w/" + fname);
            }

            if (bitmapStream != null)
            {
                image = BitmapFactory.decodeStream(bitmapStream);
            }

            return image;
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... args)
        {
            HashMap<String, Object> queryResults = new HashMap<>();
            HashMap<String, Object> weatherResult;
            String bitmap_name;

            InputStream inputStream =
                    GET(args[0]);

            if (inputStream != null)
            {
                try
                {
                    //noinspection unchecked
                    weatherResult = new ObjectMapper().readValue(inputStream, HashMap.class);

                    @SuppressWarnings("unchecked")
                    HashMap<String, String> bitmap = (HashMap) ((ArrayList) weatherResult.get("weather")).get(0);
                    bitmap_name = bitmap.get("icon");

                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> temps = (HashMap) weatherResult.get("main");
                    queryResults.put("temp", temps.get("temp").toString());
                    publishProgress(25);
                    queryResults.put("temp_min", temps.get("temp_min").toString());
                    publishProgress(50);
                    queryResults.put("temp_max", temps.get("temp_max").toString());
                    publishProgress(75);
                    queryResults.put("bitmap", getBitmap(bitmap_name));
                    publishProgress(100);
                }
                catch (IOException e)
                {
                    Log.e(ACTIVITY_NAME, "IOException while reading values into ObjectMapper");
                }
            }

            return queryResults;
        }

        @Override
        protected void onProgressUpdate(Integer... value)
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result)
        {
            super.onPostExecute(result);

            progressBar.setVisibility(View.INVISIBLE);

            currentTemp.setText(currentTemp.getHint() + ": " + result.get("temp"));
            minTemp.setText(minTemp.getHint() + ": " + result.get("temp_min"));
            maxTemp.setText(maxTemp.getHint() + ": " + result.get("temp_max"));

            weatherImage.setImageBitmap((Bitmap) result.get("bitmap"));
            weatherImage.setBackground(null);
        }
    }
}
