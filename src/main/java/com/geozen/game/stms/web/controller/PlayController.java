package com.geozen.game.stms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geozen.game.stms.domain.Room;
import com.geozen.game.stms.dto.ResultBean;
import com.geozen.game.stms.service.PlayService;

@RestController
public class PlayController {
	@Autowired
	private PlayService service;

	@RequestMapping(path = "/createRoom/{nickname}")
	public ResultBean<String> create(@PathVariable("nickname") String nickname) {
		return new ResultBean<>(service.create(nickname));
	}

	@RequestMapping(path = "/getRoomInfo/{roomNumber}")
	public ResultBean<Room> getRoomInfo(@PathVariable("roomNumber") String roomNumber) {
		return new ResultBean<>(service.get(roomNumber));
	}

	@RequestMapping(path = "/joinRoom/{roomNumber}/{nickname}")
	public ResultBean<Void> join(@PathVariable("roomNumber") String roomNumber, @PathVariable("nickname") String nickname) {
		service.join(roomNumber, nickname);
		return new ResultBean<>();
	}

	@RequestMapping(path = "/exitRoom/{roomNumber}/{nickname}")
	public ResultBean<Void> exit(@PathVariable("roomNumber") String roomNumber, @PathVariable("nickname") String nickname) {
		service.exit(roomNumber, nickname);
		return new ResultBean<>();
	}

	@RequestMapping(path = "/startGame/{roomNumber}/{nickname}")
	public ResultBean<Void> startGame(@PathVariable("roomNumber") String roomNumber, @PathVariable("nickname") String nickname) {
		service.startGame(roomNumber, nickname);
		return new ResultBean<>();
	}

	@RequestMapping(path = "/fullfill/{roomNumber}/{nickname}")
	public ResultBean<Void> fullfill(@PathVariable("roomNumber") String roomNumber, @PathVariable("nickname") String nickname) {
		service.fullfill(roomNumber, nickname);
		return new ResultBean<>();
	}

	@RequestMapping(path = "/endGame/{roomNumber}/{nickname}")
	public ResultBean<Void> endGame(@PathVariable("roomNumber") String roomNumber, @PathVariable("nickname") String nickname) {
		service.endGame(roomNumber, nickname);
		return new ResultBean<>();
	}

	@RequestMapping(path = "/closeRoom/{roomNumber}/{nickname}")
	public ResultBean<Void> close(@PathVariable("roomNumber") String roomNumber, @PathVariable("nickname") String nickname) {
		service.close(roomNumber, nickname);
		return new ResultBean<>();
	}

	@RequestMapping(path = "/lockCards/{roomNumber}/{nickname}")
	public ResultBean<Void> lockCards(@PathVariable("roomNumber") String roomNumber, @PathVariable("nickname") String nickname) {
		service.lock(roomNumber, nickname);
		return new ResultBean<>();
	}

	@RequestMapping(path = "/calResult/{roomNumber}/{nickname}")
	public ResultBean<Void> calResult(@PathVariable("roomNumber") String roomNumber, @PathVariable("nickname") String nickname) {
		service.calResult(roomNumber, nickname);
		return new ResultBean<>();
	}

}
