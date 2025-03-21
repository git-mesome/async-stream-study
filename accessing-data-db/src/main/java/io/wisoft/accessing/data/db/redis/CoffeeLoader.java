package io.wisoft.accessing.data.db.redis;


import io.wisoft.accessing.data.db.redis.async.Coffee2;
import io.wisoft.accessing.data.db.redis.async.CoffeeRepository2;
import io.wisoft.accessing.data.db.redis.sync.Coffee;
import io.wisoft.accessing.data.db.redis.sync.CoffeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoffeeLoader {
  @Autowired
  private CoffeeRepository coffeeRepository; // CoffeeRepository 사용

  @Autowired
  private CoffeeRepository2 coffeeRepository2;
//  @Autowired
//  private RedisTemplate<String, Object> redisTemplate; // RedisConfig에서 설정한 빈 사용

  @PostConstruct
  public void loadData() {
    // 기존 데이터 삭제 (선택 사항)
    coffeeRepository.deleteAll();
    coffeeRepository2.deleteAll();

    // 데이터 추가
    Coffee coffee1 = new Coffee(1L, "Jet Black Redis");
    Coffee coffee2 = new Coffee(2L, "Darth Redis");
    Coffee coffee3 = new Coffee(3L, "Black Alert Redis");

    coffeeRepository.save(coffee1); // Repository를 사용하여 저장
    coffeeRepository.save(coffee2);
    coffeeRepository.save(coffee3);

    Coffee2 coffee4= new Coffee2(1L, "Jet Black Redis");
    Coffee2 coffee5 = new Coffee2(2L, "Darth Redis");
    Coffee2 coffee6 = new Coffee2(3L, "Black Alert Redis");

    coffeeRepository2.save(coffee4); // Repository를 사용하여 저장
    coffeeRepository2.save(coffee5);
    coffeeRepository2.save(coffee6);
  }


}
