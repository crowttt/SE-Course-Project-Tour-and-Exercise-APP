package seproject.ccu.seproject.weather;
import com.google.gson.annotations.SerializedName;

public class PM25 {
    @SerializedName("PM2.5")
    private String PM25;
    private double Longitude;
    private double Latitude;

    public PM25(String PM25, double longitude, double latitude) {
        this.PM25 = PM25;
        Longitude = longitude;
        Latitude = latitude;
    }

    public String getPM25() { return PM25; }

    public void setPM25(String PM25) { this.PM25 = PM25; }

    public double getLongitude() { return Longitude; }

    public void setLongitude(double longitude) { Longitude = longitude; }

    public double getLatitude() { return Latitude; }

    public void setLatitude(double latitude) { Latitude = latitude; }
}
