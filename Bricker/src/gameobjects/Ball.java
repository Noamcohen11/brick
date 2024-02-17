package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import main.BrickerGameManager;
import main.Constants;

import java.util.Random;

/**
 * Class represting a gameobject - the ball.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private final BrickerGameManager gameManager;
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
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound,  BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        collisionCounter = 0;
        this.gameManager = gameManager;
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
     * sets initial state of the ball.
     *
     * @param center     a vector2 representing the coordinates of the ball

     */
    public void setBallStartingMovement(Vector2 center) {
        this.setCenter(center);
        Random rand = new Random();
        double angle = rand.nextDouble() * Math.PI;
        float ballVelX = (float) Math.cos(angle) * Constants.BALL_VELOCITY;
        float ballVelY = (float) Math.sin(angle) * Constants.BALL_VELOCITY;
        this.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Called once per frame. Any logic is put here. Rendering, on the other hand,
     * should only be done within 'render'.
     * Note that the time that passes between subsequent calls to this method is not constant.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getTag().equals((Constants.PUCK_TAG)) && gameManager.outOfBound(this)) {
            gameManager.removeObj(this,Layer.DEFAULT);

        }

    }
    /**
    getter fot the number of collision counter - shows how much collisions the ball has this far.
     */

    public int getBallCollisionCounter(){
        return this.collisionCounter;
    }

}
