package seproject.ccu.seproject.weather.weatherContent;

public class Metric {

    // This class store the temperature value and type

    private double Value;
    private String Unit;

    public Metric(double value, String unit) {
        Value = value;
        Unit = unit;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }
}
