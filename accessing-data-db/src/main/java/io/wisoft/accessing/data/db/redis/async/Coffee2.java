package io.wisoft.accessing.data.db.redis.async;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("coffee2")
public class Coffee2 {

  @Id
  private String id;
  private String name;

  public Coffee2() {} // 기본 생성자 필요 (직렬화/역직렬화를 위해)

  public Coffee2(String id, String name) {
    this.id = id;
    this.name = name;
  }

  // Getters and setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
