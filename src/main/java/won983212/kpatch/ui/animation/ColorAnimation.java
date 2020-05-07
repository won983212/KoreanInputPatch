package won983212.kpatch.ui.animation;

public class ColorAnimation implements IAnimation<Integer> {
	private int start, end;
	private int[] color = new int[8];
	private RatioAnimation animation;

	public ColorAnimation(int start, int end, int duration) {
		this.start = start;
		this.end = end;
		this.animation = new RatioAnimation(duration);
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
	public void setReverse(boolean reverse) {
		animation.setReverse(reverse);
	}

	@Override
	public void play() {
		animation.play();
	}

	@Override
	public boolean isRunning() {
		return animation.isRunning();
	}

	@Override
	public Integer update() {
		if(!animation.isRunning())
			return animation.getInitialIntValue(start, end);
		
		final double p = animation.update();
		int res = 0;
		for(int i=0;i<4;i++) {
			res += (int)(color[i] + (color[i+4] - color[i]) * p) << ((3-i) * 8);
		}
		return res;
	}
}
