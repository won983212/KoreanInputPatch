package won983212.kpatch.ui;

public class Theme {
	public static final int PRIMARY = 0xff308FBF;
	public static final int PRIMARY_LIGHTEST;
	
	public static final int SECONDARY = 0xffE53935;
	public static final int SECONDARY_LIGHTEST;
	
	public static final int WARN = 0xfffb8c00;
	public static final int DANGER = 0xffe53935;
	public static final int BACKGROUND = 0xffffffff;
	public static final int BACKGROUND_SHADOW = 0xff666666;
	
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
