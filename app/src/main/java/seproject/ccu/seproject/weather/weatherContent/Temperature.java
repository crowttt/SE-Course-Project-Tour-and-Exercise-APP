package seproject.ccu.seproject.weather.weatherContent;

public class Temperature {

    // This class store the metric (°C) temperature value

    private Metric Metric;

    public Temperature(Metric metric) {
        this.Metric = metric;
    }

    public Metric getMetric() {
        return Metric;
    }

    public void setMetric(Metric metric) {
        this.Metric = metric;
    }
}
