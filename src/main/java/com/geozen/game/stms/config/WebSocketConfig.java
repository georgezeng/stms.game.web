package com.geozen.game.stms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Value("${access.control.allow.origin}")
	private String origin;

	@Value("${websocket.topic}")
	private String topic;

	@Value("${websocket.destination.prefix}")
	private String prefix;

	@Value("${websocket.endPoint}")
	private String endPoint;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker(CommonConfig.WEB_CONTEXT + topic);
		config.setApplicationDestinationPrefixes(CommonConfig.WEB_CONTEXT + prefix);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(endPoint).setAllowedOrigins(origin).withSockJS();
	}

}