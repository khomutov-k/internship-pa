package ru.innopolis.process_automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class InternshipPaApplication {

  public static void main(String[] args) {
    SpringApplication.run(InternshipPaApplication.class, args);
  }

  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplateBuilder builder = new RestTemplateBuilder();
    return builder.build();
  }

}
