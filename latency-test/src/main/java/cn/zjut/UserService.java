package cn.zjut;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = {"users"})
public class UserService {
	@Cacheable
	public User getUserByCountry(String country) {
		// simulate database search
		try {
			Thread.sleep(200);
		}catch (Exception e){
			e.printStackTrace();
		}
		return new User(country);
	}
}
