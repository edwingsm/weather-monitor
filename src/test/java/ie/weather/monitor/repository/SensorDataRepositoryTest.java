package ie.weather.monitor.repository;

import ie.weather.monitor.entity.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class SensorDataRepositoryTest {

    @Autowired
    private SensorDataRepository repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void testFindBySensorIdAndTimestampBetween() {
        // Create test data
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);

        SensorData data1 = new SensorData("SENSOR_001", 23.5, now);
        SensorData data2 = new SensorData("SENSOR_001", 25.0, tomorrow);
        SensorData data3 = new SensorData("SENSOR_002", 20.0, now);

        repository.saveAll(List.of(data1, data2, data3));

        // Test query
        List<SensorData> result = repository.findBySensorIdAndTimestampBetween(
                "SENSOR_001", yesterday, now.plusHours(1));

        assertEquals(1, result.size());
        assertEquals("SENSOR_001", result.get(0).getSensorId());
        assertEquals(23.5, result.get(0).getTemperature());
    }

    @Test
    public void testFindAverageTemperatureInDateRange() {
        // Create test data
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);
        LocalDateTime middle = LocalDateTime.of(2024, 1, 15, 12, 0);

        SensorData data1 = new SensorData("SENSOR_001", 20.0, middle);
        SensorData data2 = new SensorData("SENSOR_002", 30.0, middle);

        repository.saveAll(List.of(data1, data2));

        // Test average calculation
        Double average = repository.findAverageTemperatureInDateRange(start, end);

        assertNotNull(average);
        assertEquals(25.0, average); // (20.0 + 30.0) / 2
    }

    @Test
    public void testCountDataPointsInDateRange() {
        // Create test data
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);
        LocalDateTime middle = LocalDateTime.of(2024, 1, 15, 12, 0);

        SensorData data1 = new SensorData("SENSOR_001", 20.0, middle);
        SensorData data2 = new SensorData("SENSOR_002", 30.0, middle);
        SensorData data3 = new SensorData("SENSOR_003", 25.0, middle);

        repository.saveAll(List.of(data1, data2, data3));

        // Test count
        Long count = repository.countDataPointsInDateRange(start, end);

        assertEquals(3L, count);
    }
}
