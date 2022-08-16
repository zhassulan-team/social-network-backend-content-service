package kata.academy.eurekacontentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EurekaContentService {

    public static void main(String[] args) {
        SpringApplication.run(EurekaContentService.class, args);
    }
}
