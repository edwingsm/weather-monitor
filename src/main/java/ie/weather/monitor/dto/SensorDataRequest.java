package ie.weather.monitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SensorDataRequest {

    @NotBlank(message = "Sensor ID is required")
    private String sensorId;

    @NotNull(message = "Temperature is required")
    private Double temperature;

    private Double humidity;
    private Double pressure;
    private Double windSpeed;
    private LocalDateTime timestamp; // If not provided, will use current time

    // Constructors
    public SensorDataRequest() {}

    public SensorDataRequest(String sensorId, Double temperature) {
        this.sensorId = sensorId;
        this.temperature = temperature;
    }

    // Getters and Setters
    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public Double getPressure() { return pressure; }
    public void setPressure(Double pressure) { this.pressure = pressure; }

    public Double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}