package com.won983212.simpleui.animation;

public class IntervalAnimation extends AnimationBase<Double> {
	private double from;
	private double to;

	public IntervalAnimation(int duration) {
		this(duration, 0, 0);
	}
	
	public IntervalAnimation(int duration, double from, double to) {
		super(duration);
		setRange(from, to);
	}
	
	public void setRange(double from, double to) {
		this.from = from;
		this.to = to;
	}

	@Override
	protected Double update(double time) {
		return from + time * (to - from);
	}
}
