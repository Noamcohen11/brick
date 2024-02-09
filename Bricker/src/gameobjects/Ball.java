package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class represting a gameobject - the ball.
 */
public class Ball extends GameObject {

    private final Sound collisionSound;
    private int collisionCounter;

    /**
     * Construct a new GameObject instance of the ball.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in
     *                      which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        collisionCounter = 0;
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
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionCounter++;
    }

    /**
     * brief Get the number of collisions the ball has had.
     * 
     * @return Int - The number of collisions the ball has had.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

}
