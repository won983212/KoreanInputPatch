package com.won983212.simpleui;

public class Arranges {
	public static final int TL = 0;
	public static final int TC = 1;
	public static final int TR = 2;
	public static final int TS = 3;
	public static final int CL = 4;
	public static final int CC = 5;
	public static final int CR = 6;
	public static final int CS = 7;
	public static final int BL = 8;
	public static final int BC = 9;
	public static final int BR = 10;
	public static final int BS = 11;
	public static final int SL = 12;
	public static final int SC = 13;
	public static final int SR = 14;
	public static final int SS = 15;
	
	private static final HorizontalArrange[] horizontalTemplates = { HorizontalArrange.LEFT, HorizontalArrange.CENTER,
			HorizontalArrange.RIGHT, HorizontalArrange.STRETCH};

	private static final VerticalArrange[] verticalTemplates = { VerticalArrange.TOP, VerticalArrange.CENTER,
			VerticalArrange.BOTTOM, VerticalArrange.STRETCH};
	
	public static HorizontalArrange getHorizontalArrangeByTemplate(int temp) {
		return horizontalTemplates[temp % 4];
	}
	
	public static VerticalArrange getVerticalArrangeByTemplate(int temp) {
		return verticalTemplates[temp / 4];
	}
}
