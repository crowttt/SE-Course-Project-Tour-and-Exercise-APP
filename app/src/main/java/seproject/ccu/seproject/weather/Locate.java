package seproject.ccu.seproject.weather;

import seproject.ccu.seproject.weather.locationContent.AdministrativeArea;
import seproject.ccu.seproject.weather.locationContent.ParentCity;

public class Locate {

    // This class store the location content
    // Key value for AccuWeather api
    // LocalizedName value is the village name in Chinese (ex. 三興村)

    private String Key;
    private String LocalizedName;
    private AdministrativeArea AdministrativeArea;
    private ParentCity ParentCity;

    public Locate() {}

    public Locate(String key, String localizedName, AdministrativeArea administrativeArea, ParentCity parentCity) {
        Key = key;
        LocalizedName = localizedName;
        this.AdministrativeArea = administrativeArea;
        this.ParentCity = parentCity;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getLocalizedName() {
        return LocalizedName;
    }

    public void setLocalizedName(String localizedName) {
        LocalizedName = localizedName;
    }

    public AdministrativeArea getAdministrativeArea() { return AdministrativeArea; }

    public void setAdministrativeArea(AdministrativeArea administrativeArea) { this.AdministrativeArea = administrativeArea; }

    public ParentCity getParentCity() {
        return ParentCity;
    }

    public void setParentCity(ParentCity parentCity) {
        this.ParentCity = parentCity;
    }
}
