package com.geozen.game.stms.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum CardTimes {
	DoubleGhost("无敌双鬼", 10, 1),
	DoubleGod("双倍天公", 2, 2),
	God("天公", 1, 3),
	TrippleGhost("三鬼", 20, 4), 
	Flush("同花顺", 8, 5),
	SamePoints("三条", 5, 6),
	Straight("顺子", 4, 7),
	TrippleSameType("三倍", 3, 8),
	DoubleSameType("两倍", 2, 8),
	General("普通", 1, 8),
	Bug("木虱", 0, 9)
	;
	private String name;
	private int value;
	private int priority;

	private CardTimes(String name, int value, int priority) {
		this.name = name;
		this.value = value;
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public int getPriority() {
		return priority;
	}

}
