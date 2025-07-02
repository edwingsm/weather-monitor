package ie.weather.monitor.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.weather.monitor.WeatherMonitorApplication;
import ie.weather.monitor.dto.SensorDataRequest;
import ie.weather.monitor.repository.SensorDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WeatherMonitorApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class SensorDataIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SensorDataRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        repository.deleteAll(); // Clean database before each test
    }

    @Test
    public void testCompleteWorkflow() throws Exception {
        // 1. Register sensor data
        SensorDataRequest request1 = new SensorDataRequest("SENSOR_001", 23.5);
        request1.setTimestamp(LocalDateTime.of(2024, 1, 15, 12, 0));

        SensorDataRequest request2 = new SensorDataRequest("SENSOR_001", 25.0);
        request2.setTimestamp(LocalDateTime.of(2024, 1, 16, 12, 0));

        SensorDataRequest request3 = new SensorDataRequest("SENSOR_002", 20.0);
        request3.setTimestamp(LocalDateTime.of(2024, 1, 15, 12, 0));

        // Register all data points
        mockMvc.perform(post("/api/sensor-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/sensor-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/sensor-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request3)))
                .andExpect(status().isCreated());

        // 2. Verify data is stored
        assertEquals(3, repository.count());

        // 3. Test average for all sensors
        mockMvc.perform(get("/api/sensor-data/average/temperature")
                        .param("startDate", "2024-01-01T00:00:00")
                        .param("endDate", "2024-01-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metric").value("temperature"))
                .andExpect(jsonPath("$.averageValue").value(22.833333333333332)) // (23.5 + 25.0 + 20.0) / 3
                .andExpect(jsonPath("$.dataPointCount").value(3));

        // 4. Test average for specific sensor
        mockMvc.perform(get("/api/sensor-data/sensor/SENSOR_001/average/temperature")
                        .param("startDate", "2024-01-01T00:00:00")
                        .param("endDate", "2024-01-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metric").value("temperature"))
                .andExpect(jsonPath("$.averageValue").value(24.25)) // (23.5 + 25.0) / 2
                .andExpect(jsonPath("$.sensorId").value("SENSOR_001"))
                .andExpect(jsonPath("$.dataPointCount").value(2));
    }

    @Test
    public void testNoDataFoundScenario() throws Exception {
        // Test query with no data in range
        mockMvc.perform(get("/api/sensor-data/average/temperature")
                        .param("startDate", "2025-01-01T00:00:00")
                        .param("endDate", "2025-01-31T23:59:59"))
                .andExpect(status().isNotFound());
    }
}