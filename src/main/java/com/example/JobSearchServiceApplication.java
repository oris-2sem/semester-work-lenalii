package com.example;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;


@SpringBootApplication
@ConfigurationPropertiesScan("com.example")
@EnableScheduling
public class JobSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSearchServiceApplication.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return  new RestTemplate();
    }

}
