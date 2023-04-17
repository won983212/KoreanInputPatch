package com.won983212.simpleui;

import java.awt.Dimension;
import java.awt.Rectangle;

public class DirWeights {
	public int top;
	public int bottom;
	public int left;
	public int right;

	public DirWeights() {
		this(0, 0, 0, 0);
	}
	
	public DirWeights(int r) {
		this(r, r, r, r);
	}
	
	public DirWeights(int horizontal, int vertical) {
		this(vertical, vertical, horizontal, horizontal);
	}
	
	public DirWeights(int top, int bottom, int left, int right) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}

	/**
	 * 확장된 크기를 계산합니다.
	 */
	public Dimension getExpandedSize(Dimension contentSize) {
		return new Dimension(contentSize.width + left + right, contentSize.height + top + bottom);
	}
	
	/**
	 * 실제 content 영역을 계산합니다.
	 */
	public Rectangle getContentRect(Rectangle rect) {
		return new Rectangle(rect.x + left, rect.y + top, rect.width - left - right, rect.height - top - bottom);
	}
}