package won983212.kpatch.ui.animation;

public class RatioAnimation implements IAnimation<Double> {
	// animation duration. (millisecond)
	private int duration;
	private long startTime = -1;
	private boolean isRunning = false;
	private boolean isReverse = false;
	
	public RatioAnimation(int duration) {
		this.duration = duration;
	}

	@Override
	public void setReverse(boolean reverse) {
		isReverse = reverse;
	}
	
	@Override
	public void play() {
		startTime = System.currentTimeMillis();
		isRunning = true;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
	
	@Override
	public Double update() {
		if(startTime == -1 || !isRunning)
			return getInitialFrame();
		
		long cur = System.currentTimeMillis();
		if(cur - startTime > duration) {
			isRunning = false;
			return getInitialFrame();
		} else {
			final double x = (cur - startTime) / (double) duration;
			final double y = 1-(1-x)*(1-x)*(1-x);
			return isReverse ? 1-y : y;
		}
	}
	
	public double getInitialFrame() {
		return isReverse ? 0 : 1.0;
	}
	
	public int getInitialIntValue(int start, int end) {
		return (int)(start + (end - start) * getInitialFrame());
	}
}
