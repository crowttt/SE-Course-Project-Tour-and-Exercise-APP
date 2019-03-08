package seproject.ccu.seproject.weather;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
 //   private static String API_Key = "KSNaOMn8XBS4vdaouQsiRG2QaB8q4mgT";
    private static String API_Key = "TKQfITmWQD63KwKsEYuQhA8WZXwgqVi3";
    private static String Location_Link = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/";
    private static String Weather_Link = "http://dataservice.accuweather.com/currentconditions/v1/";
    private static String PM25_Link = "http://opendata.epa.gov.tw/webapi/Data/REWIQA/?$select=SiteName,PM2.5,Longitude,Latitude&$orderby=SiteName&$skip=0&$top=1000&format=json";

    @NonNull
    public static String LocationRequest(String lat, String lon) {
        StringBuilder ret = new StringBuilder(Location_Link);
        ret.append(String.format("search?apikey=%s&q=%s,%s&language=zh-tw", API_Key, lat, lon));
        return ret.toString();
    }

    @NonNull
    public static String WeatherRequest(String key) {
        StringBuilder ret = new StringBuilder(Weather_Link);
        ret.append(String.format("%s?apikey=%s&language=zh-tw&details=true", key , API_Key));
        return ret.toString();
    }

    @NonNull
    public static String PM25Request() {
        return PM25_Link;
    }

    public static String unixTimeStampToDateTime(double unixTimeStamp) {
        DateFormat ret = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long)unixTimeStamp*1000);
        return ret.format(date);
    }
/*
    public static String getIcon(String icon) {
        return String.format("https://openweathermap.org/img/w/%s.png", icon);
    }
    */

    public static String getDateNow() {
        DateFormat ret = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        return ret.format(date);
    }
}
