package seproject.ccu.seproject.TourGuide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import seproject.ccu.seproject.R;

public class TourGuideActivity extends AppCompatActivity implements LocationListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;

    private RecyclerView recycler_view;
    private TourInformationAdapter adapter;
    private ArrayList<OneItem> mData = new ArrayList<>();
    private ArrayList<Attraction> mAttraction = new ArrayList<>();
    private boolean getService;
    private Location nowLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences spref = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        if (spref.getBoolean("IS_DARK", false)) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide);


        //取得系統定位服務
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        Log.i("myapp", "stillWell_0");
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            Log.i("myapp", "stillWell_1");
            nowLocation = locationServiceInitial();
            getService = true;    //確認開啟定位服務
        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));    //開啟設定頁面
        }

        //---------- 把資料輸入Attraction ----------//
        try {
            InputStream myInputStream = getAssets().open("Data.dat");
            BufferedReader br = new BufferedReader(new InputStreamReader(myInputStream));

            while (br.ready()) {
                Attraction tmp = new Attraction(br);
                OneItem temp = new OneItem(tmp);
                mAttraction.add(tmp);
                mData.add(temp);
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("R", "-5");
        }
        //---------- 把資料輸入Attraction ----------//

        // 連結元件
        recycler_view = findViewById(R.id.recycler_view);
        // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // 將資料交給adapter
        adapter = new TourInformationAdapter(mData, this);
        // 設置adapter給recycler_view
        recycler_view.setAdapter(adapter);


        adapter.setOnItemClickListener(new TourInformationAdapter.OnItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(TourGuideActivity.this, MapFrag.class);
                Double Lat = nowLocation.getLatitude();
                Double Long = nowLocation.getLongitude();
                Attraction attraction = mAttraction.get(position);
                Double attLat = attraction.getLat();
                Double attLong = attraction.getLng();
                Log.i("attLocation", String.valueOf(attLat));
                intent.putExtra("Latitude", Lat);
                intent.putExtra("Longitude", Long);
                intent.putExtra("attLatitude", attLat);
                intent.putExtra("attLongitude", attLong);
                startActivity(intent);
            }
        });
    }


    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;

    private Location locationServiceInitial() {
        Log.i("myapp", "stillWell_4");
        lms = (LocationManager) getSystemService(LOCATION_SERVICE);    //取得系統定位服務
        Criteria criteria = new Criteria();    //資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
        Log.i("myapp", "stillWell_4-1");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("myapp", "stillWell_4-2");
            requestLocationPermission();

        }
        Log.i("myapp", "stillWell_5");
        Location location = lms.getLastKnownLocation(bestProvider);   // 此處的location 就是取得的定位
        Log.i("myapp", "stillWell_6");
        return location;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (getService) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestLocationPermission(); // 詢問使用者開啟權限
            }
            Log.i("myapp", "if location available");
            lms.requestLocationUpdates(bestProvider, 1000, 1, this);
            //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
        }
    }

    private void requestLocationPermission() {
        Log.i("myapp", "requestLocationPermission");
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("myapp", "stillWell_7");
            // 取得授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);

            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                Log.i("myapp", "stillWell_8");
                // 請求授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                Log.i("myapp", "stillWell_9");
            } else {
                Log.i("myapp", "stillWell_10");
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (getService) {
            lms.removeUpdates(this);    //離開頁面時停止更新
        }
    }

    @Override
    public void onLocationChanged(Location location) {
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
}
