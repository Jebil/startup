package net.jk.app.startup.aws;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FederatedCredsGenerator {

  @Autowired private AWSSecurityTokenService tokenService;

  @Value("${app.federated.user.name.length:10}")
  int federatedUserNameLength;

  @Value("${app.federated.user.duration.seconds:7200}")
  int federatedUserDurationSeconds;

  public BasicSessionCredentials generateFederatedUserCreds(Policy policy) {
    return generateFederatedUserCreds(policy, federatedUserDurationSeconds);
  }

  public BasicSessionCredentials generateFederatedUserCreds(Policy policy, int duration) {

    // Create request
    String federatedUser = RandomStringUtils.random(federatedUserNameLength);
    GetFederationTokenRequest federationTokenRequest = new GetFederationTokenRequest();
    federationTokenRequest.setDurationSeconds(duration);
    federationTokenRequest.setName(federatedUser);
    federationTokenRequest.setPolicy(policy.toJson());

    // Get the temporary security credentials.
    GetFederationTokenResult federationTokenResult =
        tokenService.getFederationToken(federationTokenRequest);
    Credentials sessionCredentials = federationTokenResult.getCredentials();

    // Package the session credentials as a BasicSessionCredentials object
    return new BasicSessionCredentials(
        sessionCredentials.getAccessKeyId(),
        sessionCredentials.getSecretAccessKey(),
        sessionCredentials.getSessionToken());
  }
}
