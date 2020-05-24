package won983212.simpleui.animation;

public abstract class AnimationBase<T> {
	// animation compile types
	public static final int COMPILE_QUADRATIC_EASE_OUT = 1;
	public static final int COMPILE_MOUNTAIN = 2;
	
	// animation duration. (millisecond)
	private int duration;
	private long startTime = -1;
	private boolean isRunning = false;
	private boolean isReverse = false;
	private int compileType = COMPILE_QUADRATIC_EASE_OUT;
	
	public AnimationBase(int duration) {
		this.duration = duration;
	}

	public void setReverse(boolean reverse) {
		isReverse = reverse;
	}
	
	public void play() {
		startTime = System.currentTimeMillis();
		isRunning = true;
	}

	public boolean isRunning() {
		return isRunning;
	}
	
	public void setCompileType(int type) {
		compileType = type;
	}
	
	private double updateTime() {
		if(startTime == -1 || !isRunning)
			return getInitialFrame();
		
		long cur = System.currentTimeMillis();
		if(cur - startTime > duration) {
			isRunning = false;
			return getInitialFrame();
		} else {
			final double x = (cur - startTime) / (double) duration;
			double y = x;
			switch(compileType) {
				case COMPILE_QUADRATIC_EASE_OUT:
					y = 1-(1-x)*(1-x)*(1-x);
					break;
				case COMPILE_MOUNTAIN:
					y = -4*(x-1)*x;
					break;
			}
			return isReverse ? 1-y : y;
		}
	}
	
	public T update() {
		return update(updateTime());
	}
	
	protected abstract T update(double time);
	
	public double getInitialFrame() {
		switch(compileType) {
			case COMPILE_QUADRATIC_EASE_OUT:
				return isReverse ? 0 : 1;
			case COMPILE_MOUNTAIN:
				return 0;
		}
		return 0;
	}
}
