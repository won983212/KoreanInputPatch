package com.won983212.kpatch.ui.animation;

public class DecimalAnimation extends AnimationBase<Double> {
    public DecimalAnimation(int duration) {
        super(duration);
    }

    @Override
    protected Double update(double time) {
        return time;
    }
}
