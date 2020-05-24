package won983212.simpleui.animation;

public class ColorAnimation extends AnimationBase<Integer> {
	private int start, end;
	private int[] color = new int[8];

	public ColorAnimation(int duration) {
		super(duration);
	}
	
	public ColorAnimation(int duration, int start, int end) {
		this(duration);
		setStartColor(start);
		setEndColor(end);
	}

	public void setStartColor(int color) {
		this.start = color;
		unpackColor(start, 0);
	}

	public void setEndColor(int color) {
		this.end = color;
		unpackColor(end, 4);
	}
	
	private void unpackColor(int val, int index) {
		color[index] = (val >> 24) & 255;
		color[index + 1] = (val >> 16) & 255;
		color[index + 2] = (val >> 8) & 255;
		color[index + 3] = val & 255;
	}

	@Override
	protected Integer update(double time) {
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res += (int) (color[i] + (color[i + 4] - color[i]) * time) << ((3 - i) * 8);
		}
		return res;
	}
}
