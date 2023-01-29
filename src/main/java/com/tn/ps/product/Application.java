package com.tn.ps.product;

import static com.tn.service.api.config.PropertyLogger.sensitive;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.tn.service.api.config.PropertyLogger;

@SpringBootApplication
@EnableJpaRepositories
public class Application
{
  private static final String REGEX_PASSWORD = ".*password.*";
  private static final String REGEX_SECRET = ".*secret.*";

  public static void main(String[] args)
  {
    PropertyLogger propertyLogger = new PropertyLogger(
      LoggerFactory.getLogger(Application.class),
      sensitive(REGEX_PASSWORD),
      sensitive(REGEX_SECRET)
    );

    SpringApplication application = new SpringApplication(Application.class);
    application.addListeners(propertyLogger);
    application.run(args);
  }
}
