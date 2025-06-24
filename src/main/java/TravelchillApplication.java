import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"controller", "service", "dao","entity","config"})
public class TravelchillApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelchillApplication.class, args);
	}

}
