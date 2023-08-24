package com.tvntvn.letsplay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "dummy")
public class DummyUserProperties {

  @Setter @Getter private String user1Name;
  @Setter @Getter private String user1Email;
  @Setter @Getter private String user1Password;

  @Setter @Getter private String user2Name;
  @Setter @Getter private String user2Email;
  @Setter @Getter private String user2Password;

  @Setter @Getter private String user3Name;
  @Setter @Getter private String user3Email;
  @Setter @Getter private String user3Password;
}
