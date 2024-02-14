package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;
import main.BrickerGameManager;

public abstract class BaseStrategyDecorator implements CollisionStrategy{
    private final CollisionStrategy wrapped;

    public BaseStrategyDecorator(BrickerGameManager gameManager, Counter brickCounter,CollisionStrategy wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * When a collusion is detected this function will be called.
     *
     * @param fObj The first object that the GameObject collided with.
     * @param sObj The second object that the GameObject collided with.
     */
    @Override
    public void onCollision(GameObject fObj, GameObject sObj) {
        wrapped.onCollision(fObj, sObj);
    }
}