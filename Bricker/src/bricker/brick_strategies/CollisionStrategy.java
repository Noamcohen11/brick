package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Interface for the collision strategy.
 */
public interface CollisionStrategy {
    /**
     * When a collusion is detected this function will be called.
     *
     * @param fObj The first object that the GameObject collided with.
     * @param sObj The second object that the GameObject collided with.
     */
    public void onCollision(GameObject fObj, GameObject sObj);
}
