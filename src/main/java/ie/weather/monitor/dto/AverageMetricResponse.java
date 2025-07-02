package ie.weather.monitor.dto;

public class AverageMetricResponse {
    private String metric;
    private Double averageValue;
    private String sensorId; // null for all sensors
    private String dateRange;
    private Long dataPointCount;

    public AverageMetricResponse() {}

    public AverageMetricResponse(String metric, Double averageValue, String dateRange, Long dataPointCount) {
        this.metric = metric;
        this.averageValue = averageValue;
        this.dateRange = dateRange;
        this.dataPointCount = dataPointCount;
    }

    // Getters and Setters
    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }

    public Double getAverageValue() { return averageValue; }
    public void setAverageValue(Double averageValue) { this.averageValue = averageValue; }

    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public String getDateRange() { return dateRange; }
    public void setDateRange(String dateRange) { this.dateRange = dateRange; }

    public Long getDataPointCount() { return dataPointCount; }
    public void setDataPointCount(Long dataPointCount) { this.dataPointCount = dataPointCount; }
}