package ie.weather.monitor.service;


import ie.weather.monitor.dto.AverageMetricResponse;
import ie.weather.monitor.dto.SensorDataRequest;
import ie.weather.monitor.entity.SensorData;
import ie.weather.monitor.repository.SensorDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorDataServiceTest {

    @Mock
    private SensorDataRepository repository;

    @InjectMocks
    private SensorDataService service;

    @Test
    public void testRegisterSensorData() {
        // Prepare test data
        SensorDataRequest request = new SensorDataRequest("SENSOR_001", Double.valueOf(25.0));
        SensorData savedData = new SensorData("SENSOR_001", Double.valueOf(25.0), LocalDateTime.now());
        savedData.setId(Long.valueOf(1L));

        when(repository.save(any(SensorData.class))).thenReturn(savedData);

        // Execute
        SensorData result = service.registerSensorData(request);

        // Verify
        assertNotNull(result);
        assertEquals("SENSOR_001", result.getSensorId());
        assertEquals(25.0, result.getTemperature());
        verify(repository, times(1)).save(any(SensorData.class));
    }

    @Test
    public void testGetAverageTemperatureForAllSensors() {
        // Prepare test data
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);

        when(repository.findAverageTemperatureInDateRange(start, end)).thenReturn(Double.valueOf(22.5));
        when(repository.countDataPointsInDateRange(start, end)).thenReturn(Long.valueOf(100L));

        // Execute
        AverageMetricResponse result = service.getAverageTemperatureForAllSensors(start, end);

        // Verify
        assertNotNull(result);
        assertEquals("temperature", result.getMetric());
        assertEquals(22.5, result.getAverageValue());
        assertEquals(100L, result.getDataPointCount());
        assertNull(result.getSensorId()); // Should be null for all sensors
    }
}
