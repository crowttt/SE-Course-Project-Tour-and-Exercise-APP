package seproject.ccu.seproject.MySport.SportList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;


public class LocationService extends Service {
    private Location lastLocation;
    private LocationListener listener;
    private LocationManager locationManager;
    private double distance;
    private boolean isStart = true;

    @NonNull
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate() {
        locationProcessor();
    }

    public void locationProcessor(){
        listener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                Intent intent = new Intent("location");
                // 計算這次與上次的經緯度差
                if(isStart){
                    // copy constructor
                    lastLocation = new Location(location);
                    distance = 0;
                    isStart = false;
                }
                distance += lastLocation.distanceTo(location);
                intent.putExtra("distance", distance);
                sendBroadcast(intent);
                lastLocation.setLatitude(location.getLatitude());
                lastLocation.setLongitude(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int t, Bundle bundle){

            }

            @Override
            public void onProviderEnabled(String s){

            }

            @Override
            public void onProviderDisabled(String s){
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        // location 更新時間有待商確
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates(listener);
        }
    }
}
