package won983212.kpatch.ui.animation;

public class IntAnimation implements IAnimation<Integer> {
	private int start;
	private int end;
	private RatioAnimation animation;
	
	public IntAnimation(int start, int end, int duration) {
		this.start = start;
		this.end = end;
		this.animation = new RatioAnimation(duration);
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
		
		final double y = animation.update();
		return (int) (Math.min((1 - y) * start + y * end, end));
	}
}
