package com.mnc.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableJpaRepositories
@SpringBootApplication
public class BookingApplication {

  public static void main(String[] args) {
    final ConfigurableApplicationContext applicationContext = SpringApplication.run(BookingApplication.class, args);

    for (final String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
      System.out.println(beanDefinitionName);
    }

  }

}
