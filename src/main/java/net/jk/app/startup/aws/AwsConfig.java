package net.jk.app.startup.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AwsConfig {

  @Bean
  public Regions awsRegion() {
    return Regions.AP_SOUTH_1;
  }

  @Bean
  @Primary
  public AWSCredentialsProvider defaultCredentialsProvider() {
    return new DefaultAWSCredentialsProviderChain();
  }

  @Bean
  @Qualifier("s3_uploader")
  public AWSCredentialsProvider credentialsProvider() {
    return new ProfileCredentialsProvider("s3_uploader");
  }

  @Bean
  public AWSSecurityTokenService stsClient(
      @Autowired Regions clientRegion,
      @Autowired @Qualifier("s3_uploader") AWSCredentialsProvider credentialsProvider) {
    return AWSSecurityTokenServiceClientBuilder.standard()
        .withCredentials(credentialsProvider)
        .withRegion(clientRegion)
        .build();
  }
}
