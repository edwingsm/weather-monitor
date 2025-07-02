package ie.weather.monitor.repository;


import ie.weather.monitor.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    // Find data for specific sensor in date range
    List<SensorData> findBySensorIdAndTimestampBetween(
            String sensorId, LocalDateTime startDate, LocalDateTime endDate);

    // Find all data in date range
    List<SensorData> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Custom queries for average calculations
    @Query("SELECT AVG(s.temperature) FROM SensorData s WHERE s.timestamp BETWEEN :startDate AND :endDate")
    Double findAverageTemperatureInDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(s.temperature) FROM SensorData s WHERE s.sensorId = :sensorId AND s.timestamp BETWEEN :startDate AND :endDate")
    Double findAverageTemperatureBySensorInDateRange(
            @Param("sensorId") String sensorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) FROM SensorData s WHERE s.timestamp BETWEEN :startDate AND :endDate")
    Long countDataPointsInDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) FROM SensorData s WHERE s.sensorId = :sensorId AND s.timestamp BETWEEN :startDate AND :endDate")
    Long countDataPointsBySensorInDateRange(
            @Param("sensorId") String sensorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}