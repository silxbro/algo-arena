package algo_arena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class AlgoArenaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgoArenaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		//TODO: 세션정보 or 스프링 시큐리티 로그인 정보에서 id 받기
		return null;
	}

}