package com.debug;

import java.awt.FontMetrics;
import java.util.Arrays;
import java.util.List;

public class FontRendererInterface {
	public final int FONT_HEIGHT = 23;
	private FontMetrics ctx;

	public void setContext(FontMetrics ctx) {
		this.ctx = ctx;
	}

	public int getStringWidth(String text) {
		return ctx.stringWidth(text);
	}

	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		return Arrays.<String>asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
	}

	private String wrapFormattedStringToWidth(String str, int wrapWidth) {
		StringBuilder sb = new StringBuilder();
		int size = 0;
		for(char c : str.toCharArray()) {
			size += ctx.charWidth(c);
			if(size > wrapWidth) {
				sb.append('\n');
				size = 0;
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
