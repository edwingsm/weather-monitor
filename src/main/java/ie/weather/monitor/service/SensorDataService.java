package ie.weather.monitor.service;


import ie.weather.monitor.dto.AverageMetricResponse;
import ie.weather.monitor.dto.SensorDataRequest;
import ie.weather.monitor.entity.SensorData;
import ie.weather.monitor.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository repository;

    /**
     * Register new sensor data
     */
    public SensorData registerSensorData(SensorDataRequest request) {
        SensorData sensorData = new SensorData();
        sensorData.setSensorId(request.getSensorId());
        sensorData.setTemperature(request.getTemperature());
        sensorData.setHumidity(request.getHumidity());
        sensorData.setPressure(request.getPressure());
        sensorData.setWindSpeed(request.getWindSpeed());

        // Use provided timestamp or current time
        sensorData.setTimestamp(request.getTimestamp() != null ?
                request.getTimestamp() : LocalDateTime.now());

        return repository.save(sensorData);
    }

    /**
     * Get average temperature for all sensors in date range
     */
    public AverageMetricResponse getAverageTemperatureForAllSensors(
            LocalDateTime startDate, LocalDateTime endDate) {

        Double avgTemp = repository.findAverageTemperatureInDateRange(startDate, endDate);
        Long count = repository.countDataPointsInDateRange(startDate, endDate);

        AverageMetricResponse response = new AverageMetricResponse(
                "temperature", avgTemp,formatDateRange(startDate, endDate), count);
        return response;
    }

    /**
     * Get average temperature for specific sensor in date range
     */
    public AverageMetricResponse getAverageTemperatureForSensor(
            String sensorId, LocalDateTime startDate, LocalDateTime endDate) {

        Double avgTemp = repository.findAverageTemperatureBySensorInDateRange(
                sensorId, startDate, endDate);
        Long count = repository.countDataPointsBySensorInDateRange(
                sensorId, startDate, endDate);

        AverageMetricResponse response = new AverageMetricResponse(
                "temperature", avgTemp, formatDateRange(startDate, endDate), count);
        response.setSensorId(sensorId);
        return response;
    }

    /**
     * Get all sensor data for debugging/testing purposes
     */
    public List<SensorData> getAllSensorData() {
        return repository.findAll();
    }

    /**
     * Helper method to format date range for response
     */
    private String formatDateRange(LocalDateTime start, LocalDateTime end) {
        return start.toString() + " to " + end.toString();
    }
}
