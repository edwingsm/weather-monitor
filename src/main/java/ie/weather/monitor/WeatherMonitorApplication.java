package ie.weather.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories("ie.weather.monitor.*")
//@ComponentScan(basePackages = { "ie.weather.monitor.*" })
//@EntityScan("ie.weather.monitor.*")
public class WeatherMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherMonitorApplication.class, args);
	}

}
