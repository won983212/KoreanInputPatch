package won983212.simpleui;

import java.awt.Dimension;
import java.awt.Rectangle;

public enum HorizontalArrange {
	LEFT,
	RIGHT,
	CENTER,
	STRECTCH;

	/**
	 * 수평 정렬시 실제 위치를 계산합니다.
	 * @param available 사용할 수 있는 영역
	 * @param objectSize Component의 크기
	 */
	public int getHorizontalArrangedLocation(Rectangle available, Dimension objectSize) {
		switch(this) {
		case RIGHT:
			return available.x + available.width - objectSize.width;
		case CENTER:
			return available.x + (available.width - objectSize.width) / 2;
		default:
			return available.x;
		}
	}
	
	/**
	 * 수평 정렬시 실제 너비를 계산합니다.
	 * @param available 사용할 수 있는 영역
	 * @param objectSize Component의 크기
	 */
	public int getWidthArranged(Rectangle available, Dimension objectSize) {
		switch(this) {
		case STRECTCH:
			return available.width;
		default:
			return objectSize.width;
		}
	}
}
