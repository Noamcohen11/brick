package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Basic collision strategy - removes the object from the game.
 * 
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    private GameObjectCollection gameObjects;
    private Counter brickCounter;
    // The layer of the game object - as defined in the forum post.
    private static final int layer = Layer.STATIC_OBJECTS;

    /**
     * Construct a new BasicCollisionStrategy instance.
     *
     * @param gameObjects The game objects collection to remove the object from.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter brickCounter) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
    }

    /**
     * When a collusion is detected this function will be called.
     *
     * @param fObj The first object that the GameObject collided with.
     * @param sObj The second object that the GameObject collided with.
     */
    @Override
    public void onCollision(GameObject fObj, GameObject sObj) {
        this.gameObjects.removeGameObject(fObj, layer);
        this.brickCounter.decrement();
    }
}
