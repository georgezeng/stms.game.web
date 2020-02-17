package com.geozen.game.stms.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import com.geozen.game.stms.enums.PlayerStatus;
import com.geozen.game.stms.enums.RoomStatus;
import com.geozen.game.stms.exception.BusinessException;

public class Room {
	private String number;
	private String host;
	private RoomStatus status;
	private Set<Player> players = new LinkedHashSet<>();
	private PlayStage stage;

	public PlayStage getStage() {
		return stage;
	}

	public void setStage(PlayStage stage) {
		this.stage = stage;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public Player getPlayer(String nickname) {
		for (Player player : players) {
			if (player.getNickname().equals(nickname)) {
				return player;
			}
		}
		return null;
	}

	public RoomStatus getStatus() {
		return status;
	}

	public void setStatus(RoomStatus status) {
		this.status = status;
	}

	public boolean isHost(String nickname) {
		return host.equals(nickname);
	}

	public boolean isOccupied(String nickname) {
		return players.contains(new Player(nickname));
	}

	public void removePlayer(String nickname) {
		players.remove(new Player(nickname));
	}

	public void addPlayer(String nickname) {
		if (players.size() > 9) {
			throw new BusinessException("无法进入房间，人员已满");
		}
		players.add(new Player(players.size(), nickname));
		Player player = getPlayer(nickname);
		if (PlayerStatus.Exit.equals(player.getStatus())) {
			player.setStatus(PlayerStatus.Locked);
		} else {
			player.reset();
			if (RoomStatus.Calculated.equals(status)) {
				player.setStatus(PlayerStatus.Locked);
			}
		}
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Set<Player> getPlayers() {
		return players;
	}

}
