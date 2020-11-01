package won983212.simpleui;

import java.awt.Rectangle;

public enum VerticalArrange {
	TOP,
	BOTTOM,
	CENTER,
	STRECTCH;

	/**
	 * 수직 정렬시 실제 위치를 계산합니다.
	 * @param available 사용할 수 있는 영역
	 * @param height Component의 높이
	 */
	public int getVerticalArrangedLocation(Rectangle available, int height) {
		switch(this) {
		case BOTTOM:
			return available.y + available.height - height;
		case CENTER:
			return available.y + (available.height - height) / 2;
		default:
			return available.y;
		}
	}
	
	/**
	 * 수직 정렬시 실제 높이를 계산합니다.
	 * @param available 사용할 수 있는 영역
	 * @param height Component의 높이
	 */
	public int getHeightArranged(Rectangle available, int height) {
		switch(this) {
		case STRECTCH:
			return available.height;
		default:
			return height;
		}
	}
}