package com.geozen.game.stms.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum Card {
	JK1("JK1", "JK1", -1, 0), JK2("JK2", "JK2", -1, 0), //JK3("JK3", "JK3", -1, 0), JK4("JK4", "JK4", -1, 0),
	SA("AS", "S", 1, 1), HA("AH", "H", 1, 1), CA("AC", "C", 1, 1), DA("AD", "D", 1, 1),
	S2("2S", "S", 2, 2), H2("2H", "H", 2, 2), C2("2C", "C", 2, 2), D2("2D", "D", 2, 2),
	S3("3S", "S", 3, 3), H3("3H", "H", 3, 3), C3("3C", "C", 3, 3), D3("3D", "D", 3, 3),
	S4("4S", "S", 4, 4), H4("4H", "H", 4, 4), C4("4C", "C", 4, 4), D4("4D", "D", 4, 4),
	S5("5S", "S", 5, 5), H5("5H", "H", 5, 5), C5("5C", "C", 5, 5), D5("5D", "D", 5, 5),
	S6("6S", "S", 6, 6), H6("6H", "H", 6, 6), C6("6C", "C", 6, 6), D6("6D", "D", 6, 6),
	S7("7S", "S", 7, 7), H7("7H", "H", 7, 7), C7("7C", "C", 7, 7), D7("7D", "D", 7, 7),
	S8("8S", "S", 8, 8), H8("8H", "H", 8, 8), C8("8C", "C", 8, 8), D8("8D", "D", 8, 8),
	S9("9S", "S", 9, 9), H9("9H", "H", 9, 9), C9("9C", "C", 9, 9), D9("9D", "D", 9, 9),
	S10("10S", "S", 0, 10), H10("10H", "H", 0, 10), C10("10C", "C", 0, 10), D10("10D", "D", 0, 10),
	SJ("JS", "S", 0, 11), HJ("JH", "H", 0, 11), CJ("JC", "C", 0, 11), DJ("JD", "D", 0, 11),
	SQ("QS", "S", 0, 12), HQ("QH", "H", 0, 12), CQ("QC", "C", 0, 12), DQ("QD", "D", 0, 12),
	SK("KS", "S", 0, 13), HK("KH", "H", 0, 13), CK("KC", "C", 0, 13), DK("KD", "D", 0, 13)
	
	;
	private String name;
	private String type;
	private int point;
	private int index;
	
	private Card(String name, String type, int point, int index) {
		this.name = name;
		this.type = type;
		this.point = point;
		this.index = index;
	}

	public String getName() {
		return name;
	}


	public String getType() {
		return type;
	}


	public int getPoint() {
		return point;
	}

	public int getIndex() {
		return index;
	}

}
