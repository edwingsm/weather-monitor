package ie.weather.monitor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.weather.monitor.dto.SensorDataRequest;
import ie.weather.monitor.entity.SensorData;
import ie.weather.monitor.service.SensorDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SensorDataController.class)
public class SensorDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorDataService sensorDataService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterSensorData() throws Exception {
        // Prepare test data
        SensorDataRequest request = new SensorDataRequest("SENSOR_001", Double.valueOf(23.5));
        SensorData savedData = new SensorData("SENSOR_001", Double.valueOf( 23.5),LocalDateTime.now());
        savedData.setId(Long.valueOf(1L));

        when(sensorDataService.registerSensorData(any(SensorDataRequest.class)))
                .thenReturn(savedData);

        // Perform POST request
        mockMvc.perform(post("/api/sensor-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sensorId").value("SENSOR_001"))
                .andExpect(jsonPath("$.temperature").value(Double.valueOf(23.5)));
    }

    @Test
    public void testRegisterSensorDataValidationFailure() throws Exception {
        // Test with missing required fields
        SensorDataRequest request = new SensorDataRequest();

        mockMvc.perform(post("/api/sensor-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}