package com.geozen.game.stms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.geozen.game.stms.config.CommonConfig;
import com.geozen.game.stms.domain.Room;
import com.geozen.game.stms.dto.ResultBean;
import com.geozen.game.stms.service.PlayService;

@Controller
public class PlayWSController {
	@Autowired
	private PlayService service;

	@MessageMapping("/getRoomInfo")
	@SendTo(CommonConfig.WEB_CONTEXT + "/topic/room")
	public ResultBean<Room> getRoomInfo(ResultBean<String> payload) throws Exception {
		return new ResultBean<>(service.get(payload.getData()));
	}

}