package com.geozen.game.stms.domain;

import java.util.List;
import java.util.Stack;

import com.geozen.game.stms.dto.Cards;
import com.geozen.game.stms.enums.Card;
import com.geozen.game.stms.exception.BusinessException;

public class PlayStage {
	private Stack<Card> cards = Cards.shuffle();
	private List<Player> players;
	private int currentPlayerIndex;
	private Card extraGhost = Cards.shuffleForExtraGhost().firstElement();

	public Card getExtraGhost() {
		return extraGhost;
	}

	public void nextTurn() {
		this.currentPlayerIndex++;
	}

	public int getCurrentTurn() {
		return this.currentPlayerIndex % players.size();
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Card sendCard() {
		return cards.pop();
	}

	public void assginCards(int count) {
		for (int i = 0; i < count; i++) {
			for (Player player : players) {
				player.addCards(cards.pop());
			}
		}
	}

	public void fillCard(Player player) {
		if (player.getCards().size() < 3) {
			player.addCards(cards.pop());
			return;
		}
		throw new BusinessException("不能再补牌了");
	}
}
