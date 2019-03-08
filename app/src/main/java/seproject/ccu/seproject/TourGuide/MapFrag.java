package seproject.ccu.seproject.TourGuide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import seproject.ccu.seproject.R;

public class MapFrag extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private Bundle extra ;
    private Double Lat ;
    private Double Long ;
    private Double attLat ;
    private Double attLong ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MapView);
        extra = getIntent().getExtras();
        if(extra!=null) {
            Lat = extra.getDouble("Latitude");
            Long = extra.getDouble("Longitude");
            attLat = extra.getDouble("attLatitude");
            attLong = extra.getDouble("attLongitude") ;
        }
        else {
            Log.i("map","extra is null") ;
        }
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        LatLng destination = new LatLng(attLat,attLong);
        LatLng nowLocation = new LatLng(Lat,Long) ;
        gMap.addMarker(new MarkerOptions().position(destination).title("Marker Title").snippet("Marker Description"));
        gMap.addMarker(new MarkerOptions().position(nowLocation).title("Your Location").snippet("user location"));
    //    gMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync();
        if (ActivityCompat.checkSelfPermission(MapFrag.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapFrag.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("map", "stillWell_6");
            gMap.setMyLocationEnabled(true);
            Log.i("map", "stillWell_7");
        }
        else {
            Toast.makeText(MapFrag.this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ActivityCompat.checkSelfPermission(MapFrag.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i("map", "stillWell_8");
                gMap.setMyLocationEnabled(true);
                Log.i("map", "stillWell_9");
            }
        }
        gMap.setMyLocationEnabled(true); // 右上角的定位功能；這行會出現紅色底線，不過仍可正常編譯執行
        gMap.getUiSettings().setZoomControlsEnabled(true);  // 右下角的放大縮小功能
        gMap.getUiSettings().setCompassEnabled(true);       // 左上角的指南針，要兩指旋轉才會出現
        gMap.getUiSettings().setMapToolbarEnabled(true);    // 右下角的導覽及開啟 Google Map功能

        LatLngBounds.Builder builder= new LatLngBounds.Builder() ;
        builder.include(destination) ;
        builder.include(nowLocation) ;
        LatLngBounds bounds = builder.build() ;
        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.2) ;
        int height = (int)(getResources().getDisplayMetrics().heightPixels*1.2) ;
        int padding = (int) (width*0.3); // offset from edges of the map 30% of screen


        Log.d("map", "最高放大層級："+gMap.getMaxZoomLevel());
        Log.d("map", "最低放大層級："+gMap.getMinZoomLevel());

        gMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
      //  gMap.animateCamera(CameraUpdateFactory.zoomTo(16));     // 放大地圖到 16 倍大

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(nowLocation,destination);
        DownloadTask downloadTsk = new DownloadTask() ;
        downloadTsk.execute(url) ;
    }

    private class DownloadTask extends AsyncTask {


        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute((String)(result));

            ParserTask parserTask = new ParserTask();


            parserTask.execute((String)(result));

        }

        @Override
        protected Object doInBackground(Object... url) {
            String data = "";

            try {
                data = downloadUrl((String)(url[0]));
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap>> result) {
            super.onPostExecute(result);
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                for(int k=0;k<points.size();k++) {
                    Log.i( "points",points.get(k).toString()) ;
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            if(lineOptions==null){
                Log.i("Polyline null","polyline is null") ;
                return ;
            }
// Drawing polyline in the Google Map for the i-th route
            gMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=AIzaSyBsdRaKgYvCKc7qoXaP-iaZSCISHQafJGU" ;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
            Log.i("url data", data);

        return data;
    }
}


//---------- Next Class ----------//


class DirectionsJSONParser {

    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap>> parse(JSONObject jObject){

        List<List<HashMap>> routes = new ArrayList<List<HashMap>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l <list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return routes;
    }

    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
