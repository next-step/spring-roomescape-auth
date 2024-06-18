package roomescape;

import roomescape.config.JwtProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ JwtProperties.class })
@SpringBootApplication
public class RoomescapeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomescapeApplication.class, args);
	}

}
