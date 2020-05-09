package won983212.kpatch.ui.animation;

public class ColorAnimation extends AnimationBase<Integer> {
	private int start, end;
	private int[] color = new int[8];

	public ColorAnimation(int start, int end, int duration) {
		super(duration);
		this.start = start;
		this.end = end;
		unpackColor(start, 0);
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
