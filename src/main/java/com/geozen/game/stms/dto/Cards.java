package com.geozen.game.stms.dto;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import com.geozen.game.stms.enums.Card;

public class Cards {
	private static final Set<Card> cards = new LinkedHashSet<>();
	static {
		cards.add(Card.SA);
		cards.add(Card.HA);
		cards.add(Card.CA);
		cards.add(Card.DA);

		cards.add(Card.S2);
		cards.add(Card.H2);
		cards.add(Card.C2);
		cards.add(Card.D2);

		cards.add(Card.S3);
		cards.add(Card.H3);
		cards.add(Card.C3);
		cards.add(Card.D3);

		cards.add(Card.S4);
		cards.add(Card.H4);
		cards.add(Card.C4);
		cards.add(Card.D4);

		cards.add(Card.S5);
		cards.add(Card.H5);
		cards.add(Card.C5);
		cards.add(Card.D5);

		cards.add(Card.S6);
		cards.add(Card.H6);
		cards.add(Card.C6);
		cards.add(Card.D6);

		cards.add(Card.S7);
		cards.add(Card.H7);
		cards.add(Card.C7);
		cards.add(Card.D7);

		cards.add(Card.S8);
		cards.add(Card.H8);
		cards.add(Card.C8);
		cards.add(Card.D8);

		cards.add(Card.S9);
		cards.add(Card.H9);
		cards.add(Card.C9);
		cards.add(Card.D9);

		cards.add(Card.S10);
		cards.add(Card.H10);
		cards.add(Card.C10);
		cards.add(Card.D10);

		cards.add(Card.SJ);
		cards.add(Card.HJ);
		cards.add(Card.CJ);
		cards.add(Card.DJ);

		cards.add(Card.SQ);
		cards.add(Card.HQ);
		cards.add(Card.CQ);
		cards.add(Card.DQ);

		cards.add(Card.SK);
		cards.add(Card.HK);
		cards.add(Card.CK);
		cards.add(Card.DK);

	}

	private static final Set<Card> cardsWithJokers = new LinkedHashSet<>(cards);
	static {

		cardsWithJokers.add(Card.JK1);
		cardsWithJokers.add(Card.JK2);
	}

	public static Stack<Card> shuffle() {
		Stack<Card> stack = new Stack<>();
		cardsWithJokers.stream().forEach(card -> {
			stack.push(card);
		});
		Collections.shuffle(stack);
		return stack;
	}

	public static Stack<Card> shuffleForExtraGhost() {
		Stack<Card> stack = new Stack<>();
		cards.stream().forEach(card -> {
			stack.push(card);
		});
		Collections.shuffle(stack);
		return stack;
	}

}
