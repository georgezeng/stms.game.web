package com.geozen.game.stms.domain;

import java.util.ArrayList;
import java.util.List;

import com.geozen.game.stms.enums.Card;
import com.geozen.game.stms.enums.CardTimes;
import com.geozen.game.stms.enums.PlayerStatus;

public class Player {
	private int index;
	private String nickname;
	private PlayerStatus status;
	private int amount;
	private int stageAmount;
	private List<Card> cards = new ArrayList<>();
	private CardTimes times;
	private int points;

	public Player(int index, String nickname) {
		this.index = index;
		this.nickname = nickname;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getStageAmount() {
		return stageAmount;
	}

	public void setStageAmount(int stageAmount) {
		this.stageAmount = stageAmount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public CardTimes getTimes() {
		return times;
	}

	public void setTimes(CardTimes times) {
		this.times = times;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public int getIndex() {
		return index;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void addCards(Card card) {
		this.cards.add(card);
	}

	public Player(String nickname) {
		this(-1, nickname);
	}

	public void reset() {
		cards.clear();
		stageAmount = 0;
		times = null;
		points = 0;
		status = PlayerStatus.Ready;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		return nickname.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Player) {
			Player p = (Player) o;
			return nickname.equals(p.nickname);
		}
		return false;
	}
}
