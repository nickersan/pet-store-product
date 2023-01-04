package com.tn.ps.product.config;

import static com.tn.service.api.config.PropertyLogger.sensitive;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tn.ps.product.Application;
import com.tn.service.api.config.PropertyLogger;

@Configuration
class LoggingConfiguration
{
  private static final String REGEX_PASSWORD = ".*password.*";
  private static final String REGEX_SECRET = ".*secret.*";

  @Bean
  PropertyLogger propertyLogger()
  {
    return new PropertyLogger(
      LoggerFactory.getLogger(Application.class),
      sensitive(REGEX_PASSWORD),
      sensitive(REGEX_SECRET)
    );
  }
}
