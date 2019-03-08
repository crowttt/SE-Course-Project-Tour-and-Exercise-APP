package seproject.ccu.seproject.weather.locationContent;

public class ParentCity {

    // This class store the township name in Chinese (ex. 民雄鄉)

    private String LocalizedName;

    public ParentCity(String localizedName) {
        LocalizedName = localizedName;
    }

    public String getLocalizedName() {
        return LocalizedName;
    }

    public void setLocalizedName(String localizedName) {
        LocalizedName = localizedName;
    }
}
