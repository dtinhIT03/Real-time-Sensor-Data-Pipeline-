package org.example.kepco_kafka_druid_webportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KepcoKafkaDruidWebPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(KepcoKafkaDruidWebPortalApplication.class, args);
    }

}
