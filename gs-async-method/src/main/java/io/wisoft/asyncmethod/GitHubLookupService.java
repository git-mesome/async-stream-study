package io.wisoft.asyncmethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubLookupService {

  private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);
  private final RestTemplate restTemplate;


  public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public User findUser(String user) throws InterruptedException {
    logger.info("Looking up v1 " + user);

    String url = String.format("https://api.github.com/users/%s", user);
    User results = restTemplate.getForObject(url, User.class);

    Thread.sleep(100);


    return results;

  }

  public User findUserV2(String user) throws InterruptedException {
    logger.info("Looking up v2 " + user);

    String url = String.format("https://api.github.com/users/%s", user);
    User results = restTemplate.getForObject(url, User.class);

    Thread.sleep(100);


    return results;

  }
}