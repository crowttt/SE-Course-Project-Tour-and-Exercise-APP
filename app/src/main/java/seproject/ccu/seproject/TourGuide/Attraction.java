package seproject.ccu.seproject.TourGuide;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;

public class Attraction {
    private String name ;
    private String describe;
    private String phone ;
    private String photo ;
    private double lat ;
    private double lng ;

    public Attraction(BufferedReader br ) throws IOException {
        Log.i("Attraction", "1");
        this.name = br.readLine() ;
        this.describe = br.readLine() ;
        this.phone = br.readLine() ;
        this.photo = br.readLine() ;
        Log.i("Attraction", "2");
        this.lng = Double.parseDouble( br.readLine() ) ;
        this.lat = Double.parseDouble( br.readLine() ) ;
    }

    public String getName()     { return this.name; }

    public String getDescribe() { return this.describe; }

    public String getPhone() { return this.phone ; }

    public String getPhoto() { return this.photo ; }

    public double getLat() { return this.lat ; }

    public double getLng() { return this.lng ; }

}
