package gameobjects;

import danogl.util.Counter;

/**
 * Class representing a counter - the life counter.
 */
public class LifeCounter extends Counter {
    private final int maxValue;
    /**
     * Construct a new Counter instance of the lifeCounter.
     *
     * @param maxValue the maximum value the counter can hold
     * @param initValue    initial value for the counter

     */
    public LifeCounter(int maxValue, int initValue) {
        super(initValue);
        this.maxValue = maxValue;
    }
    /**
     * increments the live counter value by one.
     */
    @Override
    public void increment() {
        if (super.value() < maxValue)
        {
            super.increment();
        }
    }
}
