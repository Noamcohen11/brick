package gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class represting a gameobject - the brick.
 */
public class Brick extends GameObject {
    /**
     * Construct a new GameObject instance
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in
     *                      which case
     *                      the GameObject will not be rendered.
     */
    private CollisionStrategy collisionStrategy;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
            CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * When a collusion is detected this function will be called.
     *
     * @param other     The object that the GameObject collided with.
     * @param collision The collision object containing information about the
     *                  collision.
     * 
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }
}
