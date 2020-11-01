package won983212.simpleui;

import java.awt.Rectangle;

public enum HorizontalArrange {
	LEFT,
	RIGHT,
	CENTER,
	STRECTCH;

	/**
	 * 수평 정렬시 실제 위치를 계산합니다.
	 * @param available 사용할 수 있는 영역
	 * @param width Component의 너비
	 */
	public int getHorizontalArrangedLocation(Rectangle available, int width) {
		switch(this) {
		case RIGHT:
			return available.x + available.width - width;
		case CENTER:
			return available.x + (available.width - width) / 2;
		default:
			return available.x;
		}
	}
	
	/**
	 * 수평 정렬시 실제 너비를 계산합니다.
	 * @param available 사용할 수 있는 영역
	 * @param width Component의 너비
	 */
	public int getWidthArranged(Rectangle available, int width) {
		switch(this) {
		case STRECTCH:
			return available.width;
		default:
			return width;
		}
	}
}
