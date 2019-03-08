package seproject.ccu.seproject.weather.locationContent;

public class AdministrativeArea {

    // This class store the city name in Chinese (ex. 嘉義縣)

    private String LocalizedName;
    private String LocalizedType;

    public AdministrativeArea(String localizedName, String localizedType) {
        LocalizedName = localizedName;
        LocalizedType = localizedType;
    }

    public String getLocalizedName() {
        return LocalizedName;
    }

    public void setLocalizedName(String localizedName) {
        LocalizedName = localizedName;
    }

    public String getLocalizedType() { return LocalizedType; }

    public void setLocalizedType(String localizedType) { LocalizedType = localizedType; }
}
