package gameobjects;

import danogl.util.Counter;

public class LifeCounter extends Counter {
    private final int maxValue;
    public LifeCounter(int maxValue, int initValue) {
        super(initValue);
        this.maxValue = maxValue;
    }
    @Override
    public void increment() {
        if (super.value() < maxValue)
        {
            super.increment();
        }
    }
}
