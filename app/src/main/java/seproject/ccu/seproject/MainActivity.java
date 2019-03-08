package seproject.ccu.seproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import seproject.ccu.seproject.MySport.MyExerciseActivity;
import seproject.ccu.seproject.MySport.database.AppDatabaseHelper;
import seproject.ccu.seproject.TourGuide.TourGuideActivity;
import seproject.ccu.seproject.weather.Common;
import seproject.ccu.seproject.weather.AccuWeather;
import seproject.ccu.seproject.weather.Locate;
import seproject.ccu.seproject.weather.PM25;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView mileage, calorie, points;
    TextView txtCity, txtUpdateTime, txtDescription, txtHumidity, txtCelsius, txtPM25;
    ImageView themeChange;
    SharedPreferences spref;
    boolean darkmode;

    LocationManager locationManager;
    String provider;
    static double lat, lon;
    static String key;
    static int bestSuit;
    AccuWeather accuWeather[] = new AccuWeather[1];
    PM25 pm25[] = new PM25[80];
    Locate locate = new Locate();

    int MY_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        spref = getSharedPreferences("Mode", MODE_PRIVATE);
        darkmode = spref.getBoolean("IS_DARK", false);

        if (darkmode == true) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        themeChange = (ImageView) findViewById(R.id.themeChangeButton);

        if (darkmode == true) {
            themeChange.setImageResource(R.drawable.ic_darkmode);
        } else {
            themeChange.setImageResource(R.drawable.ic_lightmode);
        }

        txtCity = findViewById(R.id.TxtCity);
        txtUpdateTime = findViewById(R.id.TxtUpdateTime);
        txtDescription = findViewById(R.id.TxtDescription);
        txtHumidity = findViewById(R.id.TxtHumidity);
        txtCelsius = findViewById(R.id.TxtCelsius);
        txtPM25 = findViewById(R.id.TxtPM25);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
        }
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location location = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                // Found best last known location: %s", l);
                location = l;
            }
        }
        if (location == null)
            Log.e("TAG", "No Locate");
        else {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }
        Common.LocationRequest(String.valueOf(lat), String.valueOf(lon));
        new GetWeather().execute(Common.LocationRequest(String.valueOf(lat), String.valueOf(lon)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
        }

        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 60000, 1, this);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();

        new GetWeather().execute(Common.LocationRequest(String.valueOf(lat), String.valueOf(lon)));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetWeather extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        boolean success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("更新中...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String locStr = null;
            String weaStr = null;
            String pm25Str = null;
            String urlStr = params[0];
            Gson gson = new Gson();
            success = true;

            // get location
            try {
                URL url = new URL(urlStr);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == 200) {
                    BufferedReader read = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = read.readLine()) != null)
                        sb.append(line);
                    locStr = sb.toString();
                    httpURLConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            locate = gson.fromJson(locStr, Locate.class);
            if(locate == null) {
                success = false;
                Log.e("TAG", "fail to update location");
            }
            else {
                key = locate.getKey();
            }

            urlStr = Common.WeatherRequest(key);
            // get weather
            try {
                URL url = new URL(urlStr);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == 200) {
                    BufferedReader read = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = read.readLine()) != null)
                        sb.append(line);
                    weaStr = sb.toString();
                    httpURLConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            accuWeather = gson.fromJson(weaStr, AccuWeather[].class);
            if(accuWeather == null) {
                success = false;
                Log.e("TAG", "fail to update weather");
            }

            urlStr = Common.PM25Request();
            // get pm2.5
            try {
                URL url = new URL(urlStr);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == 200) {
                    BufferedReader read = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = read.readLine()) != null)
                        sb.append(line);
                    pm25Str = sb.toString();
                    httpURLConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pm25Str = pm25Str.replace("\"\"","\"0\"");
            pm25 = gson.fromJson(pm25Str, PM25[].class);
            if(pm25 == null) {
                success = false;
                Log.e("TAG", "fail to update PM2.5");
            }
            else {
                double error = 9999;

                for(int i = 0; i < pm25.length; i++) {
                    double temp = Math.abs(lat / pm25[i].getLatitude() - 1) + Math.abs(lon / pm25[i].getLongitude() - 1);
                    if(temp < error) {
                        error = temp;
                        bestSuit = i;
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            pd.dismiss();
            if(success) {
                if(locate.getAdministrativeArea().getLocalizedType().equals("直轄市")) {
                    txtCity.setText(String.format("%s %s", locate.getAdministrativeArea().getLocalizedName(), locate.getParentCity().getLocalizedName()));
                }
                else {
                    txtCity.setText(String.format("%s %s", locate.getAdministrativeArea().getLocalizedName(), locate.getParentCity().getLocalizedName()));
                }
                txtUpdateTime.setText(String.format("%s", Common.getDateNow()));
                txtDescription.setText(String.format("%s", accuWeather[0].getWeatherText()));
                txtHumidity.setText(String.format(Locale.US, "%d%%", accuWeather[0].getRelativeHumidity()));
                txtCelsius.setText(String.format(Locale.US, "%.0f", accuWeather[0].getTemperature().getMetric().getValue()));
                txtPM25.setText(String.format(Locale.US, "%s", pm25[bestSuit].getPM25()));
            }
            else {
                txtUpdateTime.setText("更新失敗");
            }
        }

    }

    public void onClickThemeChange(View view){
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            themeChange.setImageResource(R.drawable.ic_darkmode);

        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            themeChange.setImageResource(R.drawable.ic_lightmode);
        }
        shareTheme();
        restartApp();
    }

    public void onClickMySport(View view){
        Intent intent = new Intent(this, MyExerciseActivity.class);
        startActivity(intent);
    }

    public void onClickTourGuide(View view){
        Intent intent = new Intent(this, TourGuideActivity.class);
        startActivity(intent);
    }

    public void onClickPointMenu(View view){
        Intent intent = new Intent(this, PointMenuActivity.class);
        startActivity(intent);
    }

    public void restartApp(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public void shareTheme(){
        spref = getSharedPreferences("Mode", MODE_PRIVATE);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            spref.edit().putBoolean("IS_DARK", true).commit();
        }
        else{
            spref.edit().putBoolean("IS_DARK", false).commit();
        }
    }
}

