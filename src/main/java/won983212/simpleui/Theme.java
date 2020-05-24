package won983212.simpleui;

public class Theme {
	public static final int WHITE = 0xffffffff;
	public static final int BLACK = 0xff000000;
	public static final int LIGHT_GRAY = 0xffaaaaaa;
	public static final int DARK_GRAY = 0xff555555;
	public static final int GRAY = 0xff999999;
	
	public static final int PRIMARY = 0xff308FBF;
	public static final int PRIMARY_LIGHTEST;
	
	public static final int SECONDARY = 0xffE53935;
	public static final int SECONDARY_LIGHTEST;
	
	public static final int WARN = 0xfffb8c00;
	public static final int DANGER = 0xffe53935;
	public static final int SUCCESS = 0xff4caf50;
	public static final int BACKGROUND = WHITE;
	public static final int BACKGROUND_SHADOW = DARK_GRAY;
	
	static {
		PRIMARY_LIGHTEST = adjustColor(PRIMARY, 70);
		SECONDARY_LIGHTEST = adjustColor(SECONDARY, 70);
	}
	
	public static int adjustColor(int color, int offset) {
		int a = (color >> 24) & 0xff;
		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = color & 0xff;
		
		if(offset > 0) {
			r += Math.min(0xff, (0xff - r) * (offset / 100.0));
			g += Math.min(0xff, (0xff - g) * (offset / 100.0));
			b += Math.min(0xff, (0xff - b) * (offset / 100.0));
		} else {
			r -= Math.max(0, r * (offset / 100.0));
			g -= Math.max(0, g * (offset / 100.0));
			b -= Math.max(0, b * (offset / 100.0));
		}
		
		return (a << 24) + (r << 16) + (g << 8) + b;
	}
}
