package com.geozen.game.stms.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties
//@EnableCaching
//@EnableRedisHttpSession
//@EnableAsync
//@EnableAspectJAutoProxy
//@EnableRedisRepositories(basePackages = "com.sourcecode.malls.repository.redis.impl")
public class CommonConfig {
//	public static final String WEB_CONTEXT = "/s";
	public static final String WEB_CONTEXT = "";

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}