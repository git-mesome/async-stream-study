package io.wisoft.accessing.data.db.sync.redis;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("coffee")
public class Coffee implements Serializable {

  @Id
  private Long id;
  private String name;

  public Coffee() {} // 기본 생성자 필요 (직렬화/역직렬화를 위해)

  public Coffee(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
