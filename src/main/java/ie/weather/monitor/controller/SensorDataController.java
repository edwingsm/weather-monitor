package ie.weather.monitor.controller;

import ie.weather.monitor.service.SensorDataService;
import ie.weather.monitor.dto.AverageMetricResponse;
import ie.weather.monitor.dto.SensorDataRequest;
import ie.weather.monitor.entity.SensorData;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    /**
     * Register new sensor data
     * POST /api/sensor-data
     */
    @PostMapping
    public ResponseEntity<SensorData> registerSensorData(@Valid @RequestBody SensorDataRequest request) {
        try {
            SensorData savedData = sensorDataService.registerSensorData(request);
            return new ResponseEntity<>(savedData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get average temperature for all sensors in date range
     * GET /api/sensor-data/average/temperature?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59
     */
    @GetMapping("/average/temperature")
    public ResponseEntity<AverageMetricResponse> getAverageTemperatureForAllSensors(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        try {
            AverageMetricResponse response = sensorDataService.getAverageTemperatureForAllSensors(startDate, endDate);
            if (response.getAverageValue() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get average temperature for specific sensor in date range
     * GET /api/sensor-data/sensor/{sensorId}/average/temperature?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59
     */
    @GetMapping("/sensor/{sensorId}/average/temperature")
    public ResponseEntity<AverageMetricResponse> getAverageTemperatureForSensor(
            @PathVariable String sensorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        try {
            AverageMetricResponse response = sensorDataService.getAverageTemperatureForSensor(sensorId, startDate, endDate);
            if (response.getAverageValue() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all sensor data (for testing/debugging)
     * GET /api/sensor-data/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<SensorData>> getAllSensorData() {
        List<SensorData> data = sensorDataService.getAllSensorData();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}